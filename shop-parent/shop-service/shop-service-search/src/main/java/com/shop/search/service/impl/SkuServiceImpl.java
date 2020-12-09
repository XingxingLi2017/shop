package com.shop.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.shop.entity.Result;
import com.shop.goods.feign.SkuFeign;
import com.shop.goods.pojo.Sku;
import com.shop.search.dao.SkuEsMapper;
import com.shop.search.pojo.SkuInfo;
import com.shop.search.service.SkuService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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
     * search by conditions , aggregating category, brand , specs
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {

        // add filters
        NativeSearchQueryBuilder builder = getNativeSearchQueryBuilder(searchMap);

        // add aggregations
        addAggregations(searchMap, builder);

        // execute query
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);

        // get aggregation result info
        List<String> categoryList = getStringsCategoryList((StringTerms) page.getAggregation("skuCategoryGroup"));
        List<String> brandList = getStringsCategoryList((StringTerms)page.getAggregation("skuBrandGroup"));
        Map<String, Set<String>> specMap = getSpecMap((StringTerms) page.getAggregation("skuSpecGroup"));

        List<SkuInfo> contents = page.getContent();
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();

        Map<String, Object> ret = new HashMap<>();
        ret.put("rows", contents);
        ret.put("total", totalElements);
        ret.put("totalPages", totalPages);
        ret.put("categoryList" , categoryList);
        ret.put("brandList", brandList);
        ret.put("specMap" , specMap);

        return ret;
    }

    private void addAggregations(Map<String, Object> searchMap, NativeSearchQueryBuilder builder) {

        if(searchMap == null || StringUtils.isEmpty(searchMap.get("category"))) {
            // add an aggregation operation , query categories containing keywords
            builder.addAggregation(AggregationBuilders.terms("skuCategoryGroup").field("categoryName").size(50));
        }

        if(searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))) {
            // get brand name list containing keywords
            builder.addAggregation(AggregationBuilders.terms("skuBrandGroup").field("brandName").size(50));
        }

        // aggregate with specs , spec.keyword is used to forbid split words
        builder.addAggregation(AggregationBuilders.terms("skuSpecGroup").field("spec.keyword").size(100));

    }

    private NativeSearchQueryBuilder getNativeSearchQueryBuilder(Map<String, Object> searchMap) {

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // build combined query
        if(searchMap != null && searchMap.size() > 0) {

            // keywords filter
            if(!StringUtils.isEmpty(searchMap.get("keywords"))) {
                //builder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));
                boolQueryBuilder.must(QueryBuilders.queryStringQuery((String) searchMap.get("keywords")).field("name"));
            }
            if(!StringUtils.isEmpty(searchMap.get("category"))) {
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName", searchMap.get("category")));
            }
            if(!StringUtils.isEmpty(searchMap.get("brand"))) {
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
            }

            // specs' map filter
            for(Map.Entry<String, Object> entry : searchMap.entrySet()) {
                if(entry.getKey().startsWith("spec_")) {
                    boolQueryBuilder.must(QueryBuilders.termQuery("specMap."+entry.getKey().substring(5)+".keyword", entry.getValue()));
                }
            }

            // price filter
            String price = (String) searchMap.get("price");
            if(!StringUtils.isEmpty(price)) {
                price = price.replace("元" , "").replace("以上", "");
                String[] prices = price.split("-");
                if(prices != null && prices.length > 0) {
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price")
                            .gt(Integer.parseInt(prices[0])));
                    if(prices.length > 1) {
                        boolQueryBuilder.must(QueryBuilders.rangeQuery("price")
                                .lte(Integer.parseInt(prices[1])));
                    }
                }
            }
        }
        builder.withQuery(boolQueryBuilder);
        return builder;
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

    private Map<String, Set<String>> getSpecMap(StringTerms stringTerms) {
        Map<String , Set<String>> specMap = new HashMap<>();
        if(stringTerms != null) {
            for(StringTerms.Bucket bucket : stringTerms.getBuckets()) {

                // bucket : {key1:value1,key2:value2,key3:value3}
                String keyAsString = bucket.getKeyAsString();
                Map<String, String> map = JSON.parseObject(keyAsString, Map.class);

                for(Map.Entry<String, String> entry : map.entrySet()) {

                    // use set to remove duplicates
                    Set<String> set = specMap.get(entry.getKey());
                    if(set == null) {
                        set = new HashSet<>();
                    }
                    set.add(map.get(entry.getKey()));
                    specMap.put(entry.getKey(), set);
                }
            }
        }
        return specMap;
    }
}
