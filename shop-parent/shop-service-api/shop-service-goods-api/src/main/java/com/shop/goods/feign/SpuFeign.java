package com.shop.goods.feign;

import com.shop.entity.Result;
import com.shop.goods.pojo.Goods;
import com.shop.goods.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("goods")
@RequestMapping("/spu")
public interface SpuFeign {

    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable("id") String id);

    /***
     * get goods( sku list + spu ) by spu id
     */
    @GetMapping("/goods/{id}")
    public Result<Goods> findGoodsById(@PathVariable("id") String spuId);
}
