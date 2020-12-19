package com.shop.search.controller;

import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuController {

    @Autowired
    SkuService skuService;

    /***
     * import skuInfo indices from goods.tb_sku table
     * @return
     */
    @GetMapping("/import")
    public Result importData(){
        skuService.importData();
        return new Result(true, StatusCode.OK, "Import successfully.");
    }

    /***
     * search in skuInfo index by searchMap
     * @param searchMap
     * @return
     */
    @GetMapping
    public Map search(@RequestParam(required = false) Map<String, Object> searchMap){
        return (Map) skuService.search(searchMap);
    }
}
