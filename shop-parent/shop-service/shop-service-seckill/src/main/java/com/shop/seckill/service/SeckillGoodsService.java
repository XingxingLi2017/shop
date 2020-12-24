package com.shop.seckill.service;

import com.github.pagehelper.PageInfo;
import com.shop.seckill.pojo.SeckillGoods;

import java.util.List;

public interface SeckillGoodsService {

    PageInfo<SeckillGoods> findPage(SeckillGoods seckillGoods, int page, int size);

    PageInfo<SeckillGoods> findPage(int page, int size);

    List<SeckillGoods> findList(SeckillGoods seckillGoods);

    void delete(Long id);

    void update(SeckillGoods seckillGoods);

    void add(SeckillGoods seckillGoods);

     SeckillGoods findById(Long id);

    List<SeckillGoods> findAll();
}
