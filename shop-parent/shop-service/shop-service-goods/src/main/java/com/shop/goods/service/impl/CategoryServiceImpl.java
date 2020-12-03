package com.shop.goods.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shop.goods.dao.CategoryMapper;
import com.shop.goods.pojo.Category;
import com.shop.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Category category) {
        categoryMapper.insertSelective(category);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public void delete(Integer id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Category> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return categoryMapper.selectByExample(example);
    }

    @Override
    public Page<Category> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Category>)categoryMapper.selectAll();
    }

    @Override
    public Page<Category> findPage(Map<String, Object> searchMap, int page, int size) {
        Example example = createExample(searchMap);
        PageHelper.startPage(page, size);
        return (Page<Category>) categoryMapper.selectByExample(example);
    }

    @Override
    public List<Category> findByParentId(Integer id) {
        Category c = new Category();
        c.setParentId(id);
        return categoryMapper.select(c);
    }

    private Example createExample(Map<String, Object> map) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if(map != null) {
            if(map.get("name") != null && !"".equals(map.get("name"))) {
                criteria.andLike("name", "%"+map.get("name")+"%");
            }
            if(map.get("isShow") != null && !"".equals(map.get("isShow"))) {
                criteria.andEqualTo("isShow", map.get("isShow"));
            }
            if(map.get("isMenu") != null && !"".equals(map.get("isMenu"))) {
                criteria.andLike("isMenu", "%"+map.get("isMenu")+"%");
            }
            if(map.get("id") != null) {
                criteria.andEqualTo("id", map.get("id"));
            }
            if(map.get("goodsNum")!=null ){
                criteria.andEqualTo("goodsNum",map.get("goodsNum"));
            }
            if(map.get("seq")!=null ){
                criteria.andEqualTo("seq",map.get("seq"));
            }
            if(map.get("parentId")!=null ){
                criteria.andEqualTo("parentId",map.get("parentId"));
            }
            if(map.get("templateId")!=null ){
                criteria.andEqualTo("templateId",map.get("templateId"));
            }
        }
        return example;
    }
}
