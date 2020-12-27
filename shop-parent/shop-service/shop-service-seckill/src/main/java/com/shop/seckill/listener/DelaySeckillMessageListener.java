package com.shop.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.shop.entity.SeckillStatus;
import com.shop.pay.feign.WechatPayFeign;
import com.shop.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DelaySeckillMessageListener {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WechatPayFeign wechatPayFeign;

    private static final String USER_SECKILL_QUEUE_STATUS = "UserQueueStatus";

    @RabbitListener(queues = {"seckillListenerQueue"})
    public void getMessage(String message){
        try{
            SeckillStatus userStatus = JSON.parseObject(message, SeckillStatus.class);
            SeckillStatus status = (SeckillStatus) redisTemplate.boundHashOps(USER_SECKILL_QUEUE_STATUS).get(userStatus.getUsername());

            // if there is user queue info , close order and rollback inventory
            if(status != null) {
                // close wechat payment
                wechatPayFeign.closeOrder(status.getOrderId().toString());
                // delete order
                seckillOrderService.deleteSeckillOrder(status.getUsername());
            }

        } catch(Exception e) {

        }
    }
}
