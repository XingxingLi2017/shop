package com.shop.user.feign;

import com.shop.entity.Result;
import com.shop.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="user")
@RequestMapping("/user")
public interface UserFeign {

    @GetMapping("/load/{id}")
    public Result<com.shop.user.pojo.User> findByUsername(@PathVariable("id") String id);

    /**
     * add points for user
     * @param points
     * @return
     */
    @GetMapping("/points/add")
    public Result addPoints(@RequestParam("points") Integer points);
}
