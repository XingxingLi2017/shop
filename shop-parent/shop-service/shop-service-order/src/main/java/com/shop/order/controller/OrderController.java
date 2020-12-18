package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.Order;
import com.shop.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Order order, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Order> pageInfo = orderService.findPage(order, page, size);
        return new Result(true,StatusCode.OK,"Get Order successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Order> pageInfo = orderService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get Order successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Order>> findList(@RequestBody(required = false)  Order order){
        List<Order> list = orderService.findList(order);
        return new Result<List<Order>>(true,StatusCode.OK,"Get Order successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        //调用OrderService实现根据主键删除
        orderService.delete(id);
        return new Result(true,StatusCode.OK,"Delete Order successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  Order order,@PathVariable String id){
        order.setId(id);
        orderService.update(order);
        return new Result(true,StatusCode.OK,"Update Order successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Order order){
        //调用OrderService实现添加Order
        orderService.add(order);
        return new Result(true,StatusCode.OK,"Add Order successfully.");
    }

    @GetMapping("/{id}")
    public Result<Order> findById(@PathVariable String id){
        Order order = orderService.findById(id);
        return new Result<Order>(true,StatusCode.OK,"Get Order successfully.",order);
    }

    @GetMapping
    public Result<List<Order>> findAll(){
        List<Order> list = orderService.findAll();
        return new Result<List<Order>>(true, StatusCode.OK,"Get Order successfully.",list) ;
    }
}
