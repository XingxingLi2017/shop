package com.shop.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.goods.dao.CategoryMapper;
import com.shop.goods.dao.ParaMapper;
import com.shop.goods.pojo.Category;
import com.shop.goods.pojo.Para;
import com.shop.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParaServiceImpl implements ParaService {

    @Autowired
    private ParaMapper paraMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public PageInfo<Para> findPage(Para para, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(para);
        return new PageInfo<Para>(paraMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Para> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Para>(paraMapper.selectAll());
    }

    @Override
    public List<Para> findList(Para para){
        Example example = createExample(para);
        return paraMapper.selectByExample(example);
    }

    public Example createExample(Para para){
        Example example=new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if(para!=null){
            if(!StringUtils.isEmpty(para.getId())){
                    criteria.andEqualTo("id",para.getId());
            }
            if(!StringUtils.isEmpty(para.getName())){
                    criteria.andLike("name","%"+para.getName()+"%");
            }
            if(!StringUtils.isEmpty(para.getOptions())){
                    criteria.andEqualTo("options",para.getOptions());
            }
            if(!StringUtils.isEmpty(para.getSeq())){
                    criteria.andEqualTo("seq",para.getSeq());
            }
            if(!StringUtils.isEmpty(para.getTemplateId())){
                    criteria.andEqualTo("templateId",para.getTemplateId());
            }
        }
        return example;
    }

    @Override
    public void delete(Integer id){
        paraMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Para para){
        paraMapper.updateByPrimaryKeySelective(para);
    }

    @Override
    public void add(Para para){
        paraMapper.insertSelective(para);
    }

    @Override
    public Para findById(Integer id){
        return  paraMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }


    @Override
    public List<Para> findByCategory(Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category == null) {
            return new ArrayList<>();
        }
        Para para = new Para();
        para.setTemplateId(category.getTemplateId());
        return paraMapper.select(para);
    }
}
