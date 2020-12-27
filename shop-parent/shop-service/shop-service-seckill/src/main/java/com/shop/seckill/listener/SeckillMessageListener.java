package com.shop.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.shop.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SeckillMessageListener {

    @Autowired
    private SeckillOrderService seckillOrderService;

    /***
     * get order payment info from wechat
     */
    @RabbitListener(queues = {"${mq.pay.queue.seckillorder}"})
    public void getMessage(String message) {
        try {
            Map<String, String> resultMap = JSON.parseObject(message, Map.class);
            String return_code = resultMap.get("return_code");
            String transactionId = resultMap.get("transaction_id");
            String timeEnd = resultMap.get("time_end");
            String attach = resultMap.get("attach");
            Map<String, String> attachMap = JSON.parseObject(attach, Map.class);
            String username = attachMap.get("username");

            if("SUCCESS".equals(return_code)) {
                // update order status
                seckillOrderService.updatePayStatus(username, transactionId, timeEnd);
            } else {
                // delete order , rollbcak inventory
                seckillOrderService.deleteSeckillOrder(username);
            }
        } catch (Exception e) {

        }
    }

}
