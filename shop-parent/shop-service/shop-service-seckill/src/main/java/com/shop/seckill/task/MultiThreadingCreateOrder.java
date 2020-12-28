package com.shop.seckill.task;

import com.alibaba.fastjson.JSON;
import com.shop.entity.SeckillStatus;
import com.shop.seckill.dao.SeckillGoodsMapper;
import com.shop.seckill.pojo.SeckillGoods;
import com.shop.seckill.pojo.SeckillOrder;
import com.shop.util.IdWorker;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/***
 * place order from redis queue by multiple threads
 */
@Component
public class MultiThreadingCreateOrder {

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

    // message delay queue name
    private static final String SECKILL_DELAY_MQ = "seckillDelayQueue";
    private static final String SECKILL_DELAY_ROUTING_KEY = "seckillListenerQueue";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Async
    public void createOrder(){

        try {
//            Thread.sleep(10000);

            // get order status
            SeckillStatus status = (SeckillStatus) redisTemplate.boundListOps(USER_SECKILL_QUEUE).rightPop();
            if(status == null) {
                return ;
            }

            String time = status.getTime();
            Long id = status.getGoodsId();
            String username = status.getUsername();

            // synchronization , check inventory status, prevent oversold in concurrent environment
            Long leftCount = redisTemplate.boundHashOps(GOODS_OVERSOLD_QUEUE_PREFIX).increment(id, 0);
            if(leftCount <= 0) {
                // don't have any products left , clear redis user queue
                clearUserQueue(username);
                throw new RuntimeException("The product is sold out.");
            } else {
                leftCount = redisTemplate.boundHashOps(GOODS_OVERSOLD_QUEUE_PREFIX).increment(id, -1);
            }

            // get goods info
            String redisGoodsInfoKey = GOODS_REDIS_PREFIX + time;
            SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(redisGoodsInfoKey).get(id);

            // create order
            SeckillOrder order = new SeckillOrder();
            order.setId(idWorker.nextId());
            order.setSeckillId(goods.getId());
            order.setMoney(goods.getCostPrice());
            order.setUserId(username);
            order.setCreateTime(new Date());
            order.setStatus("0"); // unpaid

            // place order , store order info in redis, duplicates are not allowed
            redisTemplate.boundHashOps(ORDER_REDIS_KEY).put(username, order);

            // decrease inventory
            goods.setStockCount(leftCount.intValue());

            // synchronize goods.stockCount , check stock count again before updating goods info in redis
            leftCount = redisTemplate.boundHashOps(GOODS_OVERSOLD_QUEUE_PREFIX).increment(id, 0);
            if( leftCount <= 0) {
                // delete goods info from current seckill event
                redisTemplate.boundHashOps(redisGoodsInfoKey).delete(goods.getId());

                // update goods stock info into DB
                goods.setStockCount(0);
                seckillGoodsMapper.updateByPrimaryKey(goods);
            } else {
                // update goods inventory, goods.stockCount is not definitely correct, it's in soft state
                redisTemplate.boundHashOps(redisGoodsInfoKey).put(goods.getId() , goods);
            }

            // update seckill order status in the map
            status.setOrderId(order.getId());
            status.setMoney(Float.valueOf(goods.getCostPrice()));
            status.setStatus(2);    //  status of waiting for paying
            redisTemplate.boundHashOps(USER_SECKILL_QUEUE_STATUS).put(username, status);

            // send message to message delay queue
            rabbitTemplate.convertAndSend(SECKILL_DELAY_MQ, (Object)JSON.toJSONString(status),new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setExpiration("60000");
                    return message;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearUserQueue(String username) {
        redisTemplate.boundHashOps(USER_SECKILL_QUEUE_COUNT).delete(username);
        redisTemplate.boundHashOps(USER_SECKILL_QUEUE_STATUS).delete(username);
    }
}
