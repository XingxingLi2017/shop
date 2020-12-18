package com.shop.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.order.dao.OrderConfigMapper;
import com.shop.order.pojo.OrderConfig;
import com.shop.order.service.OrderConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OrderConfigServiceImpl implements OrderConfigService {

    @Autowired
    private OrderConfigMapper orderConfigMapper;


    @Override
    public PageInfo<OrderConfig> findPage(OrderConfig orderConfig, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(orderConfig);
        return new PageInfo<OrderConfig>(orderConfigMapper.selectByExample(example));
    }

    @Override
    public PageInfo<OrderConfig> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<OrderConfig>(orderConfigMapper.selectAll());
    }

    @Override
    public List<OrderConfig> findList(OrderConfig orderConfig){
        Example example = createExample(orderConfig);
        return orderConfigMapper.selectByExample(example);
    }

    public Example createExample(OrderConfig orderConfig){
        Example example=new Example(OrderConfig.class);
        Example.Criteria criteria = example.createCriteria();
        if(orderConfig!=null){
            if(!StringUtils.isEmpty(orderConfig.getId())){
                    criteria.andEqualTo("id",orderConfig.getId());
            }
            if(!StringUtils.isEmpty(orderConfig.getOrderTimeout())){
                    criteria.andEqualTo("orderTimeout",orderConfig.getOrderTimeout());
            }
            if(!StringUtils.isEmpty(orderConfig.getSeckillTimeout())){
                    criteria.andEqualTo("seckillTimeout",orderConfig.getSeckillTimeout());
            }
            if(!StringUtils.isEmpty(orderConfig.getTakeTimeout())){
                    criteria.andEqualTo("takeTimeout",orderConfig.getTakeTimeout());
            }
            if(!StringUtils.isEmpty(orderConfig.getServiceTimeout())){
                    criteria.andEqualTo("serviceTimeout",orderConfig.getServiceTimeout());
            }
            if(!StringUtils.isEmpty(orderConfig.getCommentTimeout())){
                    criteria.andEqualTo("commentTimeout",orderConfig.getCommentTimeout());
            }
        }
        return example;
    }

    @Override
    public void delete(Integer id){
        orderConfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderConfig orderConfig){
        orderConfigMapper.updateByPrimaryKeySelective(orderConfig);
    }

    @Override
    public void add(OrderConfig orderConfig){
        orderConfigMapper.insertSelective(orderConfig);
    }

    @Override
    public OrderConfig findById(Integer id){
        return  orderConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrderConfig> findAll() {
        return orderConfigMapper.selectAll();
    }
}
