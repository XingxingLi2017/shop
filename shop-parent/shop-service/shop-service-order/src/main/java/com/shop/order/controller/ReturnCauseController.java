package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.ReturnCause;
import com.shop.order.service.ReturnCauseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/returnCause")
@CrossOrigin
public class ReturnCauseController {

    @Autowired
    private ReturnCauseService returnCauseService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  ReturnCause returnCause, @PathVariable  int page, @PathVariable  int size){
        PageInfo<ReturnCause> pageInfo = returnCauseService.findPage(returnCause, page, size);
        return new Result(true,StatusCode.OK,"Get ReturnCause successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<ReturnCause> pageInfo = returnCauseService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get ReturnCause successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<ReturnCause>> findList(@RequestBody(required = false)  ReturnCause returnCause){
        List<ReturnCause> list = returnCauseService.findList(returnCause);
        return new Result<List<ReturnCause>>(true,StatusCode.OK,"Get ReturnCause successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Integer id){
        //调用ReturnCauseService实现根据主键删除
        returnCauseService.delete(id);
        return new Result(true,StatusCode.OK,"Delete ReturnCause successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  ReturnCause returnCause,@PathVariable Integer id){
        returnCause.setId(id);
        returnCauseService.update(returnCause);
        return new Result(true,StatusCode.OK,"Update ReturnCause successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   ReturnCause returnCause){
        //调用ReturnCauseService实现添加ReturnCause
        returnCauseService.add(returnCause);
        return new Result(true,StatusCode.OK,"Add ReturnCause successfully.");
    }

    @GetMapping("/{id}")
    public Result<ReturnCause> findById(@PathVariable Integer id){
        ReturnCause returnCause = returnCauseService.findById(id);
        return new Result<ReturnCause>(true,StatusCode.OK,"Get ReturnCause successfully.",returnCause);
    }

    @GetMapping
    public Result<List<ReturnCause>> findAll(){
        List<ReturnCause> list = returnCauseService.findAll();
        return new Result<List<ReturnCause>>(true, StatusCode.OK,"Get ReturnCause successfully.",list) ;
    }
}
