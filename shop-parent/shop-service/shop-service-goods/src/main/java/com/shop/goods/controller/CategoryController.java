package com.shop.goods.controller;

import com.github.pagehelper.Page;
import com.shop.entity.PageResult;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Category;
import com.shop.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result findAll(){
        List<Category> list = categoryService.findAll();
        return new Result(true, StatusCode.OK, "Get categories successfully.", list);
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        Category category = categoryService.findById(id);
        return new Result(true, StatusCode.OK, "Get category successfully.", category);
    }

    @PostMapping
    public Result add(@RequestBody Category category) {
        categoryService.add(category);
        return new Result(true, StatusCode.OK, "Add category successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Category category , @PathVariable Integer id){
        category.setId(id);
        categoryService.update(category);
        return new Result(true, StatusCode.OK, "Update category successfully.");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return new Result(true, StatusCode.OK, "Delete category successfully.");
    }

    /****
     * search category by conditions
     */
    @GetMapping("/search")
    public Result findList(@RequestParam Map searchMap) {
        List<Category> list = categoryService.findList(searchMap);
        return new Result(true, StatusCode.OK, "Get categories successfully.", list);
    }

    /***
     * pagination with conditions
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Category> pageList = categoryService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"Get categories successfully.", pageResult);
    }

    /****
     * get categories through categories' parent id
     */
    @GetMapping("/list/{pid}")
    public Result<List<Category>> findByParentId(@PathVariable Integer pid){
        List<Category> list = categoryService.findByParentId(pid);
        return new Result(true, StatusCode.OK, "Get categories successfully." , list);
    }
}
