package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.OrderConfig;
import com.shop.order.service.OrderConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orderConfig")
@CrossOrigin
public class OrderConfigController {

    @Autowired
    private OrderConfigService orderConfigService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  OrderConfig orderConfig, @PathVariable  int page, @PathVariable  int size){
        PageInfo<OrderConfig> pageInfo = orderConfigService.findPage(orderConfig, page, size);
        return new Result(true,StatusCode.OK,"Get OrderConfig successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<OrderConfig> pageInfo = orderConfigService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get OrderConfig successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<OrderConfig>> findList(@RequestBody(required = false)  OrderConfig orderConfig){
        List<OrderConfig> list = orderConfigService.findList(orderConfig);
        return new Result<List<OrderConfig>>(true,StatusCode.OK,"Get OrderConfig successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Integer id){
        //调用OrderConfigService实现根据主键删除
        orderConfigService.delete(id);
        return new Result(true,StatusCode.OK,"Delete OrderConfig successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  OrderConfig orderConfig,@PathVariable Integer id){
        orderConfig.setId(id);
        orderConfigService.update(orderConfig);
        return new Result(true,StatusCode.OK,"Update OrderConfig successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   OrderConfig orderConfig){
        //调用OrderConfigService实现添加OrderConfig
        orderConfigService.add(orderConfig);
        return new Result(true,StatusCode.OK,"Add OrderConfig successfully.");
    }

    @GetMapping("/{id}")
    public Result<OrderConfig> findById(@PathVariable Integer id){
        OrderConfig orderConfig = orderConfigService.findById(id);
        return new Result<OrderConfig>(true,StatusCode.OK,"Get OrderConfig successfully.",orderConfig);
    }

    @GetMapping
    public Result<List<OrderConfig>> findAll(){
        List<OrderConfig> list = orderConfigService.findAll();
        return new Result<List<OrderConfig>>(true, StatusCode.OK,"Get OrderConfig successfully.",list) ;
    }
}
