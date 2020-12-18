package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.OrderLog;
import com.shop.order.service.OrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orderLog")
@CrossOrigin
public class OrderLogController {

    @Autowired
    private OrderLogService orderLogService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  OrderLog orderLog, @PathVariable  int page, @PathVariable  int size){
        PageInfo<OrderLog> pageInfo = orderLogService.findPage(orderLog, page, size);
        return new Result(true,StatusCode.OK,"Get OrderLog successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<OrderLog> pageInfo = orderLogService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get OrderLog successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<OrderLog>> findList(@RequestBody(required = false)  OrderLog orderLog){
        List<OrderLog> list = orderLogService.findList(orderLog);
        return new Result<List<OrderLog>>(true,StatusCode.OK,"Get OrderLog successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        //调用OrderLogService实现根据主键删除
        orderLogService.delete(id);
        return new Result(true,StatusCode.OK,"Delete OrderLog successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  OrderLog orderLog,@PathVariable String id){
        orderLog.setId(id);
        orderLogService.update(orderLog);
        return new Result(true,StatusCode.OK,"Update OrderLog successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   OrderLog orderLog){
        //调用OrderLogService实现添加OrderLog
        orderLogService.add(orderLog);
        return new Result(true,StatusCode.OK,"Add OrderLog successfully.");
    }

    @GetMapping("/{id}")
    public Result<OrderLog> findById(@PathVariable String id){
        OrderLog orderLog = orderLogService.findById(id);
        return new Result<OrderLog>(true,StatusCode.OK,"Get OrderLog successfully.",orderLog);
    }

    @GetMapping
    public Result<List<OrderLog>> findAll(){
        List<OrderLog> list = orderLogService.findAll();
        return new Result<List<OrderLog>>(true, StatusCode.OK,"Get OrderLog successfully.",list) ;
    }
}
