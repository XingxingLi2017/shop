package com.shop.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.user.dao.ProvincesMapper;
import com.shop.user.pojo.Provinces;
import com.shop.user.service.ProvincesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ProvincesServiceImpl implements ProvincesService {

    @Autowired
    private ProvincesMapper provincesMapper;


    @Override
    public PageInfo<Provinces> findPage(Provinces provinces, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(provinces);
        return new PageInfo<Provinces>(provincesMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Provinces> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Provinces>(provincesMapper.selectAll());
    }

    @Override
    public List<Provinces> findList(Provinces provinces){
        Example example = createExample(provinces);
        return provincesMapper.selectByExample(example);
    }

    public Example createExample(Provinces provinces){
        Example example=new Example(Provinces.class);
        Example.Criteria criteria = example.createCriteria();
        if(provinces!=null){
            if(!StringUtils.isEmpty(provinces.getProvinceid())){
                    criteria.andEqualTo("provinceid",provinces.getProvinceid());
            }
            if(!StringUtils.isEmpty(provinces.getProvince())){
                    criteria.andEqualTo("province",provinces.getProvince());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        provincesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Provinces provinces){
        provincesMapper.updateByPrimaryKeySelective(provinces);
    }

    @Override
    public void add(Provinces provinces){
        provincesMapper.insertSelective(provinces);
    }

    @Override
    public Provinces findById(String id){
        return  provincesMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Provinces> findAll() {
        return provincesMapper.selectAll();
    }
}
