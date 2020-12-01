package com.shop.goods.service;

import com.github.pagehelper.Page;
import com.shop.goods.pojo.Template;

import java.util.List;
import java.util.Map;

public interface TemplateService {

    List<Template> findAll();


    Template findById(Integer id);

    void add(Template template);

    void update(Template template);


    void delete(Integer id);

    /***
     * search with conditions
     * @param searchMap
     * @return
     */
    List<Template> findList(Map<String, Object> searchMap);

    /***
     * pagination
     * @param page
     * @param size
     * @return
     */
    Page<Template> findPage(int page, int size);

    /***
     * pagination with conditions
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Template> findPage(Map<String, Object> searchMap, int page, int size);
}
