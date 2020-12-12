package com.shop.goods.feign;

import com.shop.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("goods")
@RequestMapping("/category")
public interface CategoryFeign {
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Integer id);
}
