package com.shop.order.service;

import com.github.pagehelper.PageInfo;
import com.shop.order.pojo.OrderItem;

import java.util.List;

public interface OrderItemService {

    PageInfo<OrderItem> findPage(OrderItem orderItem, int page, int size);

    PageInfo<OrderItem> findPage(int page, int size);

    List<OrderItem> findList(OrderItem orderItem);

    void delete(String id);

    void update(OrderItem orderItem);

    void add(OrderItem orderItem);

     OrderItem findById(String id);

    List<OrderItem> findAll();
}
