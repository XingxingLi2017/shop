package com.shop.order.listener;

import com.shop.order.pojo.Order;
import com.shop.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * listen on delay queue in rabbitmq, used to check order payment status
 */
@Component
@RabbitListener(queues={"orderListenerQueue"})
public class DelayMessageListener {


    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void getDealyMessage(String orderId) {
        // check order payment status
        Order order = orderService.findById(orderId);

        // cancel order and rollback inventory
        if(!"1".equals(order.getPayStatus())) {
            orderService.deleteOrder(orderId);
        }
    }

}
