package com.shop.goods.service;

import com.github.pagehelper.PageInfo;
import com.shop.goods.pojo.Sku;

import java.util.List;

public interface SkuService {
    /**
     * find sku by status
     */
    List<Sku> findByStatus(String status);

    PageInfo<Sku> findPage(Sku sku, int page, int size);

    PageInfo<Sku> findPage(int page, int size);

    List<Sku> findList(Sku sku);

    void delete(String id);

    void update(Sku sku);

    void add(Sku sku);

    Sku findById(String id);

    List<Sku> findAll();
}
