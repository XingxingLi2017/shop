package com.shop.canal;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.shop.content.feign.ContentFeign;
import com.shop.content.pojo.Content;
import com.shop.entity.Result;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@CanalEventListener
public class CanalDataEventListener {

    @Autowired
    ContentFeign contentFeign;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /****
     * listen adding event in mysql DB
     * @param eventType
     * @param rowData
     */
//    @InsertListenPoint
//    public void onEventInsert(CanalEntry.EventType eventType , CanalEntry.RowData rowData){
        // get table data after inserting
//        List<CanalEntry.Column> list = rowData.getAfterColumnsList();
//        for(CanalEntry.Column column : list) {
//            System.out.println("Colum name : + "+column.getName()+" --- Data : " + column.getValue());
//        }
//    }

//    @UpdateListenPoint
//    public void onEvent1(CanalEntry.RowData rowData) {
//        //do something...
//    }

//    @DeleteListenPoint
//    public void onEvent3(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        // get table data after deleting
//        List<CanalEntry.Column> list = rowData.getBeforeColumnsList();
//        for(CanalEntry.Column column : list) {
//            System.out.println("Colum name : + "+column.getName()+" --- Data : " + column.getValue());
//        }
//    }

    /***
     * customized listener
     * @param eventType
     * @param rowData
     */
    @ListenPoint(
            destination = "example",     // specify canal instance location
            schema = "changgou_content",  // specify DB name
            //table = {"t_user", "test_table"},    // specify DB table names , default all the tables
            eventType = {
                            CanalEntry.EventType.UPDATE,
                            CanalEntry.EventType.INSERT,
                            CanalEntry.EventType.DELETE
                        })
    public void onEvent4(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId = getCategoryId(eventType, rowData);
        // get content list from shop-content-service micro service
        Result<List<Content>> contentList = contentFeign.findByCategory(Long.valueOf(categoryId));
        List<Content> data = contentList.getData();

        // store data into reids cache
        stringRedisTemplate.boundValueOps("content_"+categoryId).set(JSON.toJSONString(data));
    }

    private String getCategoryId(CanalEntry.EventType eventType, CanalEntry.RowData rowData){
        String value = "";
        if(eventType == CanalEntry.EventType.DELETE) {
            for(CanalEntry.Column col : rowData.getBeforeColumnsList()) {
                if(col.getName().equalsIgnoreCase("category_id")) {
                    value = col.getValue();
                    return value;
                }
            }
        } else {
            for(CanalEntry.Column col : rowData.getAfterColumnsList()) {
                if(col.getName().equalsIgnoreCase("category_id")) {
                    value = col.getValue();
                    return value;
                }
            }
        }
        return value;
    }

}
