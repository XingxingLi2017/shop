package com.shop.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.order.dao.OrderLogMapper;
import com.shop.order.pojo.OrderLog;
import com.shop.order.service.OrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OrderLogServiceImpl implements OrderLogService {

    @Autowired
    private OrderLogMapper orderLogMapper;


    @Override
    public PageInfo<OrderLog> findPage(OrderLog orderLog, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(orderLog);
        return new PageInfo<OrderLog>(orderLogMapper.selectByExample(example));
    }

    @Override
    public PageInfo<OrderLog> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<OrderLog>(orderLogMapper.selectAll());
    }

    @Override
    public List<OrderLog> findList(OrderLog orderLog){
        Example example = createExample(orderLog);
        return orderLogMapper.selectByExample(example);
    }

    public Example createExample(OrderLog orderLog){
        Example example=new Example(OrderLog.class);
        Example.Criteria criteria = example.createCriteria();
        if(orderLog!=null){
            if(!StringUtils.isEmpty(orderLog.getId())){
                    criteria.andEqualTo("id",orderLog.getId());
            }
            if(!StringUtils.isEmpty(orderLog.getOperater())){
                    criteria.andEqualTo("operater",orderLog.getOperater());
            }
            if(!StringUtils.isEmpty(orderLog.getOperateTime())){
                    criteria.andEqualTo("operateTime",orderLog.getOperateTime());
            }
            if(!StringUtils.isEmpty(orderLog.getOrderId())){
                    criteria.andEqualTo("orderId",orderLog.getOrderId());
            }
            if(!StringUtils.isEmpty(orderLog.getOrderStatus())){
                    criteria.andEqualTo("orderStatus",orderLog.getOrderStatus());
            }
            if(!StringUtils.isEmpty(orderLog.getPayStatus())){
                    criteria.andEqualTo("payStatus",orderLog.getPayStatus());
            }
            if(!StringUtils.isEmpty(orderLog.getConsignStatus())){
                    criteria.andEqualTo("consignStatus",orderLog.getConsignStatus());
            }
            if(!StringUtils.isEmpty(orderLog.getRemarks())){
                    criteria.andEqualTo("remarks",orderLog.getRemarks());
            }
            if(!StringUtils.isEmpty(orderLog.getMoney())){
                    criteria.andEqualTo("money",orderLog.getMoney());
            }
            if(!StringUtils.isEmpty(orderLog.getUsername())){
                    criteria.andLike("username","%"+orderLog.getUsername()+"%");
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        orderLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderLog orderLog){
        orderLogMapper.updateByPrimaryKeySelective(orderLog);
    }

    @Override
    public void add(OrderLog orderLog){
        orderLogMapper.insertSelective(orderLog);
    }

    @Override
    public OrderLog findById(String id){
        return  orderLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrderLog> findAll() {
        return orderLogMapper.selectAll();
    }
}
