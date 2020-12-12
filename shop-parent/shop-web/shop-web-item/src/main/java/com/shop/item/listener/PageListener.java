package com.shop.item.listener;

import com.shop.item.config.RabbitMQConfig;
import com.shop.item.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageListener {
    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {RabbitMQConfig.PAGE_CREATE_QUEUE})
    public void receiveMessage(String spuId) {
        System.out.println("shop-service-page.listener.Pagelistener SpuId = " + spuId);
        pageService.generateHtml(spuId);
    }

    @RabbitListener(queues = {RabbitMQConfig.SEARCH_DEL_QUEUE})
    public void deleteTemplate(String spuId) {
        System.out.println("shop-service-page.listener.Pagelistener SpuId = " + spuId);
        pageService.deleteHtml(spuId);
    }
}
