package com.shop.pay.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RabbitMQConfig {

    // springboot environment object, load application.yml properties
    @Autowired
    private Environment env;

    // create queue
    @Bean
    public Queue orderQueue() {
        return new Queue(env.getProperty("mq.pay.queue.order"));
    }

    // create exchange
    @Bean
    public Exchange orderExchange() {
        return new DirectExchange(env.getProperty("mq.pay.exchange.order"));
    }

    // binding
    @Bean
    public Binding orderQueueExchange(Queue orderQueue, Exchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(env.getProperty("mq.pay.routing.key")).noargs();
    }

    // MQ queue for seckill order
    @Bean
    public Queue orderSeckillQueue() {
        return new Queue(env.getProperty("mq.pay.queue.seckillorder"));
    }

    @Bean
    public Exchange orderSeckillExchange() {
        return new DirectExchange(env.getProperty("mq.pay.exchange.seckillorder"));
    }

    @Bean
    public Binding orderSeckillQueueExchange(Queue orderSeckillQueue, Exchange orderSeckillExchange) {
        return BindingBuilder.bind(orderSeckillQueue)
                .to(orderSeckillExchange)
                .with(env.getProperty("mq.pay.routing.seckillkey"))
                .noargs();
    }
}
