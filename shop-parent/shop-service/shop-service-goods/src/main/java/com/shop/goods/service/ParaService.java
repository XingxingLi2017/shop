package com.shop.goods.service;

import com.github.pagehelper.PageInfo;
import com.shop.goods.pojo.Para;

import java.util.List;

public interface ParaService {

    PageInfo<Para> findPage(Para para, int page, int size);

    PageInfo<Para> findPage(int page, int size);

    List<Para> findList(Para para);

    void delete(Integer id);

    void update(Para para);

    void add(Para para);

     Para findById(Integer id);

    List<Para> findAll();

    List<Para> findByCategory(Integer categoryId);
}
