package com.shop.goods.service;

import com.github.pagehelper.PageInfo;
import com.shop.goods.pojo.Brand;

import java.util.List;

public interface BrandService {

    List<Brand> findAll();
    Brand findById(Integer id);
    void add(Brand brand);
    void update(Brand brand);
    void delete(Integer id);

    /****
     * find brands by conditions
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    /****
     * find brands by pages
     * @param page current page
     * @param size page size
     * @return
     */
    PageInfo<Brand> findPage(int page, int size);

    /****
     * get brands by pages based on given conditions
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(Brand brand, int page, int size);

}
