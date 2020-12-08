package com.shop.goods.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Sku;
import com.shop.goods.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    @GetMapping("/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable String status){
        List<Sku> list = skuService.findByStatus(status);
        return new Result<List<Sku>>(true,StatusCode.OK,"Get successfully.",list);
    }

    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Sku sku, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Sku> pageInfo = skuService.findPage(sku, page, size);
        return new Result(true, StatusCode.OK,"Get successfully.",pageInfo);
    }

    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Sku> pageInfo = skuService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get successfully.",pageInfo);
    }

    @PostMapping(value = "/search" )
    public Result<List<Sku>> findList(@RequestBody(required = false)  Sku sku){
        List<Sku> list = skuService.findList(sku);
        return new Result<List<Sku>>(true,StatusCode.OK,"Get successfully.",list);
    }

    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        skuService.delete(id);
        return new Result(true,StatusCode.OK,"Delete successfully.");
    }

    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Sku sku,@PathVariable String id){
        sku.setId(id);
        skuService.update(sku);
        return new Result(true,StatusCode.OK,"Update successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Sku sku){
        skuService.add(sku);
        return new Result(true,StatusCode.OK,"Add successfully.");
    }

    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable String id){
        Sku sku = skuService.findById(id);
        return new Result<Sku>(true,StatusCode.OK,"Get successfully.",sku);
    }

    @GetMapping
    public Result<List<Sku>> findAll(){
        List<Sku> list = skuService.findAll();
        return new Result<List<Sku>>(true, StatusCode.OK,"Get successfully.",list) ;
    }
}
