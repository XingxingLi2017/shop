package com.shop.user.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.user.pojo.Areas;
import com.shop.user.service.AreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/areas")
@CrossOrigin
public class AreasController {

    @Autowired
    private AreasService areasService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Areas areas, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Areas> pageInfo = areasService.findPage(areas, page, size);
        return new Result(true,StatusCode.OK,"Get Areas successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Areas> pageInfo = areasService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get Areas successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Areas>> findList(@RequestBody(required = false)  Areas areas){
        List<Areas> list = areasService.findList(areas);
        return new Result<List<Areas>>(true,StatusCode.OK,"Get Areas successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        areasService.delete(id);
        return new Result(true,StatusCode.OK,"Delete Areas successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  Areas areas,@PathVariable String id){
        areas.setAreaid(id);
        areasService.update(areas);
        return new Result(true,StatusCode.OK,"Update Areas successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Areas areas){
        areasService.add(areas);
        return new Result(true,StatusCode.OK,"Add Areas successfully.");
    }

    @GetMapping("/{id}")
    public Result<Areas> findById(@PathVariable String id){
        Areas areas = areasService.findById(id);
        return new Result<Areas>(true,StatusCode.OK,"Get Areas successfully.",areas);
    }

    @GetMapping
    public Result<List<Areas>> findAll(){
        List<Areas> list = areasService.findAll();
        return new Result<List<Areas>>(true, StatusCode.OK,"Get Areas successfully.",list) ;
    }
}
