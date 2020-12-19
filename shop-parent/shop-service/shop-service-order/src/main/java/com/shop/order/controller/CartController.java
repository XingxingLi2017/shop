package com.shop.order.controller;


import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.order.pojo.OrderItem;
import com.shop.order.service.CartService;
import com.shop.util.TokenDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Value("${publicKeyFile}")
    private String publicKeyFileName;
    /****
     * add goods into cart
     * @param num
     * @param skuId
     * @return
     */
    @GetMapping("/add")
    public Result add(@RequestParam("num") Integer num, @RequestParam("id") Long skuId) {
        Map<String, String> userInfo = TokenDecoder.getUserInfo();
        cartService.add(num, skuId, userInfo.get("username"));
        return new Result(true, StatusCode.OK, "Add item into cart successfully.");
    }

    @GetMapping("/list")
    public Result<List<OrderItem>> list(){
        Map<String, String> userInfo = TokenDecoder.getUserInfo();
        List<OrderItem> list = cartService.list(userInfo.get("username"));
        return new Result(true, StatusCode.OK, "Get items in cart successfully.", list);
    }
}
