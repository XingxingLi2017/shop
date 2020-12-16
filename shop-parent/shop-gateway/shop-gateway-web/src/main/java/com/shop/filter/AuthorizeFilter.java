package com.shop.filter;


import com.shop.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;


/***
 * global filter for user authentication in Gateway
 * check request if has JWT token
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        
        // get token from params, cookies, headers
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // check headers
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        // check params
        if(StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        // check cookies
        if(StringUtils.isEmpty(token)) {
            HttpCookie cookies = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(cookies != null) {
                token = cookies.getValue();
            }
        }

        // parse token
//        try {
//            JwtUtil.parseJWT(token);
//            // put token into headers for authorization in Oauth 2.0
//            request.mutate().header(AUTHORIZE_TOKEN, token);
//
//            return chain.filter(exchange);
//        } catch (Exception e) {
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }

        // we don't need to verify JWT token anymore, oauth2 will complete the verification
        if(StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        } else {
            if(!token.startsWith("bearer ") && !token.startsWith("Bearer ")) {
                token = "bearer " + token;
            }

            // put the token in headers for oauth2 resource server
            final String finalToken = token;
            request.mutate().headers(httpHeaders -> httpHeaders.set(AUTHORIZE_TOKEN, finalToken));

            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
