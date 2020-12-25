package com.shop.seckill.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.entity.SeckillStatus;
import com.shop.seckill.dao.SeckillOrderMapper;
import com.shop.seckill.pojo.SeckillOrder;
import com.shop.seckill.service.SeckillOrderService;
import com.shop.seckill.task.MultiThreadingCreateOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String USER_SECKILL_QUEUE = "UserQueue";
    private static final String USER_SECKILL_STATUS_MAP = "SeckillStatus";

    /***
     * place seckill order by multiple threads
     * push seckill order status into queue in redis
     * @param time
     * @param id
     * @param username
     * @return
     */
    @Override
    public Boolean add(String time, Long id, String username) {
        SeckillStatus status = new SeckillStatus(username, new Date(), 1, id, time);
        // push from left , pop from right
        redisTemplate.boundListOps(USER_SECKILL_QUEUE).leftPush(status);
        // map used for query seckill order status
        redisTemplate.boundHashOps(USER_SECKILL_STATUS_MAP).put(username, status);
        // start a thread to place order
        multiThreadingCreateOrder.createOrder();
        return true;
    }

    /***
     * query user's status in the queue
     * @param username
     * @return
     */
    @Override
    public SeckillStatus queryStatus(String username) {
        return (SeckillStatus) redisTemplate.boundHashOps(USER_SECKILL_STATUS_MAP).get(username);
    }

    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(seckillOrder);
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder){
        Example example = createExample(seckillOrder);
        return seckillOrderMapper.selectByExample(example);
    }

    public Example createExample(SeckillOrder seckillOrder){
        Example example=new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillOrder!=null){
            if(!StringUtils.isEmpty(seckillOrder.getId())){
                    criteria.andEqualTo("id",seckillOrder.getId());
            }
            if(!StringUtils.isEmpty(seckillOrder.getSeckillId())){
                    criteria.andEqualTo("seckillId",seckillOrder.getSeckillId());
            }
            if(!StringUtils.isEmpty(seckillOrder.getMoney())){
                    criteria.andEqualTo("money",seckillOrder.getMoney());
            }
            if(!StringUtils.isEmpty(seckillOrder.getUserId())){
                    criteria.andEqualTo("userId",seckillOrder.getUserId());
            }
            if(!StringUtils.isEmpty(seckillOrder.getCreateTime())){
                    criteria.andEqualTo("createTime",seckillOrder.getCreateTime());
            }
            if(!StringUtils.isEmpty(seckillOrder.getPayTime())){
                    criteria.andEqualTo("payTime",seckillOrder.getPayTime());
            }
            if(!StringUtils.isEmpty(seckillOrder.getStatus())){
                    criteria.andEqualTo("status",seckillOrder.getStatus());
            }
            if(!StringUtils.isEmpty(seckillOrder.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",seckillOrder.getReceiverAddress());
            }
            if(!StringUtils.isEmpty(seckillOrder.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",seckillOrder.getReceiverMobile());
            }
            if(!StringUtils.isEmpty(seckillOrder.getReceiver())){
                    criteria.andEqualTo("receiver",seckillOrder.getReceiver());
            }
            if(!StringUtils.isEmpty(seckillOrder.getTransactionId())){
                    criteria.andEqualTo("transactionId",seckillOrder.getTransactionId());
            }
        }
        return example;
    }

    @Override
    public void delete(Long id){
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(SeckillOrder seckillOrder){
        seckillOrderMapper.updateByPrimaryKeySelective(seckillOrder);
    }

    @Override
    public void add(SeckillOrder seckillOrder){
        seckillOrderMapper.insertSelective(seckillOrder);
    }

    @Override
    public SeckillOrder findById(Long id){
        return  seckillOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }

}
