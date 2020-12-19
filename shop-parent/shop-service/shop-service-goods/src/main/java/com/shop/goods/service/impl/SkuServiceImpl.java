package com.shop.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.goods.dao.SkuMapper;
import com.shop.goods.pojo.Sku;
import com.shop.goods.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;


    @Override
    public List<Sku> findByStatus(String status) {
        Sku sku = new Sku();
        sku.setStatus(status);
        return skuMapper.select(sku);
    }

    @Override
    public PageInfo<Sku> findPage(Sku sku, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(sku);
        return new PageInfo<Sku>(skuMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Sku> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Sku>(skuMapper.selectAll());
    }

    @Override
    public List<Sku> findList(Sku sku){
        Example example = createExample(sku);
        return skuMapper.selectByExample(example);
    }


    public Example createExample(Sku sku){
        Example example=new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        if(sku!=null){
            if(!StringUtils.isEmpty(sku.getId())){
                    criteria.andEqualTo("id",sku.getId());
            }
            if(!StringUtils.isEmpty(sku.getSn())){
                    criteria.andEqualTo("sn",sku.getSn());
            }
            if(!StringUtils.isEmpty(sku.getName())){
                    criteria.andLike("name","%"+sku.getName()+"%");
            }
            if(!StringUtils.isEmpty(sku.getPrice())){
                    criteria.andEqualTo("price",sku.getPrice());
            }
            if(!StringUtils.isEmpty(sku.getNum())){
                    criteria.andEqualTo("num",sku.getNum());
            }
            if(!StringUtils.isEmpty(sku.getAlertNum())){
                    criteria.andEqualTo("alertNum",sku.getAlertNum());
            }
            if(!StringUtils.isEmpty(sku.getImage())){
                    criteria.andEqualTo("image",sku.getImage());
            }
            if(!StringUtils.isEmpty(sku.getImages())){
                    criteria.andEqualTo("images",sku.getImages());
            }
            if(!StringUtils.isEmpty(sku.getWeight())){
                    criteria.andEqualTo("weight",sku.getWeight());
            }
            if(!StringUtils.isEmpty(sku.getCreateTime())){
                    criteria.andEqualTo("createTime",sku.getCreateTime());
            }
            if(!StringUtils.isEmpty(sku.getUpdateTime())){
                    criteria.andEqualTo("updateTime",sku.getUpdateTime());
            }
            if(!StringUtils.isEmpty(sku.getSpuId())){
                    criteria.andEqualTo("spuId",sku.getSpuId());
            }
            if(!StringUtils.isEmpty(sku.getCategoryId())){
                    criteria.andEqualTo("categoryId",sku.getCategoryId());
            }
            if(!StringUtils.isEmpty(sku.getCategoryName())){
                    criteria.andEqualTo("categoryName",sku.getCategoryName());
            }
            if(!StringUtils.isEmpty(sku.getBrandName())){
                    criteria.andEqualTo("brandName",sku.getBrandName());
            }
            if(!StringUtils.isEmpty(sku.getSpec())){
                    criteria.andEqualTo("spec",sku.getSpec());
            }
            if(!StringUtils.isEmpty(sku.getSaleNum())){
                    criteria.andEqualTo("saleNum",sku.getSaleNum());
            }
            if(!StringUtils.isEmpty(sku.getCommentNum())){
                    criteria.andEqualTo("commentNum",sku.getCommentNum());
            }
            if(!StringUtils.isEmpty(sku.getStatus())){
                    criteria.andEqualTo("status",sku.getStatus());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        skuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Sku sku){
        skuMapper.updateByPrimaryKey(sku);
    }

    @Override
    public void add(Sku sku){
        skuMapper.insert(sku);
    }

    @Override
    public Sku findById(String id){
        return  skuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Sku> findAll() {
        return skuMapper.selectAll();
    }

    @Override
    public void decrCount(Map<String, String> decrMap) {
        decrMap.forEach((key, value) -> {
            Long skuId = Long.parseLong(key);
            Integer decrNum = Integer.parseInt(value);

            // decrease if decrNum <= inventory , use row lock in DB to solve synchronization problem
            int count = skuMapper.decrCount(skuId, decrNum);
            if(count <= 0) {
                throw  new RuntimeException("Don't have enough inventory, rollback transaction");
            }

        });
    }
}
