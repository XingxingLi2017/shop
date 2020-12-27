package com.shop.pay.feign;

import com.shop.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("pay")
@RequestMapping("/wechat/pay")
public interface WechatPayFeign {
    /***
     * close wechat order
     */
    @GetMapping("/close")
    Result closeOrder(@RequestParam("outTradeNo") String outTradeNo);
}
