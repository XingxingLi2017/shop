package com.shop.seckill.service;

import com.github.pagehelper.PageInfo;
import com.shop.entity.SeckillStatus;
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

    /***
     * place seckill order
     * @param time
     * @param id
     * @param username
     */
    Boolean add(String time, Long id, String username);

    /***
     * get user order's status in the queue
     * @param username
     * @return
     */
    SeckillStatus queryStatus(String username);
}
