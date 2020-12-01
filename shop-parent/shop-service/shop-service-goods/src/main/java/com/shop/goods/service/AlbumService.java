package com.shop.goods.service;

import com.github.pagehelper.Page;
import com.shop.goods.pojo.Album;

import java.util.List;
import java.util.Map;

public interface AlbumService {
    List<Album> findAll();
    Album findById(Long id);
    void add(Album album);
    void update(Album album);
    void delete(Long id);

    /****
     * search through multiple conditions, use condition map
     */
    List<Album> findList(Map<String, Object> searchMap);

    /****
     * pagination
     */
    Page<Album> findPage(Integer page, Integer size);

    /****
     * pagination with conditions
     */
    Page<Album> findPage(Map<String, Object> searchMap, Integer page, Integer size);
}
