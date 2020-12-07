package com.shop.content.service;

import com.github.pagehelper.PageInfo;
import com.shop.content.pojo.Content;

import java.util.List;

public interface ContentService {
    List<Content> findByCategory(Long cid);
    PageInfo<Content> findPage(Content content, int page, int size);

    PageInfo<Content> findPage(int page, int size);

    List<Content> findList(Content content);

    void delete(Long id);

    void update(Content content);

    void add(Content content);

    Content findById(Long id);

    List<Content> findAll();
}
