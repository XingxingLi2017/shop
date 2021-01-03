package com.shop.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.goods.feign.SkuFeign;
import com.shop.order.dao.OrderItemMapper;
import com.shop.order.dao.OrderMapper;
import com.shop.order.pojo.Order;
import com.shop.order.pojo.OrderItem;
import com.shop.order.service.OrderService;
import com.shop.user.feign.UserFeign;
import com.shop.util.IdWorker;
import com.shop.util.TokenDecoder;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /***
     * place order and sent order info to message delay queue
     * @param order
     */
    @GlobalTransactional
    @Override
    public void add(Order order){
        String username = TokenDecoder.getUserInfo().get("username");

        // add order common info into tb_order

        order.setId(idWorker.nextId() + "");
        order.setUsername(username);
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setSourceType("1");  // comes from web
        order.setOrderStatus("0");  // not completed
        order.setPayStatus("0");
        order.setIsDelete("0");

        // get total number and money of products in cart
        List<OrderItem> orderItems = redisTemplate.boundHashOps("Cart_" + username).values();
        int totalNum = 0;
        int totalMoney = 0;
        Map<String, String> decrMap = new HashMap<>();  // used for decrease inventory

        for(OrderItem item : orderItems) {
            totalNum += item.getNum();
            totalMoney += item.getMoney();

            item.setId(idWorker.nextId() + "");
            item.setOrderId(order.getId());
            item.setIsReturn("0");

            decrMap.put(item.getSkuId().toString(), item.getNum().toString());
        }

        // validate order price with frontend
        if(order.getTotalMoney() != null && order.getTotalMoney() != totalMoney) {
            throw new RuntimeException("Order total money is different with input total money.");
        }
        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalMoney);
        orderMapper.insertSelective(order);

        // add order details into tb_order_item
        for(OrderItem item : orderItems) {
            orderItemMapper.insertSelective(item);
        }

        // decrease inventory in GOODS service
        skuFeign.decrCount(decrMap);

        // add user points for username , representing user activity
        userFeign.addPoints(1);

        // send order info into delay queue in rabbitmq
        rabbitTemplate.convertAndSend("orderDelayQueue", (Object) order.getId(), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // setup TTL= 20 sec for the message
                message.getMessageProperties().setExpiration("60000");
                return message;
            }
        });
    }

    @Override
    public void add(Order order, String[] skuIds) {
        String username = TokenDecoder.getUserInfo().get("username");

        // delete order items that are not in the cart now
        Set<String> idsSet = new HashSet<>(Arrays.asList(skuIds));
        Set<Long> keys = redisTemplate.boundHashOps("Cart_" + username).keys();
        for(Long key : keys) {
            if(!idsSet.contains(key + "")) {
                redisTemplate.boundHashOps("Cart_" + username).delete(key);
            }
        }

        // add order and order items
        add(order);
    }

    /****
     * logically delete order , rollback decreased inventory
     */
    @Override
    public void deleteOrder(String outTradeNo) {
        Order order = orderMapper.selectByPrimaryKey(outTradeNo);

        order.setUpdateTime(new Date());
        order.setPayStatus("2");    // fail to pay
        orderMapper.updateByPrimaryKeySelective(order);

        // rollback inventory
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        List<OrderItem> orderItems = orderItemMapper.select(item);

        Map<String, String> decrMap = new HashMap<>();
        for(OrderItem e : orderItems) {
            Long skuId = e.getSkuId();
            Integer num = 0 - e.getNum();
            decrMap.put(skuId.toString(), num.toString());
        }

        // increase inventory in GOODS service
        skuFeign.decrCount(decrMap);
    }

    /***
     * update order status after payment
     * @param outTradeNo
     * @param payTime
     * @param transactionId
     */
    @Override
    public void updateStatus(String outTradeNo, String payTime, String transactionId) {
        Order order = orderMapper.selectByPrimaryKey(outTradeNo);

        // update
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

            Date payTimeDate = sdf.parse(payTime);
            order.setPayTime(payTimeDate);
            order.setPayStatus("1");
            order.setTransactionId(transactionId);

            orderMapper.updateByPrimaryKey(order);
        } catch (ParseException e) {

        }

    }

    @Override
    public PageInfo<Order> findPage(Order order, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(order);
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Order> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    @Override
    public List<Order> findList(Order order){
        Example example = createExample(order);
        return orderMapper.selectByExample(example);
    }

    public Example createExample(Order order){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(order!=null){
            if(!StringUtils.isEmpty(order.getId())){
                    criteria.andEqualTo("id",order.getId());
            }
            if(!StringUtils.isEmpty(order.getTotalNum())){
                    criteria.andEqualTo("totalNum",order.getTotalNum());
            }
            if(!StringUtils.isEmpty(order.getTotalMoney())){
                    criteria.andEqualTo("totalMoney",order.getTotalMoney());
            }
            if(!StringUtils.isEmpty(order.getPreMoney())){
                    criteria.andEqualTo("preMoney",order.getPreMoney());
            }
            if(!StringUtils.isEmpty(order.getPostFee())){
                    criteria.andEqualTo("postFee",order.getPostFee());
            }
            if(!StringUtils.isEmpty(order.getPayMoney())){
                    criteria.andEqualTo("payMoney",order.getPayMoney());
            }
            if(!StringUtils.isEmpty(order.getPayType())){
                    criteria.andEqualTo("payType",order.getPayType());
            }
            if(!StringUtils.isEmpty(order.getCreateTime())){
                    criteria.andEqualTo("createTime",order.getCreateTime());
            }
            if(!StringUtils.isEmpty(order.getUpdateTime())){
                    criteria.andEqualTo("updateTime",order.getUpdateTime());
            }
            if(!StringUtils.isEmpty(order.getPayTime())){
                    criteria.andEqualTo("payTime",order.getPayTime());
            }
            if(!StringUtils.isEmpty(order.getConsignTime())){
                    criteria.andEqualTo("consignTime",order.getConsignTime());
            }
            if(!StringUtils.isEmpty(order.getEndTime())){
                    criteria.andEqualTo("endTime",order.getEndTime());
            }
            if(!StringUtils.isEmpty(order.getCloseTime())){
                    criteria.andEqualTo("closeTime",order.getCloseTime());
            }
            if(!StringUtils.isEmpty(order.getShippingName())){
                    criteria.andEqualTo("shippingName",order.getShippingName());
            }
            if(!StringUtils.isEmpty(order.getShippingCode())){
                    criteria.andEqualTo("shippingCode",order.getShippingCode());
            }
            if(!StringUtils.isEmpty(order.getUsername())){
                    criteria.andLike("username","%"+order.getUsername()+"%");
            }
            if(!StringUtils.isEmpty(order.getBuyerMessage())){
                    criteria.andEqualTo("buyerMessage",order.getBuyerMessage());
            }
            if(!StringUtils.isEmpty(order.getBuyerRate())){
                    criteria.andEqualTo("buyerRate",order.getBuyerRate());
            }
            if(!StringUtils.isEmpty(order.getReceiverContact())){
                    criteria.andEqualTo("receiverContact",order.getReceiverContact());
            }
            if(!StringUtils.isEmpty(order.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",order.getReceiverMobile());
            }
            if(!StringUtils.isEmpty(order.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",order.getReceiverAddress());
            }
            if(!StringUtils.isEmpty(order.getSourceType())){
                    criteria.andEqualTo("sourceType",order.getSourceType());
            }
            if(!StringUtils.isEmpty(order.getTransactionId())){
                    criteria.andEqualTo("transactionId",order.getTransactionId());
            }
            if(!StringUtils.isEmpty(order.getOrderStatus())){
                    criteria.andEqualTo("orderStatus",order.getOrderStatus());
            }
            if(!StringUtils.isEmpty(order.getPayStatus())){
                    criteria.andEqualTo("payStatus",order.getPayStatus());
            }
            if(!StringUtils.isEmpty(order.getConsignStatus())){
                    criteria.andEqualTo("consignStatus",order.getConsignStatus());
            }
            if(!StringUtils.isEmpty(order.getIsDelete())){
                    criteria.andEqualTo("isDelete",order.getIsDelete());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }
}
