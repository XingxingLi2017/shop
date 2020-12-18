package com.shop.oauth.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/***
 * generate admin token for Feign invocation
 */

public class AdminTokenUtil {

    public static String createAdminToken(String keyStoreFile, String keyStorePassword, String keySecret, String keyAlias){
        ClassPathResource resource = new ClassPathResource(keyStoreFile);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource , keyStorePassword.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(keyAlias, keySecret.toCharArray());

        // get private key based on RSA algorithm
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("name" , "authorization server");
        // add authorities
        payLoad.put("authorities", new String[]{"ROLE_ADMIN", "OAUTH"});

        Jwt jwt = JwtHelper.encode(JSON.toJSONString(payLoad), new RsaSigner(privateKey));
        String token = jwt.getEncoded();
        return token;
    }

}
