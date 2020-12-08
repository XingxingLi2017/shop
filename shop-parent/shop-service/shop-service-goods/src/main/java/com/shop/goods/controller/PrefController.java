package com.shop.goods.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Pref;
import com.shop.goods.service.PrefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pref")
@CrossOrigin
public class PrefController {

    @Autowired
    private PrefService prefService;

    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Pref pref, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Pref> pageInfo = prefService.findPage(pref, page, size);
        return new Result(true, StatusCode.OK,"Get pref successfully.",pageInfo);
    }

    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Pref> pageInfo = prefService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get pref successfully.",pageInfo);
    }
    
    @PostMapping(value = "/search" )
    public Result<List<Pref>> findList(@RequestBody(required = false)  Pref pref){
        List<Pref> list = prefService.findList(pref);
        return new Result<List<Pref>>(true,StatusCode.OK,"Get pref successfully.",list);
    }

    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Integer id){
        prefService.delete(id);
        return new Result(true,StatusCode.OK,"Delete successfully.");
    }

    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Pref pref,@PathVariable Integer id){
        pref.setId(id);
        prefService.update(pref);
        return new Result(true,StatusCode.OK,"Update successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Pref pref){
        prefService.add(pref);
        return new Result(true,StatusCode.OK,"Add successfully.");
    }

    @GetMapping("/{id}")
    public Result<Pref> findById(@PathVariable Integer id){
        Pref pref = prefService.findById(id);
        return new Result<Pref>(true,StatusCode.OK,"Get pref successfully.",pref);
    }

    @GetMapping
    public Result<List<Pref>> findAll(){
        List<Pref> list = prefService.findAll();
        return new Result<List<Pref>>(true, StatusCode.OK,"Get pref successfully.",list) ;
    }
}
