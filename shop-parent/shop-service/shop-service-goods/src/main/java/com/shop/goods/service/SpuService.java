package com.shop.goods.service;

import com.github.pagehelper.PageInfo;
import com.shop.goods.pojo.Goods;
import com.shop.goods.pojo.Spu;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SpuService {

    void realDel(String id);

    void restore(String id);

    void putMany(Long[] spuIds);

    void put(Long spuId);

    void pull(Long spuId); // off the shelf
    void audit(Long spuId);

    Goods findGoodsById(Long id);

    /***
     * sku + spu = goods info
     * @param goods
     */
    void saveGoods(Goods goods);

    PageInfo<Spu> findPage(Spu spu, int page, int size);

    PageInfo<Spu> findPage(int page, int size);

    List<Spu> findList(Spu spu);

    void delete(String id);

    void update(Spu spu);

    void add(Spu spu);

    Spu findById(String id);

    List<Spu> findAll();
}
