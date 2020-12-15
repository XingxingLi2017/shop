package com.shop.oauth.controller;

import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.oauth.service.UserLoginService;
import com.shop.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/user")
public class UserLoginController {

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.clientId}")
    private String clientId;

    @Autowired
    UserLoginService userLoginService;

    @PostMapping("/login")
    public Result login(String username, String password) throws UnsupportedEncodingException {
        String grantType = "password";
        AuthToken token = userLoginService.login(username, password, clientId, clientSecret, grantType);
        if(token != null) {
            return new Result(true, StatusCode.OK, "login successfully.", token);
        }
        return new Result(false, StatusCode.LOGINERROR, "fail to login");
    }
}
