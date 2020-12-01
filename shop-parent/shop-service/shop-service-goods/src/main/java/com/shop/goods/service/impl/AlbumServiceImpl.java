package com.shop.goods.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shop.goods.dao.AlbumMapper;
import com.shop.goods.pojo.Album;
import com.shop.goods.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumMapper albumMapper;

    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }

    @Override
    public Album findById(Long id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Album album) {
        albumMapper.insert(album);
    }

    @Override
    public void update(Album album) {
        albumMapper.updateByPrimaryKeySelective(album);
    }

    @Override
    public void delete(Long id) {
        albumMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Album> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return albumMapper.selectByExample(example);
    }

    @Override
    public Page<Album> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Page<Album> list = (Page<Album>) albumMapper.selectAll();
        return list;
    }

    @Override
    public Page<Album> findPage(Map<String, Object> searchMap, Integer page, Integer size) {
        Example example = createExample( searchMap );
        PageHelper.startPage(page, size);
        Page<Album> list = (Page<Album>) albumMapper.selectByExample(example);
        return list;
    }

    private Example createExample(Map<String, Object> map) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if(map != null) {
            if(map.get("title") != null && !"".equals(map.get("title"))) {
                criteria.andLike("title", "%"+map.get("title")+"%");
            }
            if(map.get("image") != null && !"".equals(map.get("image"))) {
                criteria.andLike("image", "%"+map.get("image")+"%");
            }
            if(map.get("image_items") != null && !"".equals(map.get("image_items"))) {
                criteria.andLike("image_items", "%"+map.get("image_items")+"%");
            }
        }
        return example;
    }
}
