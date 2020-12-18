package com.shop.order.service;

import com.github.pagehelper.PageInfo;
import com.shop.order.pojo.OrderLog;

import java.util.List;

public interface OrderLogService {

    PageInfo<OrderLog> findPage(OrderLog orderLog, int page, int size);

    PageInfo<OrderLog> findPage(int page, int size);

    List<OrderLog> findList(OrderLog orderLog);

    void delete(String id);

    void update(OrderLog orderLog);

    void add(OrderLog orderLog);

     OrderLog findById(String id);

    List<OrderLog> findAll();
}
