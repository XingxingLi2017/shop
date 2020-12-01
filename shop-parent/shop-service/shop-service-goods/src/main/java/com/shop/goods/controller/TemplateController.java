package com.shop.goods.controller;

import com.github.pagehelper.Page;
import com.shop.entity.PageResult;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Template;
import com.shop.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    TemplateService templateService;

    @GetMapping
    public Result findAll(){
        List<Template> templateList = templateService.findAll();
        return new Result(true, StatusCode.OK,"Get templates successfully.",templateList) ;
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        Template template = templateService.findById(id);
        return new Result(true,StatusCode.OK,"Get template successfully",template);
    }

    @PostMapping
    public Result add(@RequestBody Template template){
        templateService.add(template);
        return new Result(true,StatusCode.OK,"Add template successfully");
    }

    @PutMapping(value="/{id}")
    public Result update(@RequestBody Template template,@PathVariable Integer id){
        template.setId(id);
        templateService.update(template);
        return new Result(true,StatusCode.OK,"Update template successfully.");
    }

    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Integer id){
        templateService.delete(id);
        return new Result(true,StatusCode.OK,"Delete template successfully.");
    }

    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<Template> list = templateService.findList(searchMap);
        return new Result(true,StatusCode.OK,"Get templates successfully.",list);
    }

    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Template> pageList = templateService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"Get templates successfully.",pageResult);
    }
}
