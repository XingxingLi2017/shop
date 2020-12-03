package com.shop.goods.service;

import com.github.pagehelper.PageInfo;
import com.shop.goods.pojo.Pref;

import java.util.List;

public interface PrefService {

    PageInfo<Pref> findPage(Pref pref, int page, int size);

    PageInfo<Pref> findPage(int page, int size);

    List<Pref> findList(Pref pref);

    void delete(Integer id);

    void update(Pref pref);

    void add(Pref pref);

     Pref findById(Integer id);

    List<Pref> findAll();
}
