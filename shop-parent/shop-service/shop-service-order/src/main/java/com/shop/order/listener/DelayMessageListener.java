package com.shop.order.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/***
 * listen on delay queue in rabbitmq, used to check order payment status
 */
@Component
@RabbitListener(queues={"orderListenerQueue"})
public class DelayMessageListener {

    @RabbitHandler
    public void getDealyMessage(String orderId) {
        System.out.println(orderId);
        // cancel order and rollback inventory

    }

}
