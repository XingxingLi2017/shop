package com.shop.user.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.user.pojo.Cities;
import com.shop.user.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cities")
@CrossOrigin
public class CitiesController {

    @Autowired
    private CitiesService citiesService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Cities cities, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Cities> pageInfo = citiesService.findPage(cities, page, size);
        return new Result(true,StatusCode.OK,"Get Cities successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Cities> pageInfo = citiesService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get Cities successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Cities>> findList(@RequestBody(required = false)  Cities cities){
        List<Cities> list = citiesService.findList(cities);
        return new Result<List<Cities>>(true,StatusCode.OK,"Get Cities successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        citiesService.delete(id);
        return new Result(true,StatusCode.OK,"Delete Cities successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  Cities cities,@PathVariable String id){
        cities.setCityid(id);
        citiesService.update(cities);
        return new Result(true,StatusCode.OK,"Update Cities successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Cities cities){
        citiesService.add(cities);
        return new Result(true,StatusCode.OK,"Add Cities successfully.");
    }

    @GetMapping("/{id}")
    public Result<Cities> findById(@PathVariable String id){
        Cities cities = citiesService.findById(id);
        return new Result<Cities>(true,StatusCode.OK,"Get Cities successfully.",cities);
    }

    @GetMapping
    public Result<List<Cities>> findAll(){
        List<Cities> list = citiesService.findAll();
        return new Result<List<Cities>>(true, StatusCode.OK,"Get Cities successfully.",list) ;
    }
}
