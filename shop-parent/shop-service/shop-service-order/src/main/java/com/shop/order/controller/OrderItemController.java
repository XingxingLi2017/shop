package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.OrderItem;
import com.shop.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orderItem")
@CrossOrigin
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  OrderItem orderItem, @PathVariable  int page, @PathVariable  int size){
        PageInfo<OrderItem> pageInfo = orderItemService.findPage(orderItem, page, size);
        return new Result(true,StatusCode.OK,"Get OrderItem successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<OrderItem> pageInfo = orderItemService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get OrderItem successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<OrderItem>> findList(@RequestBody(required = false)  OrderItem orderItem){
        List<OrderItem> list = orderItemService.findList(orderItem);
        return new Result<List<OrderItem>>(true,StatusCode.OK,"Get OrderItem successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        //调用OrderItemService实现根据主键删除
        orderItemService.delete(id);
        return new Result(true,StatusCode.OK,"Delete OrderItem successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  OrderItem orderItem,@PathVariable String id){
        orderItem.setId(id);
        orderItemService.update(orderItem);
        return new Result(true,StatusCode.OK,"Update OrderItem successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   OrderItem orderItem){
        //调用OrderItemService实现添加OrderItem
        orderItemService.add(orderItem);
        return new Result(true,StatusCode.OK,"Add OrderItem successfully.");
    }

    @GetMapping("/{id}")
    public Result<OrderItem> findById(@PathVariable String id){
        OrderItem orderItem = orderItemService.findById(id);
        return new Result<OrderItem>(true,StatusCode.OK,"Get OrderItem successfully.",orderItem);
    }

    @GetMapping
    public Result<List<OrderItem>> findAll(){
        List<OrderItem> list = orderItemService.findAll();
        return new Result<List<OrderItem>>(true, StatusCode.OK,"Get OrderItem successfully.",list) ;
    }
}
