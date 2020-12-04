package com.shop.goods.service;

import com.github.pagehelper.PageInfo;
import com.shop.goods.pojo.Goods;
import com.shop.goods.pojo.Spu;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SpuService {

    void update(Goods goods);

    void realDelete(String id);

    void restore(String id);

    void putMany(String[] spuIds);

    void put(String spuId);

    void pull(String spuId); // off the shelf
    void audit(String spuId);

    Goods findGoodsById(String id);

    /***
     * sku + spu = goods info
     * @param goods
     */
    void saveGoods(Goods goods);
    void add(Goods goods);

    PageInfo<Spu> findPage(Spu spu, int page, int size);

    PageInfo<Spu> findPage(int page, int size);

    List<Spu> findList(Spu spu);

    void delete(String id);

    void update(Spu spu);

    void add(Spu spu);

    Spu findById(String id);

    List<Spu> findAll();
}
