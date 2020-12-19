package com.shop.goods.dao;
import com.shop.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku> {

    @Update("UPDATE tb_sku SET num=num-#{num} WHERE id=#{id} AND num >= #{num}")
    int decrCount(@Param("id") Long skuId, @Param("num") Integer decrNum);
}
