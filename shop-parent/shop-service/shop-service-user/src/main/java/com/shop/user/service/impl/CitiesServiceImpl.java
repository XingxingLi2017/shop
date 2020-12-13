package com.shop.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.user.dao.CitiesMapper;
import com.shop.user.pojo.Cities;
import com.shop.user.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CitiesServiceImpl implements CitiesService {

    @Autowired
    private CitiesMapper citiesMapper;


    @Override
    public PageInfo<Cities> findPage(Cities cities, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(cities);
        return new PageInfo<Cities>(citiesMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Cities> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Cities>(citiesMapper.selectAll());
    }

    @Override
    public List<Cities> findList(Cities cities){
        Example example = createExample(cities);
        return citiesMapper.selectByExample(example);
    }

    public Example createExample(Cities cities){
        Example example=new Example(Cities.class);
        Example.Criteria criteria = example.createCriteria();
        if(cities!=null){
            if(!StringUtils.isEmpty(cities.getCityid())){
                    criteria.andEqualTo("cityid",cities.getCityid());
            }
            if(!StringUtils.isEmpty(cities.getCity())){
                    criteria.andEqualTo("city",cities.getCity());
            }
            if(!StringUtils.isEmpty(cities.getProvinceid())){
                    criteria.andEqualTo("provinceid",cities.getProvinceid());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        citiesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Cities cities){
        citiesMapper.updateByPrimaryKeySelective(cities);
    }

    @Override
    public void add(Cities cities){
        citiesMapper.insertSelective(cities);
    }

    @Override
    public Cities findById(String id){
        return  citiesMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Cities> findAll() {
        return citiesMapper.selectAll();
    }
}
