package com.shop.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.goods.dao.PrefMapper;
import com.shop.goods.pojo.Pref;
import com.shop.goods.service.PrefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class PrefServiceImpl implements PrefService {

    @Autowired
    private PrefMapper prefMapper;


    @Override
    public PageInfo<Pref> findPage(Pref pref, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(pref);
        return new PageInfo<Pref>(prefMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Pref> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Pref>(prefMapper.selectAll());
    }

    @Override
    public List<Pref> findList(Pref pref){
        //构建查询条件
        Example example = createExample(pref);
        //根据构建的条件查询数据
        return prefMapper.selectByExample(example);
    }


    public Example createExample(Pref pref){
        Example example=new Example(Pref.class);
        Example.Criteria criteria = example.createCriteria();
        if(pref!=null){
            if(!StringUtils.isEmpty(pref.getId())){
                    criteria.andEqualTo("id",pref.getId());
            }
            if(!StringUtils.isEmpty(pref.getCateId())){
                    criteria.andEqualTo("cateId",pref.getCateId());
            }
            if(!StringUtils.isEmpty(pref.getBuyMoney())){
                    criteria.andEqualTo("buyMoney",pref.getBuyMoney());
            }
            if(!StringUtils.isEmpty(pref.getPreMoney())){
                    criteria.andEqualTo("preMoney",pref.getPreMoney());
            }
            if(!StringUtils.isEmpty(pref.getStartTime())){
                    criteria.andEqualTo("startTime",pref.getStartTime());
            }
            if(!StringUtils.isEmpty(pref.getEndTime())){
                    criteria.andEqualTo("endTime",pref.getEndTime());
            }
            if(!StringUtils.isEmpty(pref.getType())){
                    criteria.andEqualTo("type",pref.getType());
            }
            if(!StringUtils.isEmpty(pref.getState())){
                    criteria.andEqualTo("state",pref.getState());
            }
        }
        return example;
    }

    @Override
    public void delete(Integer id){
        prefMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Pref pref){
        prefMapper.updateByPrimaryKey(pref);
    }

    @Override
    public void add(Pref pref){
        prefMapper.insert(pref);
    }

    @Override
    public Pref findById(Integer id){
        return  prefMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Pref> findAll() {
        return prefMapper.selectAll();
    }
}
