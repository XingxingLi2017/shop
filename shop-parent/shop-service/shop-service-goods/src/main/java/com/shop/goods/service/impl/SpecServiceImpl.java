package com.shop.goods.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shop.goods.dao.SpecMapper;
import com.shop.goods.pojo.Spec;
import com.shop.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class SpecServiceImpl implements SpecService {

    @Autowired
    SpecMapper specMapper;

    @Override
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    @Override
    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Spec spec) {
        specMapper.insertSelective(spec);
    }

    @Override
    public void update(Spec spec) {
        specMapper.updateByPrimaryKeySelective(spec);
    }

    @Override
    public void delete(Integer id) {
        specMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Spec> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return specMapper.selectByExample(example);
    }

    @Override
    public Page<Spec> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        return (Page<Spec>)specMapper.selectAll();
    }

    @Override
    public Page<Spec> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Spec>)specMapper.selectByExample(example);
    }

    @Override
    public List<Map> findSpecListByCategoryName(String categoryName) {
        List<Map> list = specMapper.findSpecListByCategoryName(categoryName);
        for(Map spec : list) {
            String[] options = spec.get("options").toString().split(",");
            // change options prop from string to string array
            spec.put("options", options);
        }
        return list;
    }

    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            if(searchMap.get("name") != null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            if(searchMap.get("options")!=null && !"".equals(searchMap.get("options"))){
                criteria.andLike("options","%"+searchMap.get("options")+"%");
            }
            if(searchMap.get("id") != null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }
            if(searchMap.get("seq") != null ){
                criteria.andEqualTo("seq",searchMap.get("seq"));
            }
            if(searchMap.get("templateId") != null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }
        }
        return example;
    }
}
