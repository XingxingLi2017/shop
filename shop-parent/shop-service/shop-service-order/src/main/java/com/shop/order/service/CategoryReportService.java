package com.shop.order.service;

import com.github.pagehelper.PageInfo;
import com.shop.order.pojo.CategoryReport;

import java.util.Date;
import java.util.List;

public interface CategoryReportService {

    PageInfo<CategoryReport> findPage(CategoryReport categoryReport, int page, int size);

    PageInfo<CategoryReport> findPage(int page, int size);

    List<CategoryReport> findList(CategoryReport categoryReport);

    void delete(Date id);

    void update(CategoryReport categoryReport);

    void add(CategoryReport categoryReport);

     CategoryReport findById(Date id);

    List<CategoryReport> findAll();
}
