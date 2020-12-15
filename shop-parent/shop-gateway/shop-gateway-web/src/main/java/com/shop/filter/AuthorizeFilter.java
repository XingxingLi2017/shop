package com.shop.filter;


import com.shop.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/***
 * global filter for user authentication
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        
        // get token from params, cookies, headers
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();

        if (path.startsWith("/api/user/login") || path.startsWith("/api/brand/search/")) {
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }


        // check headers
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        // true : token is in headers
        // false : token is not in headers
        boolean hasToken = true;

        // check params
        if(StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            hasToken = false;
        }

        // check cookies
        if(StringUtils.isEmpty(token)) {
            HttpCookie cookies = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(cookies != null) {
                token = cookies.getValue();
            }
        }

        // parse token
        try {
            JwtUtil.parseJWT(token);

            // put token into headers for authorization in Oauth 2.0
            request.mutate().header(AUTHORIZE_TOKEN, token);

            return chain.filter(exchange);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
