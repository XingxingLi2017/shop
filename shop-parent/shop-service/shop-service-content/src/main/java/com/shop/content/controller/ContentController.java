package com.shop.content.controller;

import com.github.pagehelper.PageInfo;
import com.shop.content.pojo.Content;
import com.shop.content.service.ContentService;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content")
@CrossOrigin
public class ContentController {

    @Autowired
    private ContentService contentService;

    /***
     * get contents by category id
     * @param categoryId
     * @return
     */
    @GetMapping("/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable("id") Long categoryId){
        List<Content> contents = contentService.findByCategory(categoryId);
        return new Result(true, StatusCode.OK, "Get content successfully.", contents);
    }

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Content content, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Content> pageInfo = contentService.findPage(content, page, size);
        return new Result(true,StatusCode.OK,"Get Content successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Content> pageInfo = contentService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get Content successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Content>> findList(@RequestBody(required = false)  Content content){
        List<Content> list = contentService.findList(content);
        return new Result<List<Content>>(true,StatusCode.OK,"Get Content successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Long id){
        contentService.delete(id);
        return new Result(true,StatusCode.OK,"Delete Content successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  Content content,@PathVariable Long id){
        content.setId(id);
        contentService.update(content);
        return new Result(true,StatusCode.OK,"Update Content successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Content content){
        contentService.add(content);
        return new Result(true,StatusCode.OK,"Add Content successfully.");
    }

    @GetMapping("/{id}")
    public Result<Content> findById(@PathVariable Long id){
        Content content = contentService.findById(id);
        return new Result<Content>(true,StatusCode.OK,"Get Content successfully.",content);
    }

    @GetMapping
    public Result<List<Content>> findAll(){
        List<Content> list = contentService.findAll();
        return new Result<List<Content>>(true, StatusCode.OK,"Get Content successfully.",list) ;
    }
}
