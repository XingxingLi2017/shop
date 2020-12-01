package com.shop.goods.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shop.goods.dao.TemplateMapper;
import com.shop.goods.pojo.Template;
import com.shop.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    TemplateMapper templateMapper;

    @Override
    public List<Template> findAll() {
        return templateMapper.selectAll();
    }

    @Override
    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Template template) {
        templateMapper.insertSelective(template);
    }

    @Override
    public void update(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public void delete(Integer id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Template> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return templateMapper.selectByExample(example);
    }

    @Override
    public Page<Template> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Template>) templateMapper.selectAll();
    }

    @Override
    public Page<Template> findPage(Map<String, Object> searchMap, int page, int size) {
        Example example = createExample(searchMap);
        PageHelper.startPage(page, size);
        return (Page<Template>) templateMapper.selectByExample(example);
    }

    private Example createExample(Map<String, Object> map ) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if(map != null) {
            if(map.get("name") != null && !"".equals(map.get("name"))) {
                criteria.andLike("name" , "%"+map.get("name")+"%");
            }
            if(map.get("id") != null) {
                criteria.andEqualTo("id" , map.get("id"));
            }
            if(map.get("specNum")!=null ){
                criteria.andEqualTo("specNum",map.get("specNum"));
            }
            if(map.get("paraNum")!=null ){
                criteria.andEqualTo("paraNum",map.get("paraNum"));
            }
        }
        return example;
    }
}
