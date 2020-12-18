package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.UndoLog;
import com.shop.order.service.UndoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/undoLog")
@CrossOrigin
public class UndoLogController {

    @Autowired
    private UndoLogService undoLogService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  UndoLog undoLog, @PathVariable  int page, @PathVariable  int size){
        PageInfo<UndoLog> pageInfo = undoLogService.findPage(undoLog, page, size);
        return new Result(true,StatusCode.OK,"Get UndoLog successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<UndoLog> pageInfo = undoLogService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get UndoLog successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<UndoLog>> findList(@RequestBody(required = false)  UndoLog undoLog){
        List<UndoLog> list = undoLogService.findList(undoLog);
        return new Result<List<UndoLog>>(true,StatusCode.OK,"Get UndoLog successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Long id){
        //调用UndoLogService实现根据主键删除
        undoLogService.delete(id);
        return new Result(true,StatusCode.OK,"Delete UndoLog successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  UndoLog undoLog,@PathVariable Long id){
        undoLog.setId(id);
        undoLogService.update(undoLog);
        return new Result(true,StatusCode.OK,"Update UndoLog successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   UndoLog undoLog){
        //调用UndoLogService实现添加UndoLog
        undoLogService.add(undoLog);
        return new Result(true,StatusCode.OK,"Add UndoLog successfully.");
    }

    @GetMapping("/{id}")
    public Result<UndoLog> findById(@PathVariable Long id){
        UndoLog undoLog = undoLogService.findById(id);
        return new Result<UndoLog>(true,StatusCode.OK,"Get UndoLog successfully.",undoLog);
    }

    @GetMapping
    public Result<List<UndoLog>> findAll(){
        List<UndoLog> list = undoLogService.findAll();
        return new Result<List<UndoLog>>(true, StatusCode.OK,"Get UndoLog successfully.",list) ;
    }
}
