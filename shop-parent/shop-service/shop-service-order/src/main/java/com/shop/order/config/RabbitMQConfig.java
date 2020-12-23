package com.shop.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * Implement delay queue with DLX and dead letter queue in RabbitMQ for order payment
 */

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue orderDelayQueue(){
        return QueueBuilder
                .durable("orderDelayQueue")
                .withArgument("x-dead-letter-exchange", "orderListenerExchange")         // dead letter exchange
                .withArgument("x-dead-letter-routing-key", "orderListenerQueue")
                .build();
    }

    @Bean
    public Queue orderListenerQueue(){
        return new Queue("orderListenerQueue", true);

    }

    @Bean
    public Exchange orderListenerExchange(){
        return new DirectExchange("orderListenerExchange");
    }

    @Bean
    public Binding orderListenerBinding(Queue orderListenerQueue, Exchange orderListenerExchange){
        return BindingBuilder
                .bind(orderListenerQueue)
                .to(orderListenerExchange)
                .with("orderListenerQueue")
                .noargs();
    }

}
