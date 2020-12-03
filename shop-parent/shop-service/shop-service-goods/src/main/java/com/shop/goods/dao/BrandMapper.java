package com.shop.goods.dao;

import com.shop.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends Mapper<Brand> {

    @Select(" SELECT NAME, image" +
            " FROM tb_brand" +
            " WHERE id IN (SELECT brand_id" +
            "               FROM tb_category_brand" +
            "               WHERE category_id IN (SELECT id" +
            "                                       FROM tb_category" +
            "                                       WHERE NAME = #{categoryName})) ORDER BY seq ASC")
    List<Map> findBrandListByCategoryName(@Param("categoryName")String categoryName);

    @Select(" SELECT tb.*" +
            " FROM tb_brand tb, tb_category_brand tcb" +
            " WHERE tb.id = tcb.brand_id AND tcb.category_id=#{categoryId}")
    List<Brand> findByCategory(@Param("categoryId") Integer categoryId);
}
