package com.shop.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.shop.entity.Result;
import com.shop.goods.feign.SkuFeign;
import com.shop.goods.pojo.Sku;
import com.shop.search.dao.SkuEsMapper;
import com.shop.search.pojo.SkuInfo;
import com.shop.search.service.SkuService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
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

        // delete old index
        elasticsearchTemplate.deleteIndex(SkuInfo.class);
        elasticsearchTemplate.createIndex(SkuInfo.class);
        elasticsearchTemplate.putMapping(SkuInfo.class);

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

        // highlight field
        builder.withHighlightFields(new HighlightBuilder.Field("name"));
        // highlight pre tag and post tag
        builder.withHighlightBuilder(new HighlightBuilder().preTags("<em style='color:red'>").postTags("</em>").fragmentSize(100));

        // execute query , customized search result mapping
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(
                builder.build(),
                SkuInfo.class,
                new SearchResultMapper() {      // encapsulate search hits into SkuInfo.class
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        SearchHits hits = searchResponse.getHits();
                        List<T> list = new ArrayList<>();
                        for(SearchHit hit : hits) {
                            // get contents without highlight
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(),SkuInfo.class);

                            // get highlight field fragments
                            HighlightField highlightField = hit.getHighlightFields().get("name");
                            if(highlightField != null && highlightField.getFragments() != null) {
                                Text[] fragments = highlightField.getFragments();
                                StringBuilder sb = new StringBuilder();
                                for(Text fragment : fragments) {
                                    sb.append(fragment.toString());
                                }

                                // replace name field string with highlight string
                                skuInfo.setName(sb.toString());
                            }
                            list.add((T) skuInfo);
                        }
                        return new AggregatedPageImpl<T>(list, pageable, searchResponse.getHits().getTotalHits(), searchResponse.getAggregations());
                    }
                }
        );

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

        NativeSearchQuery query = builder.build();
        Pageable pageable = query.getPageable();
        int pageSize = pageable.getPageSize();
        int pageNum = pageable.getPageNumber() + 1;
        ret.put("pageNum",pageNum);
        ret.put("pageSize",pageSize);

        return ret;
    }

    /***
     * Aggregations == GROUP BY clause
     * ES query can have multiple groups in result set
     * @param searchMap
     * @param builder
     */
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

            // keywords matching
            if(!StringUtils.isEmpty(searchMap.get("keywords"))) {
                //builder.withQuery(QueryBuilders.queryStringQuery(keywords).field("name"));

                // must clause == AND , should == OR
                boolQueryBuilder.must(QueryBuilders.queryStringQuery((String) searchMap.get("keywords")).field("name"));
            }
            // category field matching
            if(!StringUtils.isEmpty(searchMap.get("category"))) {
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName", searchMap.get("category")));
            }

            // brand field matching
            if(!StringUtils.isEmpty(searchMap.get("brand"))) {
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
            }

            // specs' map filter
            for(Map.Entry<String, Object> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                if(key.startsWith("spec_")) {
                    value = value.replace("\\", "");        // process '\' (3G\4G) sign
                    boolQueryBuilder.must(QueryBuilders.termQuery("specMap."+entry.getKey().substring(5)+".keyword", value));
                }
            }

            // price filter
            String price = (String) searchMap.get("price");
            if(!StringUtils.isEmpty(price)) {
                price = price.replace("元" , "").replace("以上", "");
                String[] prices = price.split("-");
                if(prices.length > 0) {
                    boolQueryBuilder
                            .must(QueryBuilders.rangeQuery("price")
                            .gt(Integer.parseInt(prices[0])));
                    if(prices.length > 1) {
                        try {
                            int param2 = Integer.parseInt(prices[1]);
                            boolQueryBuilder
                                    .must(QueryBuilders.rangeQuery("price")
                                    .lte(param2));
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }

        // sort result
        String sortField = (String) searchMap.get("sortField");
        String sortRule = (String) searchMap.get("sortRule");
        if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
            // withSort == Order by
            builder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule.toUpperCase())));
        }

        // pagination
        int pageNum = converterPage(searchMap);
        int size = 30;
        builder.withPageable(PageRequest.of(pageNum - 1 , size));

        builder.withQuery(boolQueryBuilder);
        return builder;
    }

    private Integer converterPage(Map<String, Object> searchMap) {
        if(searchMap != null) {
            String pageNum = (String) searchMap.get("pageNum");
            try {
                return Integer.parseInt(pageNum);
            } catch (NumberFormatException e) {

            }
        }
        return 1;
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

                // bucket : {key1:value1, key2:value2, key3:value3}
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
