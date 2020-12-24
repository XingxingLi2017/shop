package com.shop.seckill.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.seckill.pojo.SeckillGoods;
import com.shop.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/seckillGoods")
@CrossOrigin
public class SeckillGoodsController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  SeckillGoods seckillGoods, @PathVariable  int page, @PathVariable  int size){
        PageInfo<SeckillGoods> pageInfo = seckillGoodsService.findPage(seckillGoods, page, size);
        return new Result(true,StatusCode.OK,"Get SeckillGoods successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<SeckillGoods> pageInfo = seckillGoodsService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get SeckillGoods successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<SeckillGoods>> findList(@RequestBody(required = false)  SeckillGoods seckillGoods){
        List<SeckillGoods> list = seckillGoodsService.findList(seckillGoods);
        return new Result<List<SeckillGoods>>(true,StatusCode.OK,"Get SeckillGoods successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Long id){
        seckillGoodsService.delete(id);
        return new Result(true,StatusCode.OK,"Delete SeckillGoods successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  SeckillGoods seckillGoods,@PathVariable Long id){
        seckillGoods.setId(id);
        seckillGoodsService.update(seckillGoods);
        return new Result(true,StatusCode.OK,"Update SeckillGoods successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   SeckillGoods seckillGoods){
        seckillGoodsService.add(seckillGoods);
        return new Result(true,StatusCode.OK,"Add SeckillGoods successfully.");
    }

    @GetMapping("/{id}")
    public Result<SeckillGoods> findById(@PathVariable Long id){
        SeckillGoods seckillGoods = seckillGoodsService.findById(id);
        return new Result<SeckillGoods>(true,StatusCode.OK,"Get SeckillGoods successfully.",seckillGoods);
    }

    @GetMapping
    public Result<List<SeckillGoods>> findAll(){
        List<SeckillGoods> list = seckillGoodsService.findAll();
        return new Result<List<SeckillGoods>>(true, StatusCode.OK,"Get SeckillGoods successfully.",list) ;
    }
}
