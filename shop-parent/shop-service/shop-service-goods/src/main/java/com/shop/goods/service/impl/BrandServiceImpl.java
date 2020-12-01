package com.shop.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.goods.dao.BrandMapper;
import com.shop.goods.pojo.Brand;
import com.shop.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        // xxxSelective : ignore null properties in the object
        brandMapper.insertSelective(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);
        return brandMapper.selectByExample(example);
    }

    /****
     * pagination
     * @param page current page
     * @param size page size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Brand> list = brandMapper.selectAll();
        //Page<Brand> ret = (Page)list;
        return new PageInfo<Brand>(list);
    }

    /****
     * get brands by pages based on given conditions
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Brand brand, int page, int size) {
        Example example = createExample(brand);
        PageHelper.startPage(page, size);
        List<Brand> list = brandMapper.selectByExample(example);
        return new PageInfo<Brand>(list);
    }

    @Override
    public List<Map> findBrandListByCategoryName(String categoryName) {
        return brandMapper.findBrandListByCategoryName(categoryName);
    }

    /****
     * create conditions
     * @param brand
     * @return
     */
    private Example createExample(Brand brand) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        if(brand != null) {
            if(!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name", "%" + brand.getName()+ "%");
            }
            if(!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter", brand.getLetter());
            }
            if(!StringUtils.isEmpty(brand.getId())) {
                criteria.andEqualTo("id", brand.getId());
            }
        }
        return example;
    }
}
