package com.shop.content.feign;

import com.shop.content.pojo.Content;
import com.shop.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("content")
@RequestMapping("/content")
public interface ContentFeign {

    @GetMapping("/list/category/{id}")
    Result<List<Content>> findByCategory(@PathVariable("id") Long categoryId);
}
