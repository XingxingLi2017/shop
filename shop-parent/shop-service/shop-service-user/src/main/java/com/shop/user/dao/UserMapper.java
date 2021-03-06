package com.shop.user.dao;
import com.shop.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Update("update tb_user set points = points + #{points} where username = #{username}")
    void addPoints(@Param("username") String username, @Param("points") Integer points);
}
