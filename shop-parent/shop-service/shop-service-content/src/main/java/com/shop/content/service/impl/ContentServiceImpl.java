package com.shop.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.content.dao.ContentMapper;
import com.shop.content.pojo.Content;
import com.shop.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    ContentMapper contentMapper;

    @Override
    public List<Content> findByCategory(Long id) {
        Content content = new Content();
        content.setCategoryId(id);
        content.setStatus("1");
        return contentMapper.select(content);
    }

    @Override
    public PageInfo<Content> findPage(Content content, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(content);
        return new PageInfo<Content>(contentMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Content> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Content>(contentMapper.selectAll());
    }

    @Override
    public List<Content> findList(Content content){
        Example example = createExample(content);
        return contentMapper.selectByExample(example);
    }

    public Example createExample(Content content){
        Example example=new Example(Content.class);
        Example.Criteria criteria = example.createCriteria();
        if(content!=null){
            if(!StringUtils.isEmpty(content.getId())){
                criteria.andEqualTo("id",content.getId());
            }
            if(!StringUtils.isEmpty(content.getCategoryId())){
                criteria.andEqualTo("categoryId",content.getCategoryId());
            }
            if(!StringUtils.isEmpty(content.getTitle())){
                criteria.andLike("title","%"+content.getTitle()+"%");
            }
            if(!StringUtils.isEmpty(content.getUrl())){
                criteria.andEqualTo("url",content.getUrl());
            }
            if(!StringUtils.isEmpty(content.getPic())){
                criteria.andEqualTo("pic",content.getPic());
            }
            if(!StringUtils.isEmpty(content.getStatus())){
                criteria.andEqualTo("status",content.getStatus());
            }
            if(!StringUtils.isEmpty(content.getSortOrder())){
                criteria.andEqualTo("sortOrder",content.getSortOrder());
            }
        }
        return example;
    }

    @Override
    public void delete(Long id){
        contentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Content content){
        contentMapper.updateByPrimaryKeySelective(content);
    }

    @Override
    public void add(Content content){
        contentMapper.insertSelective(content);
    }

    @Override
    public Content findById(Long id){
        return  contentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Content> findAll() {
        return contentMapper.selectAll();
    }
}
