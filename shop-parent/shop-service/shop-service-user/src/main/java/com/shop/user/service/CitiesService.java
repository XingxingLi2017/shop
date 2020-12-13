package com.shop.user.service;

import com.github.pagehelper.PageInfo;
import com.shop.user.pojo.Cities;

import java.util.List;

public interface CitiesService {

    PageInfo<Cities> findPage(Cities cities, int page, int size);

    PageInfo<Cities> findPage(int page, int size);

    List<Cities> findList(Cities cities);

    void delete(String id);

    void update(Cities cities);

    void add(Cities cities);

     Cities findById(String id);

    List<Cities> findAll();
}
