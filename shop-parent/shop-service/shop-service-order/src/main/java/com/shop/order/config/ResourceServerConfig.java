package com.shop.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/***
 * configuration class for spring security oauth2 resource server
 * identify JWT token use public key
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)    // activate @PreAuthorize annotation of SpringSecurity
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // file name of public key
    private static final String PUBLIC_KEY = "public.key";

    private String getPublicKey(){
        ClassPathResource resource = new ClassPathResource(PUBLIC_KEY);
        try(
                InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                BufferedReader br = new BufferedReader(reader)){
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return null;
        }
    }

    /***
     * create JWT token converter with RSA signature verifier
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        String publicKey = getPublicKey();

        // create RSA verifier with public key string
        converter.setVerifier(new RsaVerifier(publicKey));

        return converter;
    }

    /***
     * create JWT token store
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }


    /***
     * intercept all http urls and check if they have JWT tokens
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()                                    // start authorization config for requests
//                .antMatchers("/sku/*").permitAll()    // don't intercept user registration request
                .anyRequest().authenticated();                     // intercept all the other request
    }
}
