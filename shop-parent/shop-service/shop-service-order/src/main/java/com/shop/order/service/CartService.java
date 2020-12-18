package com.shop.order.service;

import com.shop.order.pojo.OrderItem;

import java.util.List;

public interface CartService {

    /***
     * add order item to cart
     * @param num
     * @param id
     */
    void add(Integer num, Long id , String username);

    /***
     * list order items in user's cart
     * @param username
     * @return
     */
    List<OrderItem> list(String username);
}
