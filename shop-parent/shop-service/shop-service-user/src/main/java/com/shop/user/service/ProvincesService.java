package com.shop.user.service;

import com.github.pagehelper.PageInfo;
import com.shop.user.pojo.Provinces;

import java.util.List;

public interface ProvincesService {

    PageInfo<Provinces> findPage(Provinces provinces, int page, int size);

    PageInfo<Provinces> findPage(int page, int size);

    List<Provinces> findList(Provinces provinces);

    void delete(String id);

    void update(Provinces provinces);

    void add(Provinces provinces);

     Provinces findById(String id);

    List<Provinces> findAll();
}
