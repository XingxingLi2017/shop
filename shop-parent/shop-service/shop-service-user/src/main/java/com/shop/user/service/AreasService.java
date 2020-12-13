package com.shop.user.service;

import com.github.pagehelper.PageInfo;
import com.shop.user.pojo.Areas;

import java.util.List;

public interface AreasService {

    PageInfo<Areas> findPage(Areas areas, int page, int size);

    PageInfo<Areas> findPage(int page, int size);

    List<Areas> findList(Areas areas);

    void delete(String id);

    void update(Areas areas);

    void add(Areas areas);

     Areas findById(String id);

    List<Areas> findAll();
}
