package com.shop.oauth.interceptor;

import com.shop.oauth.util.AdminTokenUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenRequestInterceptor implements RequestInterceptor {

    @Value("${encrypt.key-store.alias}")
    private String keyAlias;

    @Value("${encrypt.key-store.password}")
    private String keyStorePassword;

    @Value("${encrypt.key-store.secret}")
    private String keySecret;

    private static final String AUTHORIZE_TOKEN = "Authorization";

    /***
     * intercept before feign sending request
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // get admin token
        String token = AdminTokenUtil.createAdminToken(keyAlias+".jks", keyStorePassword, keySecret, keyAlias);

        requestTemplate.header(AUTHORIZE_TOKEN, "bearer " + token);

    }
}
