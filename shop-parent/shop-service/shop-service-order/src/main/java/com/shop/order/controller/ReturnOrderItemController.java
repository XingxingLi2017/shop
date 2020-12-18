package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.ReturnOrderItem;
import com.shop.order.service.ReturnOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/returnOrderItem")
@CrossOrigin
public class ReturnOrderItemController {

    @Autowired
    private ReturnOrderItemService returnOrderItemService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  ReturnOrderItem returnOrderItem, @PathVariable  int page, @PathVariable  int size){
        PageInfo<ReturnOrderItem> pageInfo = returnOrderItemService.findPage(returnOrderItem, page, size);
        return new Result(true,StatusCode.OK,"Get ReturnOrderItem successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<ReturnOrderItem> pageInfo = returnOrderItemService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get ReturnOrderItem successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<ReturnOrderItem>> findList(@RequestBody(required = false)  ReturnOrderItem returnOrderItem){
        List<ReturnOrderItem> list = returnOrderItemService.findList(returnOrderItem);
        return new Result<List<ReturnOrderItem>>(true,StatusCode.OK,"Get ReturnOrderItem successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Long id){
        //调用ReturnOrderItemService实现根据主键删除
        returnOrderItemService.delete(id);
        return new Result(true,StatusCode.OK,"Delete ReturnOrderItem successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  ReturnOrderItem returnOrderItem,@PathVariable Long id){
        returnOrderItem.setId(id);
        returnOrderItemService.update(returnOrderItem);
        return new Result(true,StatusCode.OK,"Update ReturnOrderItem successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   ReturnOrderItem returnOrderItem){
        //调用ReturnOrderItemService实现添加ReturnOrderItem
        returnOrderItemService.add(returnOrderItem);
        return new Result(true,StatusCode.OK,"Add ReturnOrderItem successfully.");
    }

    @GetMapping("/{id}")
    public Result<ReturnOrderItem> findById(@PathVariable Long id){
        ReturnOrderItem returnOrderItem = returnOrderItemService.findById(id);
        return new Result<ReturnOrderItem>(true,StatusCode.OK,"Get ReturnOrderItem successfully.",returnOrderItem);
    }

    @GetMapping
    public Result<List<ReturnOrderItem>> findAll(){
        List<ReturnOrderItem> list = returnOrderItemService.findAll();
        return new Result<List<ReturnOrderItem>>(true, StatusCode.OK,"Get ReturnOrderItem successfully.",list) ;
    }
}
