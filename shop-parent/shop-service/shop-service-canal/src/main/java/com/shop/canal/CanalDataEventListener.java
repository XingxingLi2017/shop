package com.shop.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;

import java.util.List;

@CanalEventListener
public class CanalDataEventListener {

    /****
     * listen adding event in mysql DB
     * @param eventType
     * @param rowData
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType , CanalEntry.RowData rowData){
        // get table data after inserting
        List<CanalEntry.Column> list = rowData.getAfterColumnsList();
        for(CanalEntry.Column column : list) {
            System.out.println("Colum name : + "+column.getName()+" --- Data : " + column.getValue());
        }
    }

    @UpdateListenPoint
    public void onEvent1(CanalEntry.RowData rowData) {
        //do something...
    }

    @DeleteListenPoint
    public void onEvent3(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        // get table data after deleting
        List<CanalEntry.Column> list = rowData.getBeforeColumnsList();
        for(CanalEntry.Column column : list) {
            System.out.println("Colum name : + "+column.getName()+" --- Data : " + column.getValue());
        }
    }

    /***
     * customized listener
     * @param eventType
     * @param rowData
     */
    @ListenPoint(
            destination = "example",     // specify canal instance location
            schema = "changgou_content",  // specify DB name
            //table = {"t_user", "test_table"},    // specify DB table names , default all the tables
            eventType = CanalEntry.EventType.UPDATE)
    public void onEvent4(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        List<CanalEntry.Column> list = rowData.getAfterColumnsList();
        for(CanalEntry.Column column : list) {
            System.out.println("Colum name : + "+column.getName()+" --- Data : " + column.getValue());
        }
    }


}
