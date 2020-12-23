package com.shop.order.listener;

import com.alibaba.fastjson.JSON;
import com.shop.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues={"${mq.pay.queue.order}"})
public class OrderMessageListener {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void getMessage(String message){
        Map<String, String> resultMap = JSON.parseObject(message, Map.class);
        // return code
        String returnCode = resultMap.get("return_code");
        if("SUCCESS".equals(returnCode)) {
            // result code
            String resultCode = resultMap.get("result_code");
            // order number in shop
            String outTradeNo = resultMap.get("out_trade_no");

            if("SUCCESS".equals(resultCode)) {
                // set order status
                orderService.updateStatus(outTradeNo, resultMap.get("time_end"), resultMap.get("transaction_id"));
                return;
            } else {
                // turn down the order in wechat
                // cancel order, rollback inventory
                orderService.deleteOrder(outTradeNo);
            }
        }
    }
}
