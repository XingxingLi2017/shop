package com.shop.oauth.service.impl;


import com.shop.oauth.service.UserLoginService;
import com.shop.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

@Service
public class UserLoginServiceImpl implements UserLoginService {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /***
     * get token from oauth2 server endpoint /oauth/token
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret, String grantType) throws UnsupportedEncodingException {

        // String url = "http://localhost:9001/oauth/token";
        ServiceInstance userAuthService = loadBalancerClient.choose("user-auth");
        String url = userAuthService.getUri()+"/oauth/token";

        // form params
        MultiValueMap<String, String> formMap = new LinkedMultiValueMap<String, String>();
        formMap.add("username", username);
        formMap.add("password", password);
        formMap.add("grant_type", grantType);

        // Authorization header
        String authorization = "Basic " + new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes()) , "UTF-8");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization" , authorization);

        // encapsulate headers and params
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<MultiValueMap>(formMap, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map<String, String> body = responseEntity.getBody();
        AuthToken token = new AuthToken();
        token.setAccessToken(body.get("access_token"));
        token.setRefreshToken(body.get("refresh_token"));
        token.setJti(body.get("jti"));
        return token;
    }
}
