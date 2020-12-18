package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.ReturnOrder;
import com.shop.order.service.ReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/returnOrder")
@CrossOrigin
public class ReturnOrderController {

    @Autowired
    private ReturnOrderService returnOrderService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  ReturnOrder returnOrder, @PathVariable  int page, @PathVariable  int size){
        PageInfo<ReturnOrder> pageInfo = returnOrderService.findPage(returnOrder, page, size);
        return new Result(true,StatusCode.OK,"Get ReturnOrder successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<ReturnOrder> pageInfo = returnOrderService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get ReturnOrder successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<ReturnOrder>> findList(@RequestBody(required = false)  ReturnOrder returnOrder){
        List<ReturnOrder> list = returnOrderService.findList(returnOrder);
        return new Result<List<ReturnOrder>>(true,StatusCode.OK,"Get ReturnOrder successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Long id){
        //调用ReturnOrderService实现根据主键删除
        returnOrderService.delete(id);
        return new Result(true,StatusCode.OK,"Delete ReturnOrder successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  ReturnOrder returnOrder,@PathVariable Long id){
        returnOrder.setId(id);
        returnOrderService.update(returnOrder);
        return new Result(true,StatusCode.OK,"Update ReturnOrder successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   ReturnOrder returnOrder){
        //调用ReturnOrderService实现添加ReturnOrder
        returnOrderService.add(returnOrder);
        return new Result(true,StatusCode.OK,"Add ReturnOrder successfully.");
    }

    @GetMapping("/{id}")
    public Result<ReturnOrder> findById(@PathVariable Long id){
        ReturnOrder returnOrder = returnOrderService.findById(id);
        return new Result<ReturnOrder>(true,StatusCode.OK,"Get ReturnOrder successfully.",returnOrder);
    }

    @GetMapping
    public Result<List<ReturnOrder>> findAll(){
        List<ReturnOrder> list = returnOrderService.findAll();
        return new Result<List<ReturnOrder>>(true, StatusCode.OK,"Get ReturnOrder successfully.",list) ;
    }
}
