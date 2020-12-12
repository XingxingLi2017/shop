package com.shop.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.shop.entity.Result;
import com.shop.goods.feign.CategoryFeign;
import com.shop.goods.feign.SkuFeign;
import com.shop.goods.feign.SpuFeign;
import com.shop.goods.pojo.Category;
import com.shop.goods.pojo.Goods;
import com.shop.goods.pojo.Sku;
import com.shop.goods.pojo.Spu;
import com.shop.item.service.PageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {

    @Value("${pagePath}")
    String pagePath;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    SpuFeign spuFeign;

    @Autowired
    CategoryFeign categoryFeign;

    @Autowired
    SkuFeign skuFeign;

    @Override
    public void generateHtml(String spuId) {
        // get context to store data
        Context context = new Context();

        // get spu + sku list data from goods service
        Map<String, Object> itemData = getItemData(spuId);
        context.setVariables(itemData);

        File dir = new File(pagePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        String path = dir.toURI().getPath() + File.separator + spuId + ".html";
        File file = new File(path);

        try(Writer out = new PrintWriter(file)) {
            templateEngine.process("item", context, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteHtml(String spuId) {
        File dir = new File(pagePath);
        String path = dir.toURI().getPath() + File.separator + spuId + ".html";
        File file = new File(path);
        if(file.exists()) {
            file.delete();
        }
    }

    private Map<String, Object> getItemData(String spuId) {
        Map<String, Object> resultMap = new HashMap<>();
        Result result = spuFeign.findGoodsById(spuId);
        Goods goods = JSON.parseObject(JSON.toJSONString(result.getData()), Goods.class);
        Spu spu = goods.getSpu();
        List<Sku> skuList = goods.getSkuList();

        resultMap.put("spu", spu);
        if(spu != null) {
            if(StringUtils.isNotEmpty(spu.getImages())) {
                resultMap.put("imageList", spu.getImages().split(","));
            }
        }

        Category category1 = JSON.parseObject(JSON.toJSONString(categoryFeign.findById(spu.getCategory1Id()).getData()), Category.class);
        Category category2 = JSON.parseObject(JSON.toJSONString(categoryFeign.findById(spu.getCategory2Id()).getData()), Category.class);
        Category category3 = JSON.parseObject(JSON.toJSONString(categoryFeign.findById(spu.getCategory3Id()).getData()), Category.class);

        resultMap.put("category1", category1);
        resultMap.put("category2", category2);
        resultMap.put("category3", category3);

        resultMap.put("skuList", skuList);

        resultMap.put("specificationList", JSON.parseObject(spu.getSpecItems(), Map.class));
        return resultMap;
    }

}
