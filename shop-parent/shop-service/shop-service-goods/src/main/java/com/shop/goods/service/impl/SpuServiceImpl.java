package com.shop.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.goods.dao.BrandMapper;
import com.shop.goods.dao.CategoryMapper;
import com.shop.goods.dao.SkuMapper;
import com.shop.goods.dao.SpuMapper;
import com.shop.goods.pojo.*;
import com.shop.goods.service.SpuService;
import com.shop.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    private IdWorker idWorker;

    @Override
    public void realDel(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!"1".equals(spu.getIsDelete())){
            throw new RuntimeException("The goods was not deleted.");
        }
        spuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void restore(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!"1".equals(spu.getIsDelete())){
            throw new RuntimeException("The goods was not deleted.");
        }
        spu.setIsDelete("0");
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void putMany(Long[] spuIds) {
        // update tb_spu set IsMarketable=1 where id in (ids) and isDelete=0 and status=1
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andIn("id", Arrays.asList(spuIds));
        criteria.andEqualTo("isDelete", "0");
        criteria.andEqualTo("status", "1");

        Spu spu = new Spu();
        spu.setIsMarketable("1");

        spuMapper.updateByExampleSelective(spu, example);
    }

    @Override
    public void put(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw new RuntimeException("The goods was deleted.");
        }
        // didn't pass the audit
        if(!spu.getStatus().equals("1")) {
            throw new RuntimeException("The goods didn't pass the audit.");
        }
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void pull(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if(spu.getIsDelete().equalsIgnoreCase("1")) {
            throw new RuntimeException("The goods was deleted.");
        }

        spu.setIsMarketable("0"); // off the shelf
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void audit(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if(spu.getIsDelete().equalsIgnoreCase("1")) {
            throw new RuntimeException("The goods was deleted.");
        }

        spu.setStatus("1");
        spu.setIsMarketable("1"); // put on the shelf
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /***
     * the id belongs to spu
     * @param id
     * @return
     */
    @Override
    public Goods findGoodsById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        Sku sku = new Sku();
        sku.setSpuId(id + "");
        List<Sku> skus = skuMapper.select(sku);
        Goods goods = new Goods();
        goods.setSkuList(skus);
        goods.setSpu(spu);
        return goods;
    }

    @Transactional
    @Override
    public void saveGoods(Goods goods) {
        Spu spu = goods.getSpu();
        if(spu.getId() != null) {
            // update operation
            spuMapper.updateByPrimaryKeySelective(spu);
            Sku s = new Sku();
            s.setSpuId(spu.getId());
            skuMapper.delete(s);
        } else {
            // addition operation
            spu.setId(idWorker.nextId()+"");
            spuMapper.insertSelective(spu);
        }

        Date date = new Date();
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

        List<Sku> skuList = goods.getSkuList();
        for(Sku sku : skuList) {
            sku.setId(idWorker.nextId()+"");

            // spu name + spec info = name
            String name = spu.getName();
            if(StringUtils.isEmpty(sku.getSpec())) {
                sku.setSpec("{}");
            }
            Map<String, String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
            for(String key : specMap.keySet()) {
                name += " " + specMap.get(key);
            }
            sku.setName(name);

            sku.setCreateTime(date);
            sku.setSpuId(spu.getId());
            sku.setCategoryId(category.getId());    // the level-3 category id
            sku.setCategoryName(category.getName());
            sku.setBrandName(brand.getName());

            skuMapper.insertSelective(sku);
        }
    }


    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(spu);
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Spu> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    @Override
    public List<Spu> findList(Spu spu){
        Example example = createExample(spu);
        return spuMapper.selectByExample(example);
    }


    public Example createExample(Spu spu){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(spu!=null){
            if(!StringUtils.isEmpty(spu.getId())){
                    criteria.andEqualTo("id",spu.getId());
            }
            if(!StringUtils.isEmpty(spu.getSn())){
                    criteria.andEqualTo("sn",spu.getSn());
            }
            if(!StringUtils.isEmpty(spu.getName())){
                    criteria.andLike("name","%"+spu.getName()+"%");
            }
            if(!StringUtils.isEmpty(spu.getCaption())){
                    criteria.andEqualTo("caption",spu.getCaption());
            }
            if(!StringUtils.isEmpty(spu.getBrandId())){
                    criteria.andEqualTo("brandId",spu.getBrandId());
            }
            if(!StringUtils.isEmpty(spu.getCategory1Id())){
                    criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            if(!StringUtils.isEmpty(spu.getCategory2Id())){
                    criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            if(!StringUtils.isEmpty(spu.getCategory3Id())){
                    criteria.andEqualTo("category3Id",spu.getCategory3Id());
            }
            if(!StringUtils.isEmpty(spu.getTemplateId())){
                    criteria.andEqualTo("templateId",spu.getTemplateId());
            }
            if(!StringUtils.isEmpty(spu.getFreightId())){
                    criteria.andEqualTo("freightId",spu.getFreightId());
            }
            if(!StringUtils.isEmpty(spu.getImage())){
                    criteria.andEqualTo("image",spu.getImage());
            }
            if(!StringUtils.isEmpty(spu.getImages())){
                    criteria.andEqualTo("images",spu.getImages());
            }
            if(!StringUtils.isEmpty(spu.getSaleService())){
                    criteria.andEqualTo("saleService",spu.getSaleService());
            }
            if(!StringUtils.isEmpty(spu.getIntroduction())){
                    criteria.andEqualTo("introduction",spu.getIntroduction());
            }
            if(!StringUtils.isEmpty(spu.getSpecItems())){
                    criteria.andEqualTo("specItems",spu.getSpecItems());
            }
            if(!StringUtils.isEmpty(spu.getParaItems())){
                    criteria.andEqualTo("paraItems",spu.getParaItems());
            }
            if(!StringUtils.isEmpty(spu.getSaleNum())){
                    criteria.andEqualTo("saleNum",spu.getSaleNum());
            }
            if(!StringUtils.isEmpty(spu.getCommentNum())){
                    criteria.andEqualTo("commentNum",spu.getCommentNum());
            }
            if(!StringUtils.isEmpty(spu.getIsMarketable())){
                    criteria.andEqualTo("isMarketable",spu.getIsMarketable());
            }
            if(!StringUtils.isEmpty(spu.getIsEnableSpec())){
                    criteria.andEqualTo("isEnableSpec",spu.getIsEnableSpec());
            }
            if(!StringUtils.isEmpty(spu.getIsDelete())){
                    criteria.andEqualTo("isDelete",spu.getIsDelete());
            }
            if(!StringUtils.isEmpty(spu.getStatus())){
                    criteria.andEqualTo("status",spu.getStatus());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (!spu.getIsMarketable().equals("0")){
            throw new RuntimeException("Can't delete marketable goods.");
        }
        spu.setIsDelete("1");
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }

    @Override
    public Spu findById(String id){
        return  spuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }
}
