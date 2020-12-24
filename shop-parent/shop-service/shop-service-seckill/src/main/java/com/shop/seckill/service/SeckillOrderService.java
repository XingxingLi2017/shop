package com.shop.seckill.service;

import com.github.pagehelper.PageInfo;
import com.shop.seckill.pojo.SeckillOrder;

import java.util.List;

public interface SeckillOrderService {

    PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size);

    PageInfo<SeckillOrder> findPage(int page, int size);

    List<SeckillOrder> findList(SeckillOrder seckillOrder);

    void delete(Long id);

    void update(SeckillOrder seckillOrder);

    void add(SeckillOrder seckillOrder);

     SeckillOrder findById(Long id);

    List<SeckillOrder> findAll();
}
