package com.shop.user.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.user.pojo.Provinces;
import com.shop.user.service.ProvincesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/provinces")
@CrossOrigin
public class ProvincesController {

    @Autowired
    private ProvincesService provincesService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Provinces provinces, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Provinces> pageInfo = provincesService.findPage(provinces, page, size);
        return new Result(true,StatusCode.OK,"Get Provinces successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Provinces> pageInfo = provincesService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get Provinces successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Provinces>> findList(@RequestBody(required = false)  Provinces provinces){
        List<Provinces> list = provincesService.findList(provinces);
        return new Result<List<Provinces>>(true,StatusCode.OK,"Get Provinces successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        provincesService.delete(id);
        return new Result(true,StatusCode.OK,"Delete Provinces successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  Provinces provinces,@PathVariable String id){
        provinces.setProvinceid(id);
        provincesService.update(provinces);
        return new Result(true,StatusCode.OK,"Update Provinces successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Provinces provinces){
        provincesService.add(provinces);
        return new Result(true,StatusCode.OK,"Add Provinces successfully.");
    }

    @GetMapping("/{id}")
    public Result<Provinces> findById(@PathVariable String id){
        Provinces provinces = provincesService.findById(id);
        return new Result<Provinces>(true,StatusCode.OK,"Get Provinces successfully.",provinces);
    }

    @GetMapping
    public Result<List<Provinces>> findAll(){
        List<Provinces> list = provincesService.findAll();
        return new Result<List<Provinces>>(true, StatusCode.OK,"Get Provinces successfully.",list) ;
    }
}
