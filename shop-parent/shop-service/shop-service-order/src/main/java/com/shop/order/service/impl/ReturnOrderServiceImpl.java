package com.shop.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.order.dao.ReturnOrderMapper;
import com.shop.order.pojo.ReturnOrder;
import com.shop.order.service.ReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {

    @Autowired
    private ReturnOrderMapper returnOrderMapper;


    @Override
    public PageInfo<ReturnOrder> findPage(ReturnOrder returnOrder, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(returnOrder);
        return new PageInfo<ReturnOrder>(returnOrderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<ReturnOrder> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<ReturnOrder>(returnOrderMapper.selectAll());
    }

    @Override
    public List<ReturnOrder> findList(ReturnOrder returnOrder){
        Example example = createExample(returnOrder);
        return returnOrderMapper.selectByExample(example);
    }

    public Example createExample(ReturnOrder returnOrder){
        Example example=new Example(ReturnOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if(returnOrder!=null){
            if(!StringUtils.isEmpty(returnOrder.getId())){
                    criteria.andEqualTo("id",returnOrder.getId());
            }
            if(!StringUtils.isEmpty(returnOrder.getOrderId())){
                    criteria.andEqualTo("orderId",returnOrder.getOrderId());
            }
            if(!StringUtils.isEmpty(returnOrder.getApplyTime())){
                    criteria.andEqualTo("applyTime",returnOrder.getApplyTime());
            }
            if(!StringUtils.isEmpty(returnOrder.getUserId())){
                    criteria.andEqualTo("userId",returnOrder.getUserId());
            }
            if(!StringUtils.isEmpty(returnOrder.getUserAccount())){
                    criteria.andEqualTo("userAccount",returnOrder.getUserAccount());
            }
            if(!StringUtils.isEmpty(returnOrder.getLinkman())){
                    criteria.andEqualTo("linkman",returnOrder.getLinkman());
            }
            if(!StringUtils.isEmpty(returnOrder.getLinkmanMobile())){
                    criteria.andEqualTo("linkmanMobile",returnOrder.getLinkmanMobile());
            }
            if(!StringUtils.isEmpty(returnOrder.getType())){
                    criteria.andEqualTo("type",returnOrder.getType());
            }
            if(!StringUtils.isEmpty(returnOrder.getReturnMoney())){
                    criteria.andEqualTo("returnMoney",returnOrder.getReturnMoney());
            }
            if(!StringUtils.isEmpty(returnOrder.getIsReturnFreight())){
                    criteria.andEqualTo("isReturnFreight",returnOrder.getIsReturnFreight());
            }
            if(!StringUtils.isEmpty(returnOrder.getStatus())){
                    criteria.andEqualTo("status",returnOrder.getStatus());
            }
            if(!StringUtils.isEmpty(returnOrder.getDisposeTime())){
                    criteria.andEqualTo("disposeTime",returnOrder.getDisposeTime());
            }
            if(!StringUtils.isEmpty(returnOrder.getReturnCause())){
                    criteria.andEqualTo("returnCause",returnOrder.getReturnCause());
            }
            if(!StringUtils.isEmpty(returnOrder.getEvidence())){
                    criteria.andEqualTo("evidence",returnOrder.getEvidence());
            }
            if(!StringUtils.isEmpty(returnOrder.getDescription())){
                    criteria.andEqualTo("description",returnOrder.getDescription());
            }
            if(!StringUtils.isEmpty(returnOrder.getRemark())){
                    criteria.andEqualTo("remark",returnOrder.getRemark());
            }
            if(!StringUtils.isEmpty(returnOrder.getAdminId())){
                    criteria.andEqualTo("adminId",returnOrder.getAdminId());
            }
        }
        return example;
    }

    @Override
    public void delete(Long id){
        returnOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(ReturnOrder returnOrder){
        returnOrderMapper.updateByPrimaryKeySelective(returnOrder);
    }

    @Override
    public void add(ReturnOrder returnOrder){
        returnOrderMapper.insertSelective(returnOrder);
    }

    @Override
    public ReturnOrder findById(Long id){
        return  returnOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ReturnOrder> findAll() {
        return returnOrderMapper.selectAll();
    }
}
