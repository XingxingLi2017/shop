package com.shop.goods.service;

import com.github.pagehelper.Page;
import com.shop.goods.pojo.Spec;

import java.util.List;
import java.util.Map;

public interface SpecService {

    List<Spec> findAll();

    Spec findById(Integer id);

    void add(Spec spec);

    void update(Spec spec);

    void delete(Integer id);

    List<Spec> findList(Map<String, Object> searchMap);

    Page<Spec> findPage(int page, int size);

    Page<Spec> findPage(Map<String, Object> searchMap, int page, int size);

    /****
     * get specification list through category name
     * @param categoryName
     * @return
     */
    List<Map> findSpecListByCategoryName(String categoryName);

    List<Spec> findByCategory(Integer categoryId);
}
