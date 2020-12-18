package com.shop.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.order.dao.ReturnCauseMapper;
import com.shop.order.pojo.ReturnCause;
import com.shop.order.service.ReturnCauseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ReturnCauseServiceImpl implements ReturnCauseService {

    @Autowired
    private ReturnCauseMapper returnCauseMapper;


    @Override
    public PageInfo<ReturnCause> findPage(ReturnCause returnCause, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(returnCause);
        return new PageInfo<ReturnCause>(returnCauseMapper.selectByExample(example));
    }

    @Override
    public PageInfo<ReturnCause> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<ReturnCause>(returnCauseMapper.selectAll());
    }

    @Override
    public List<ReturnCause> findList(ReturnCause returnCause){
        Example example = createExample(returnCause);
        return returnCauseMapper.selectByExample(example);
    }

    public Example createExample(ReturnCause returnCause){
        Example example=new Example(ReturnCause.class);
        Example.Criteria criteria = example.createCriteria();
        if(returnCause!=null){
            if(!StringUtils.isEmpty(returnCause.getId())){
                    criteria.andEqualTo("id",returnCause.getId());
            }
            if(!StringUtils.isEmpty(returnCause.getCause())){
                    criteria.andEqualTo("cause",returnCause.getCause());
            }
            if(!StringUtils.isEmpty(returnCause.getSeq())){
                    criteria.andEqualTo("seq",returnCause.getSeq());
            }
            if(!StringUtils.isEmpty(returnCause.getStatus())){
                    criteria.andEqualTo("status",returnCause.getStatus());
            }
        }
        return example;
    }

    @Override
    public void delete(Integer id){
        returnCauseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(ReturnCause returnCause){
        returnCauseMapper.updateByPrimaryKeySelective(returnCause);
    }

    @Override
    public void add(ReturnCause returnCause){
        returnCauseMapper.insertSelective(returnCause);
    }

    @Override
    public ReturnCause findById(Integer id){
        return  returnCauseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ReturnCause> findAll() {
        return returnCauseMapper.selectAll();
    }
}
