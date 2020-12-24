package com.shop.seckill.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.seckill.pojo.SeckillOrder;
import com.shop.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/seckillOrder")
@CrossOrigin
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  SeckillOrder seckillOrder, @PathVariable  int page, @PathVariable  int size){
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(seckillOrder, page, size);
        return new Result(true,StatusCode.OK,"Get SeckillOrder successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get SeckillOrder successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<SeckillOrder>> findList(@RequestBody(required = false)  SeckillOrder seckillOrder){
        List<SeckillOrder> list = seckillOrderService.findList(seckillOrder);
        return new Result<List<SeckillOrder>>(true,StatusCode.OK,"Get SeckillOrder successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Long id){
        seckillOrderService.delete(id);
        return new Result(true,StatusCode.OK,"Delete SeckillOrder successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  SeckillOrder seckillOrder,@PathVariable Long id){
        seckillOrder.setId(id);
        seckillOrderService.update(seckillOrder);
        return new Result(true,StatusCode.OK,"Update SeckillOrder successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   SeckillOrder seckillOrder){
        seckillOrderService.add(seckillOrder);
        return new Result(true,StatusCode.OK,"Add SeckillOrder successfully.");
    }

    @GetMapping("/{id}")
    public Result<SeckillOrder> findById(@PathVariable Long id){
        SeckillOrder seckillOrder = seckillOrderService.findById(id);
        return new Result<SeckillOrder>(true,StatusCode.OK,"Get SeckillOrder successfully.",seckillOrder);
    }

    @GetMapping
    public Result<List<SeckillOrder>> findAll(){
        List<SeckillOrder> list = seckillOrderService.findAll();
        return new Result<List<SeckillOrder>>(true, StatusCode.OK,"Get SeckillOrder successfully.",list) ;
    }
}
