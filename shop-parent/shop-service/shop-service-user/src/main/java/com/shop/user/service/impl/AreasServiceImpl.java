package com.shop.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.user.dao.AreasMapper;
import com.shop.user.pojo.Areas;
import com.shop.user.service.AreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AreasServiceImpl implements AreasService {

    @Autowired
    private AreasMapper areasMapper;


    @Override
    public PageInfo<Areas> findPage(Areas areas, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(areas);
        return new PageInfo<Areas>(areasMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Areas> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Areas>(areasMapper.selectAll());
    }

    @Override
    public List<Areas> findList(Areas areas){
        Example example = createExample(areas);
        return areasMapper.selectByExample(example);
    }

    public Example createExample(Areas areas){
        Example example=new Example(Areas.class);
        Example.Criteria criteria = example.createCriteria();
        if(areas!=null){
            if(!StringUtils.isEmpty(areas.getAreaid())){
                    criteria.andEqualTo("areaid",areas.getAreaid());
            }
            if(!StringUtils.isEmpty(areas.getArea())){
                    criteria.andEqualTo("area",areas.getArea());
            }
            if(!StringUtils.isEmpty(areas.getCityid())){
                    criteria.andEqualTo("cityid",areas.getCityid());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        areasMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Areas areas){
        areasMapper.updateByPrimaryKeySelective(areas);
    }

    @Override
    public void add(Areas areas){
        areasMapper.insertSelective(areas);
    }

    @Override
    public Areas findById(String id){
        return  areasMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Areas> findAll() {
        return areasMapper.selectAll();
    }
}
