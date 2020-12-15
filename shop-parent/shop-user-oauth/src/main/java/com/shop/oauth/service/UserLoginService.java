package com.shop.oauth.service;

import com.shop.oauth.util.AuthToken;

import java.io.UnsupportedEncodingException;

public interface UserLoginService {
    AuthToken login(String username, String password, String clientId, String clientSecret, String grantType) throws UnsupportedEncodingException;
}
