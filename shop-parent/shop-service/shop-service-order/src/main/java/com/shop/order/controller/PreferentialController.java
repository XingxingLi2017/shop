package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.Preferential;
import com.shop.order.service.PreferentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/preferential")
@CrossOrigin
public class PreferentialController {

    @Autowired
    private PreferentialService preferentialService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Preferential preferential, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Preferential> pageInfo = preferentialService.findPage(preferential, page, size);
        return new Result(true,StatusCode.OK,"Get Preferential successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Preferential> pageInfo = preferentialService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get Preferential successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Preferential>> findList(@RequestBody(required = false)  Preferential preferential){
        List<Preferential> list = preferentialService.findList(preferential);
        return new Result<List<Preferential>>(true,StatusCode.OK,"Get Preferential successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Integer id){
        //调用PreferentialService实现根据主键删除
        preferentialService.delete(id);
        return new Result(true,StatusCode.OK,"Delete Preferential successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  Preferential preferential,@PathVariable Integer id){
        preferential.setId(id);
        preferentialService.update(preferential);
        return new Result(true,StatusCode.OK,"Update Preferential successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Preferential preferential){
        //调用PreferentialService实现添加Preferential
        preferentialService.add(preferential);
        return new Result(true,StatusCode.OK,"Add Preferential successfully.");
    }

    @GetMapping("/{id}")
    public Result<Preferential> findById(@PathVariable Integer id){
        Preferential preferential = preferentialService.findById(id);
        return new Result<Preferential>(true,StatusCode.OK,"Get Preferential successfully.",preferential);
    }

    @GetMapping
    public Result<List<Preferential>> findAll(){
        List<Preferential> list = preferentialService.findAll();
        return new Result<List<Preferential>>(true, StatusCode.OK,"Get Preferential successfully.",list) ;
    }
}
