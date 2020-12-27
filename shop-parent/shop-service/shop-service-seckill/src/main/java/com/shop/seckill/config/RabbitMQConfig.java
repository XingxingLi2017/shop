package com.shop.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    /***
     * delay queue for seckill order payment status check
     */
    @Bean
    public Queue seckillDelayQueue(){
        return QueueBuilder
                .durable("seckillDelayQueue")
                .withArgument("x-dead-letter-exchange", "seckillListenerExchange")         // dead letter exchange
                .withArgument("x-dead-letter-routing-key", "seckillListenerQueue")
                .build();
    }

    @Bean
    public Queue seckillListenerQueue(){
        return new Queue("seckillListenerQueue", true);

    }

    @Bean
    public Exchange seckillListenerExchange(){
        return new DirectExchange("seckillListenerExchange");
    }

    @Bean
    public Binding orderListenerBinding(Queue seckillListenerQueue, Exchange seckillListenerExchange){
        return BindingBuilder
                .bind(seckillListenerQueue)
                .to(seckillListenerExchange)
                .with("seckillListenerQueue")
                .noargs();
    }
}
