package com.shop.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.shop.canal.config.RabbitMQConfig;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/***
 * listen to DB table [tb_spu] , monitor on Spu.isMarketable, Spu.status, delete and add
 */
@CanalEventListener
public class SpuListener {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @ListenPoint(schema = {"changgou_goods"},
            table={"tb_spu"},
            eventType = {
                CanalEntry.EventType.UPDATE,
                CanalEntry.EventType.INSERT,
                CanalEntry.EventType.DELETE
            })
    public void goodsUp(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {

        // get data before changes
        Map<String, String> oldData = new HashMap<>();
        rowData.getBeforeColumnsList().forEach((column)->{
            oldData.put(column.getName(), column.getValue());
        });

        // get data after changes
        Map<String, String> newData = new HashMap<>();
        rowData.getAfterColumnsList().forEach((column)->{
            newData.put(column.getName(), column.getValue());
        });

        // when goods is deleted
        if(eventType == CanalEntry.EventType.DELETE) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_DOWN_EXCHANGE, "", oldData.get("id"));
            return;
        }

        // when goods is put on the shelf , is_marketable 0 -> 1
        if("0".equals(oldData.get("is_marketable")) && "1".equals(newData.get("is_marketable"))) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UP_EXCHANGE, "", newData.get("id"));
        }

        // poll from the shelf
        if("1".equals(oldData.get("is_marketable")) && "0".equals(newData.get("is_marketable"))) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_DOWN_EXCHANGE, "", newData.get("id"));
        }

        // status 0 -> 1 , the goods pass the audition
        if ("0".equals(oldData.get("status")) && "1".equals(newData.get("status"))) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UP_EXCHANGE, "", newData.get("id"));
        }

    }
}
