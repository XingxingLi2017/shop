package com.shop.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenDecoder {

    private static String publicKey;

    /***
     * get user info
     * @return
     */
    public static Map<String, String> getUserInfo(String pubKeyFileName) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return decodeToken(details.getTokenValue() , pubKeyFileName);
    }

    /***
     * decode JWT token with RSA algorithm
     * @param tokenValue
     * @param pubKeyFileName
     * @return
     */
    public static Map<String, String> decodeToken(String tokenValue , String pubKeyFileName) {
        Jwt jwt = JwtHelper.decodeAndVerify(tokenValue, new RsaVerifier(getPublicKey(pubKeyFileName)));
        String claims = jwt.getClaims();
        return JSON.parseObject(claims, Map.class);
    }

    /***
     * get public key in class path
     * @param pubKeyFileName
     * @return
     */
    public static String getPublicKey(String pubKeyFileName) {
        if(!StringUtils.isEmpty(publicKey)) return publicKey;

        Resource resource = new ClassPathResource(pubKeyFileName);

        try(InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(reader)) {
            publicKey = br.lines().collect(Collectors.joining("\n"));
            return publicKey;
        } catch (IOException e) {
            return null;
        }
    }
}
