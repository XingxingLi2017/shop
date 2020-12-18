package com.shop.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.order.dao.CategoryReportMapper;
import com.shop.order.pojo.CategoryReport;
import com.shop.order.service.CategoryReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class CategoryReportServiceImpl implements CategoryReportService {

    @Autowired
    private CategoryReportMapper categoryReportMapper;


    @Override
    public PageInfo<CategoryReport> findPage(CategoryReport categoryReport, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(categoryReport);
        return new PageInfo<CategoryReport>(categoryReportMapper.selectByExample(example));
    }

    @Override
    public PageInfo<CategoryReport> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<CategoryReport>(categoryReportMapper.selectAll());
    }

    @Override
    public List<CategoryReport> findList(CategoryReport categoryReport){
        Example example = createExample(categoryReport);
        return categoryReportMapper.selectByExample(example);
    }

    public Example createExample(CategoryReport categoryReport){
        Example example=new Example(CategoryReport.class);
        Example.Criteria criteria = example.createCriteria();
        if(categoryReport!=null){
            if(!StringUtils.isEmpty(categoryReport.getCategoryId1())){
                    criteria.andEqualTo("categoryId1",categoryReport.getCategoryId1());
            }
            if(!StringUtils.isEmpty(categoryReport.getCategoryId2())){
                    criteria.andEqualTo("categoryId2",categoryReport.getCategoryId2());
            }
            if(!StringUtils.isEmpty(categoryReport.getCategoryId3())){
                    criteria.andEqualTo("categoryId3",categoryReport.getCategoryId3());
            }
            if(!StringUtils.isEmpty(categoryReport.getCountDate())){
                    criteria.andEqualTo("countDate",categoryReport.getCountDate());
            }
            if(!StringUtils.isEmpty(categoryReport.getNum())){
                    criteria.andEqualTo("num",categoryReport.getNum());
            }
            if(!StringUtils.isEmpty(categoryReport.getMoney())){
                    criteria.andEqualTo("money",categoryReport.getMoney());
            }
        }
        return example;
    }

    @Override
    public void delete(Date id){
        categoryReportMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(CategoryReport categoryReport){
        categoryReportMapper.updateByPrimaryKeySelective(categoryReport);
    }

    @Override
    public void add(CategoryReport categoryReport){
        categoryReportMapper.insertSelective(categoryReport);
    }

    @Override
    public CategoryReport findById(Date id){
        return  categoryReportMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CategoryReport> findAll() {
        return categoryReportMapper.selectAll();
    }
}
