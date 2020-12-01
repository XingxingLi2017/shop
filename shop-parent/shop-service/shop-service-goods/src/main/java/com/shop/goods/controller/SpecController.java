package com.shop.goods.controller;

import com.github.pagehelper.Page;
import com.shop.entity.PageResult;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Spec;
import com.shop.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/spec")
public class SpecController {

    @Autowired
    SpecService specService;

    @GetMapping
    public Result findAll(){
        List<Spec> list = specService.findAll();
        return new Result(true, StatusCode.OK, "Get spec successfully.", list);
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        Spec spec = specService.findById(id);
        return new Result(true,StatusCode.OK,"Get spec successfully.",spec);
    }

    @PostMapping
    public Result add(@RequestBody Spec spec){
        specService.add(spec);
        return new Result(true,StatusCode.OK,"Add spec successfully.");
    }

    @PutMapping(value="/{id}")
    public Result update(@RequestBody Spec spec,@PathVariable Integer id){
        spec.setId(id);
        specService.update(spec);
        return new Result(true,StatusCode.OK,"Update spec successfully.");
    }

    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Integer id){
        specService.delete(id);
        return new Result(true,StatusCode.OK,"Delete spec successfully.");
    }

    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<Spec> list = specService.findList(searchMap);
        return new Result(true,StatusCode.OK,"Get spec successfully.",list);
    }

    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Spec> pageList = specService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"Get spec successfully.", pageResult);
    }

    @GetMapping("/category/{categoryName}")
    public Result<List<Map>> findSpecListByCategoryName(@PathVariable String categoryName) {
        List<Map> list = specService.findSpecListByCategoryName(categoryName);
        return new Result(true, StatusCode.OK, "Get spec successfully.", list);
    }
}
