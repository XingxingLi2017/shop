package com.shop.seckill.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.entity.SeckillStatus;
import com.shop.seckill.dao.SeckillGoodsMapper;
import com.shop.seckill.dao.SeckillOrderMapper;
import com.shop.seckill.pojo.SeckillGoods;
import com.shop.seckill.pojo.SeckillOrder;
import com.shop.seckill.service.SeckillOrderService;
import com.shop.seckill.task.MultiThreadingCreateOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    private static final String ORDER_REDIS_KEY = "SeckillOrder";
    private static final String GOODS_REDIS_PREFIX = "SeckillGoods_";

    // prevent one user placing multiple orders
    private static final String USER_SECKILL_QUEUE = "UserQueue";

    // query user order's status
    private static final String USER_SECKILL_QUEUE_STATUS = "UserQueueStatus";

    // prevent user from placing multiple orders on the same product
    private static final String USER_SECKILL_QUEUE_COUNT= "UserQueueCount";

    // queue for preventing oversold
    private static final String GOODS_OVERSOLD_QUEUE_PREFIX = "GoodsOversoldQueue_";


    /***
     * place seckill order by multiple threads
     * push seckill order status into queue in redis
     * @param time
     * @param id
     * @param username
     * @return
     */
    @Override
    public Boolean add(String time, Long id, String username) {

        // mark the user in the queue
        // user can only buy one product once
        Long count = redisTemplate.boundHashOps(USER_SECKILL_QUEUE_COUNT).increment(username, 1);
        if(count > 1) {
            // 100 represents user is already in queue
            throw new RuntimeException("100");
        }

        SeckillStatus status = new SeckillStatus(username, new Date(), 1, id, time);
        // map used for query seckill order status
        redisTemplate.boundHashOps(USER_SECKILL_QUEUE_STATUS).put(username, status);
        // enqueue
        redisTemplate.boundListOps(USER_SECKILL_QUEUE).leftPush(status);

        // start a thread to place order
        multiThreadingCreateOrder.createOrder();
        return true;
    }

    /***
     * query user's status in the queue
     * @param username
     * @return
     */
    @Override
    public SeckillStatus queryStatus(String username) {
        return (SeckillStatus) redisTemplate.boundHashOps(USER_SECKILL_QUEUE_STATUS).get(username);
    }

    /***
     * update payment status for seckill order
     * @param username
     * @param transactionId
     * @param endTime
     */
    @Override
    public void updatePayStatus(String username, String transactionId, String endTime) {
        SeckillOrder order = (SeckillOrder) redisTemplate.boundHashOps(ORDER_REDIS_KEY).get(username);
        if(order != null) {
            try {
                // update order status , paid time
                order.setStatus("1");
                order.setTransactionId(transactionId);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                Date payTimeDate = sdf.parse(endTime);
                order.setPayTime(payTimeDate);

                seckillOrderMapper.insertSelective(order);

                // delete order in order map
                redisTemplate.boundHashOps(ORDER_REDIS_KEY).delete(username);

                // clear user info in queue
                clearUserQueue(username);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * delete seckill order in redis
     * @param username
     */
    @Override
    public void deleteSeckillOrder(String username) {

        // delete placed order in redis
        redisTemplate.boundHashOps(ORDER_REDIS_KEY).delete(username);

        // get user status in queue
        SeckillStatus status = (SeckillStatus) redisTemplate.boundHashOps(USER_SECKILL_QUEUE_STATUS).get(username);
        clearUserQueue(username);

        // rollback inventory
        // check redis
        String redisGoodsKey = GOODS_REDIS_PREFIX + status.getTime();
        SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(redisGoodsKey).get(status.getGoodsId());
        if(goods == null) {
            // update inventory in DB
            goods = seckillGoodsMapper.selectByPrimaryKey(status.getGoodsId());
            goods.setStockCount(1);
            seckillGoodsMapper.updateByPrimaryKeySelective(goods);
        } else {
            goods.setStockCount(goods.getStockCount() + 1);
        }

        // update inventory in Redis
        redisTemplate.boundHashOps(redisGoodsKey).put(goods.getId(), goods);
        redisTemplate.boundHashOps(GOODS_OVERSOLD_QUEUE_PREFIX).increment(goods.getId(), 1);

    }

    private void clearUserQueue(String username) {
        redisTemplate.boundHashOps(USER_SECKILL_QUEUE_COUNT).delete(username);
        redisTemplate.boundHashOps(USER_SECKILL_QUEUE_STATUS).delete(username);
    }

    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(seckillOrder);
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder){
        Example example = createExample(seckillOrder);
        return seckillOrderMapper.selectByExample(example);
    }

    public Example createExample(SeckillOrder seckillOrder){
        Example example=new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillOrder!=null){
            if(!StringUtils.isEmpty(seckillOrder.getId())){
                    criteria.andEqualTo("id",seckillOrder.getId());
            }
            if(!StringUtils.isEmpty(seckillOrder.getSeckillId())){
                    criteria.andEqualTo("seckillId",seckillOrder.getSeckillId());
            }
            if(!StringUtils.isEmpty(seckillOrder.getMoney())){
                    criteria.andEqualTo("money",seckillOrder.getMoney());
            }
            if(!StringUtils.isEmpty(seckillOrder.getUserId())){
                    criteria.andEqualTo("userId",seckillOrder.getUserId());
            }
            if(!StringUtils.isEmpty(seckillOrder.getCreateTime())){
                    criteria.andEqualTo("createTime",seckillOrder.getCreateTime());
            }
            if(!StringUtils.isEmpty(seckillOrder.getPayTime())){
                    criteria.andEqualTo("payTime",seckillOrder.getPayTime());
            }
            if(!StringUtils.isEmpty(seckillOrder.getStatus())){
                    criteria.andEqualTo("status",seckillOrder.getStatus());
            }
            if(!StringUtils.isEmpty(seckillOrder.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",seckillOrder.getReceiverAddress());
            }
            if(!StringUtils.isEmpty(seckillOrder.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",seckillOrder.getReceiverMobile());
            }
            if(!StringUtils.isEmpty(seckillOrder.getReceiver())){
                    criteria.andEqualTo("receiver",seckillOrder.getReceiver());
            }
            if(!StringUtils.isEmpty(seckillOrder.getTransactionId())){
                    criteria.andEqualTo("transactionId",seckillOrder.getTransactionId());
            }
        }
        return example;
    }

    @Override
    public void delete(Long id){
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(SeckillOrder seckillOrder){
        seckillOrderMapper.updateByPrimaryKeySelective(seckillOrder);
    }

    @Override
    public void add(SeckillOrder seckillOrder){
        seckillOrderMapper.insertSelective(seckillOrder);
    }

    @Override
    public SeckillOrder findById(Long id){
        return  seckillOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }

}
