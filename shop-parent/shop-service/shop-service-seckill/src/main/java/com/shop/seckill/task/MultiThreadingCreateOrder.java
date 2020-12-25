package com.shop.seckill.task;

import com.shop.entity.SeckillStatus;
import com.shop.seckill.dao.SeckillGoodsMapper;
import com.shop.seckill.pojo.SeckillGoods;
import com.shop.seckill.pojo.SeckillOrder;
import com.shop.util.IdWorker;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    private static final String USER_SECKILL_QUEUE = "UserQueue";
    private static final String USER_SECKILL_STATUS_MAP = "SeckillStatus";

    @Async
    public void createOrder(){

        try {
            System.out.println("Start sleep.");
            Thread.sleep(10000);

            // pop from queue in redis
            SeckillStatus status = (SeckillStatus) redisTemplate.boundListOps(USER_SECKILL_QUEUE).rightPop();
            if(status == null) {
                return ;
            }

            String time = status.getTime();
            Long id = status.getGoodsId();
            String username = status.getUsername();

            // get goods info
            String redisKey = GOODS_REDIS_PREFIX + time;
            SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps(redisKey).get(id);

            // check inventory
            if (goods == null || goods.getStockCount() <= 0) {
                throw new RuntimeException("The product is sold out.");
            }

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
            goods.setStockCount(goods.getStockCount() - 1);

            if(goods.getStockCount() <= 0) {
                // delete goods info from current seckill event
                redisTemplate.boundHashOps(redisKey).delete(goods.getId());

                // update goods stock info into DB
                seckillGoodsMapper.updateByPrimaryKey(goods);
            } else {

                // update goods inventory
                redisTemplate.boundHashOps(redisKey).put(goods.getId() , goods);
            }


            // update seckill order status in the map
            status.setOrderId(order.getId());
            status.setMoney(Float.valueOf(goods.getCostPrice()));
            status.setStatus(2); // wait for paying
            redisTemplate.boundHashOps(USER_SECKILL_STATUS_MAP).put(username, status);

            System.out.println("Place order successfully.");

        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
