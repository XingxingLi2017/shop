package com.shop.goods.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Brand;
import com.shop.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public Result<List<Brand>> findAll() {
        List<Brand> list = brandService.findAll();
        return new Result<List<Brand>>(true, StatusCode.OK, "Get successfully.", list);
    }

    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable("id") Integer id) {
        Brand brand = brandService.findById(id);
        return new Result(true, StatusCode.OK, "Get successfully.", brand);
    }

    @PostMapping
    public Result add(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result(true, StatusCode.OK, "Add brand successfully.", null);
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Brand brand , @PathVariable("id") Integer id) {
        brand.setId(id);
        brandService.update(brand);
        return new Result(true, StatusCode.OK, "Update brand successfully.");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        brandService.delete(id);
        return new Result(true, StatusCode.OK, "Delete brand successfully.");
    }

    /***
     * search by contidions
     * @param brand
     * @return
     */
    @PostMapping("/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand){
        List<Brand> list = brandService.findList(brand);
        return new Result(true, StatusCode.OK, "Get brands successfully.", list);
    }

    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@PathVariable  Integer page, @PathVariable  Integer size) {
        int q = 10 / 0;
        PageInfo<Brand> pageInfo = brandService.findPage(page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "Get brands successfully.", pageInfo);
    }

    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo> findPage(@RequestBody Brand brand, @PathVariable  Integer page, @PathVariable  Integer size){
        PageInfo<Brand> pageInfo = brandService.findPage(brand, page, size);
        return new Result<PageInfo>(true, StatusCode.OK, "Get brands successfully.", pageInfo);
    }
}
