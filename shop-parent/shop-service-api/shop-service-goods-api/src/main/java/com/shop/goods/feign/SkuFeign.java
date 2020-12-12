package com.shop.goods.feign;

import com.shop.entity.Result;
import com.shop.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("goods")
@RequestMapping("/sku")
public interface SkuFeign {

    // used for import SKU into Elasticsearch skuinfo index
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable("status") String status);

    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable("id") String id);

}
