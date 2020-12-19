package com.shop.user.service;

import com.github.pagehelper.PageInfo;
import com.shop.user.pojo.User;

import java.util.List;

public interface UserService {

    PageInfo<User> findPage(User user, int page, int size);

    PageInfo<User> findPage(int page, int size);

    List<User> findList(User user);

    void delete(String id);

    void update(User user);

    void add(User user);

     User findById(String id);

    List<User> findAll();

    void addPoints(String username, Integer points);
}
