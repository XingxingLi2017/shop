package com.shop.user.feign;

import com.shop.entity.Result;
import com.shop.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="user")
@RequestMapping("/user")
public interface UserFeign {

    @GetMapping("/{id}")
    public Result<User> findByUsername(@PathVariable("id") String id);
}
