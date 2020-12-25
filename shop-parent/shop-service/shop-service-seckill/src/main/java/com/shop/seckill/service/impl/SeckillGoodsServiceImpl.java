package com.shop.seckill.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.seckill.dao.SeckillGoodsMapper;
import com.shop.seckill.pojo.SeckillGoods;
import com.shop.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public SeckillGoods findByTimeAndId(String time, Long id) {
        return (SeckillGoods)redisTemplate.boundHashOps("SeckillGoods_" + time).get(id);
    }

    @Override
    public PageInfo<SeckillGoods> findPage(SeckillGoods seckillGoods, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(seckillGoods);
        return new PageInfo<SeckillGoods>(seckillGoodsMapper.selectByExample(example));
    }

    @Override
    public PageInfo<SeckillGoods> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<SeckillGoods>(seckillGoodsMapper.selectAll());
    }

    @Override
    public List<SeckillGoods> findList(SeckillGoods seckillGoods){
        Example example = createExample(seckillGoods);
        return seckillGoodsMapper.selectByExample(example);
    }

    public Example createExample(SeckillGoods seckillGoods){
        Example example=new Example(SeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillGoods!=null){
            if(!StringUtils.isEmpty(seckillGoods.getId())){
                    criteria.andEqualTo("id",seckillGoods.getId());
            }
            if(!StringUtils.isEmpty(seckillGoods.getSupId())){
                    criteria.andEqualTo("supId",seckillGoods.getSupId());
            }
            if(!StringUtils.isEmpty(seckillGoods.getSkuId())){
                    criteria.andEqualTo("skuId",seckillGoods.getSkuId());
            }
            if(!StringUtils.isEmpty(seckillGoods.getName())){
                    criteria.andLike("name","%"+seckillGoods.getName()+"%");
            }
            if(!StringUtils.isEmpty(seckillGoods.getSmallPic())){
                    criteria.andEqualTo("smallPic",seckillGoods.getSmallPic());
            }
            if(!StringUtils.isEmpty(seckillGoods.getPrice())){
                    criteria.andEqualTo("price",seckillGoods.getPrice());
            }
            if(!StringUtils.isEmpty(seckillGoods.getCostPrice())){
                    criteria.andEqualTo("costPrice",seckillGoods.getCostPrice());
            }
            if(!StringUtils.isEmpty(seckillGoods.getCreateTime())){
                    criteria.andEqualTo("createTime",seckillGoods.getCreateTime());
            }
            if(!StringUtils.isEmpty(seckillGoods.getCheckTime())){
                    criteria.andEqualTo("checkTime",seckillGoods.getCheckTime());
            }
            if(!StringUtils.isEmpty(seckillGoods.getStatus())){
                    criteria.andEqualTo("status",seckillGoods.getStatus());
            }
            if(!StringUtils.isEmpty(seckillGoods.getStartTime())){
                    criteria.andEqualTo("startTime",seckillGoods.getStartTime());
            }
            if(!StringUtils.isEmpty(seckillGoods.getEndTime())){
                    criteria.andEqualTo("endTime",seckillGoods.getEndTime());
            }
            if(!StringUtils.isEmpty(seckillGoods.getNum())){
                    criteria.andEqualTo("num",seckillGoods.getNum());
            }
            if(!StringUtils.isEmpty(seckillGoods.getStockCount())){
                    criteria.andEqualTo("stockCount",seckillGoods.getStockCount());
            }
            if(!StringUtils.isEmpty(seckillGoods.getIntroduction())){
                    criteria.andEqualTo("introduction",seckillGoods.getIntroduction());
            }
        }
        return example;
    }

    @Override
    public void delete(Long id){
        seckillGoodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(SeckillGoods seckillGoods){
        seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
    }

    @Override
    public void add(SeckillGoods seckillGoods){
        seckillGoodsMapper.insertSelective(seckillGoods);
    }

    @Override
    public SeckillGoods findById(Long id){
        return  seckillGoodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SeckillGoods> findAll() {
        return seckillGoodsMapper.selectAll();
    }

    @Override
    public List<SeckillGoods> list(String timeInterval) {
        return redisTemplate.boundHashOps("SeckillGoods_" + timeInterval).values();
    }
}
