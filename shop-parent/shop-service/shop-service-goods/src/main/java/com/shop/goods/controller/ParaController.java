package com.shop.goods.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Para;
import com.shop.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/para")
@CrossOrigin
public class ParaController {

    @Autowired
    private ParaService paraService;

    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody Para para, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Para> pageInfo = paraService.findPage(para, page, size);
        return new Result(true, StatusCode.OK,"Get param successfully.",pageInfo);
    }

    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Para> pageInfo = paraService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get param successfully.",pageInfo);
    }

    @PostMapping(value = "/search" )
    public Result<List<Para>> findList(@RequestBody(required = false)  Para para){
        List<Para> list = paraService.findList(para);
        return new Result<List<Para>>(true,StatusCode.OK,"Get param successfully.",list);
    }

    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Integer id){
        paraService.delete(id);
        return new Result(true,StatusCode.OK,"Delete successfully.");
    }

    @PutMapping(value="/{id}")
    public Result update(@RequestBody Para para,@PathVariable Integer id){
        para.setId(id);
        paraService.update(para);
        return new Result(true,StatusCode.OK,"Update successfully.");
    }

    @PostMapping
    public Result add(@RequestBody Para para){
        paraService.add(para);
        return new Result(true,StatusCode.OK,"Add successfully.");
    }

    @GetMapping("/{id}")
    public Result<Para> findById(@PathVariable Integer id){
        Para para = paraService.findById(id);
        return new Result<Para>(true,StatusCode.OK,"Get param successfully.",para);
    }

    @GetMapping
    public Result<List<Para>> findAll(){
        List<Para> list = paraService.findAll();
        return new Result<List<Para>>(true, StatusCode.OK,"Get param successfully.",list) ;
    }

    @GetMapping("/category/{id}")
    public Result<List<Para>> findByCategoryId(@PathVariable Integer id) {
        List<Para> byCategory = paraService.findByCategory(id);
        return new Result(true, StatusCode.OK, "Get param successfully.", byCategory);
    }
}
