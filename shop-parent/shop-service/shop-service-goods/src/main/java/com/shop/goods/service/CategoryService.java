package com.shop.goods.service;

import com.github.pagehelper.Page;
import com.shop.goods.pojo.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Integer id);
    void add(Category category);
    void update(Category category);
    void delete(Integer id);

    /****
     * search by conditions
     * @param searchMap
     * @return
     */
    List<Category> findList(Map<String, Object> searchMap);

    /****
     * pagination
     * @param page
     * @param size
     * @return
     */
    Page<Category> findPage(int page, int size);

    /****
     * pagination with conditions
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Category> findPage(Map<String, Object> searchMap, int page, int size);
}
