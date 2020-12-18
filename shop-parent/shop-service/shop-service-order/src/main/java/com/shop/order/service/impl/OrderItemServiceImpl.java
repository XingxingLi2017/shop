package com.shop.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.order.dao.OrderItemMapper;
import com.shop.order.pojo.OrderItem;
import com.shop.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;


    @Override
    public PageInfo<OrderItem> findPage(OrderItem orderItem, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(orderItem);
        return new PageInfo<OrderItem>(orderItemMapper.selectByExample(example));
    }

    @Override
    public PageInfo<OrderItem> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<OrderItem>(orderItemMapper.selectAll());
    }

    @Override
    public List<OrderItem> findList(OrderItem orderItem){
        Example example = createExample(orderItem);
        return orderItemMapper.selectByExample(example);
    }

    public Example createExample(OrderItem orderItem){
        Example example=new Example(OrderItem.class);
        Example.Criteria criteria = example.createCriteria();
        if(orderItem!=null){
            if(!StringUtils.isEmpty(orderItem.getId())){
                    criteria.andEqualTo("id",orderItem.getId());
            }
            if(!StringUtils.isEmpty(orderItem.getCategoryId1())){
                    criteria.andEqualTo("categoryId1",orderItem.getCategoryId1());
            }
            if(!StringUtils.isEmpty(orderItem.getCategoryId2())){
                    criteria.andEqualTo("categoryId2",orderItem.getCategoryId2());
            }
            if(!StringUtils.isEmpty(orderItem.getCategoryId3())){
                    criteria.andEqualTo("categoryId3",orderItem.getCategoryId3());
            }
            if(!StringUtils.isEmpty(orderItem.getSpuId())){
                    criteria.andEqualTo("spuId",orderItem.getSpuId());
            }
            if(!StringUtils.isEmpty(orderItem.getSkuId())){
                    criteria.andEqualTo("skuId",orderItem.getSkuId());
            }
            if(!StringUtils.isEmpty(orderItem.getOrderId())){
                    criteria.andEqualTo("orderId",orderItem.getOrderId());
            }
            if(!StringUtils.isEmpty(orderItem.getName())){
                    criteria.andLike("name","%"+orderItem.getName()+"%");
            }
            if(!StringUtils.isEmpty(orderItem.getPrice())){
                    criteria.andEqualTo("price",orderItem.getPrice());
            }
            if(!StringUtils.isEmpty(orderItem.getNum())){
                    criteria.andEqualTo("num",orderItem.getNum());
            }
            if(!StringUtils.isEmpty(orderItem.getMoney())){
                    criteria.andEqualTo("money",orderItem.getMoney());
            }
            if(!StringUtils.isEmpty(orderItem.getPayMoney())){
                    criteria.andEqualTo("payMoney",orderItem.getPayMoney());
            }
            if(!StringUtils.isEmpty(orderItem.getImage())){
                    criteria.andEqualTo("image",orderItem.getImage());
            }
            if(!StringUtils.isEmpty(orderItem.getWeight())){
                    criteria.andEqualTo("weight",orderItem.getWeight());
            }
            if(!StringUtils.isEmpty(orderItem.getPostFee())){
                    criteria.andEqualTo("postFee",orderItem.getPostFee());
            }
            if(!StringUtils.isEmpty(orderItem.getIsReturn())){
                    criteria.andEqualTo("isReturn",orderItem.getIsReturn());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem orderItem){
        orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }

    @Override
    public void add(OrderItem orderItem){
        orderItemMapper.insertSelective(orderItem);
    }

    @Override
    public OrderItem findById(String id){
        return  orderItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemMapper.selectAll();
    }
}
