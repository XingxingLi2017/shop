package com.shop.order.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.CategoryReport;
import com.shop.order.service.CategoryReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/categoryReport")
@CrossOrigin
public class CategoryReportController {

    @Autowired
    private CategoryReportService categoryReportService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  CategoryReport categoryReport, @PathVariable  int page, @PathVariable  int size){
        PageInfo<CategoryReport> pageInfo = categoryReportService.findPage(categoryReport, page, size);
        return new Result(true,StatusCode.OK,"Get CategoryReport successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<CategoryReport> pageInfo = categoryReportService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get CategoryReport successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<CategoryReport>> findList(@RequestBody(required = false)  CategoryReport categoryReport){
        List<CategoryReport> list = categoryReportService.findList(categoryReport);
        return new Result<List<CategoryReport>>(true,StatusCode.OK,"Get CategoryReport successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Date id){
        categoryReportService.delete(id);
        return new Result(true,StatusCode.OK,"Delete CategoryReport successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  CategoryReport categoryReport,@PathVariable Date id){
        categoryReport.setCountDate(id);
        categoryReportService.update(categoryReport);
        return new Result(true,StatusCode.OK,"Update CategoryReport successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   CategoryReport categoryReport){
        //调用CategoryReportService实现添加CategoryReport
        categoryReportService.add(categoryReport);
        return new Result(true,StatusCode.OK,"Add CategoryReport successfully.");
    }

    @GetMapping("/{id}")
    public Result<CategoryReport> findById(@PathVariable Date id){
        CategoryReport categoryReport = categoryReportService.findById(id);
        return new Result<CategoryReport>(true,StatusCode.OK,"Get CategoryReport successfully.",categoryReport);
    }

    @GetMapping
    public Result<List<CategoryReport>> findAll(){
        List<CategoryReport> list = categoryReportService.findAll();
        return new Result<List<CategoryReport>>(true, StatusCode.OK,"Get CategoryReport successfully.",list) ;
    }
}
