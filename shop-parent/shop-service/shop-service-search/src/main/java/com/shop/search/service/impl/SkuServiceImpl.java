package com.shop.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.shop.entity.Result;
import com.shop.goods.feign.SkuFeign;
import com.shop.goods.pojo.Sku;
import com.shop.search.dao.SkuEsMapper;
import com.shop.search.pojo.SkuInfo;
import com.shop.search.service.SkuService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuFeign skuFeign;

    @Autowired
    SkuEsMapper skuEsMapper;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void importData() {

        Result<List<Sku>> skuResult = skuFeign.findByStatus("1");

        // parse Sku to  SkuInfo through prop names
        List<SkuInfo> skuInfoList = new ArrayList<>();
        for(Sku e : skuResult.getData()) {
            SkuInfo skuInfo = JSON.parseObject(JSON.toJSONString(e), SkuInfo.class);
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
            skuInfoList.add(skuInfo);
        }
        //List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuResult.getData()), SkuInfo.class);

        skuEsMapper.saveAll(skuInfoList);
    }


    /***
     * search by conditions
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        // build query by keywords
        if(searchMap != null && searchMap.size() > 0) {
            String keywords = (String) searchMap.get("keywords");

            if(!StringUtils.isEmpty(keywords)) {
                builder.withQuery(
                        QueryBuilders.queryStringQuery(keywords)
                                .field("name")
                );
            }
        }

        // add an aggregation operation
        builder.addAggregation(AggregationBuilders.terms("skuCategoryGroup").field("categoryName"));

        // execute query
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

        // get aggregation result info
        StringTerms stringTerms = (StringTerms) page.getAggregation("skuCategoryGroup");
        List<String> categoryList = getStringsCategoryList(stringTerms);

        List<SkuInfo> contents = page.getContent();
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        Map<String, Object> ret = new HashMap<>();
        ret.put("rows", contents);
        ret.put("total", totalElements);
        ret.put("totalPages", totalPages);
        ret.put("categoryList" , categoryList);

        return ret;
    }

    private List<String> getStringsCategoryList(StringTerms stringTerms) {
        List<String> categoryList = new ArrayList<>();
        if (stringTerms != null) {
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();
                categoryList.add(keyAsString);
            }
        }
        return categoryList;
    }
}
