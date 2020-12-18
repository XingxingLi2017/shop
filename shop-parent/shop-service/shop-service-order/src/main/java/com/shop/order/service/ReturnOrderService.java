package com.shop.order.service;

import com.github.pagehelper.PageInfo;
import com.shop.order.pojo.ReturnOrder;

import java.util.List;

public interface ReturnOrderService {

    PageInfo<ReturnOrder> findPage(ReturnOrder returnOrder, int page, int size);

    PageInfo<ReturnOrder> findPage(int page, int size);

    List<ReturnOrder> findList(ReturnOrder returnOrder);

    void delete(Long id);

    void update(ReturnOrder returnOrder);

    void add(ReturnOrder returnOrder);

     ReturnOrder findById(Long id);

    List<ReturnOrder> findAll();
}
