package com.shop.order.service;

import com.github.pagehelper.PageInfo;
import com.shop.order.pojo.Order;

import java.util.List;

public interface OrderService {

    PageInfo<Order> findPage(Order order, int page, int size);

    PageInfo<Order> findPage(int page, int size);

    List<Order> findList(Order order);

    void delete(String id);

    void update(Order order);

    void add(Order order);
    void add(Order order, String[] skuIds);

    Order findById(String id);

    List<Order> findAll();

}
