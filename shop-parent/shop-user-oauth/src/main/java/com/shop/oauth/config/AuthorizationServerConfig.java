package com.shop.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;

/****
 * main config class for oauth2
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    // converter for JWTTokenStore
    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    // get userDetails(UserJwt)
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    CustomUserAuthenticationConverter customUserAuthenticationConverter;

    /***
     * client info config
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).clients(clientDetails());
//          clients.inMemory()
//                .withClient("shop")          // client id
//                .secret("shop")                      // client secret
//                .redirectUris("http://localhost")       // redirect uri
//                .accessTokenValiditySeconds(600)          //token duration
//                .refreshTokenValiditySeconds(600)         // refresh token duration
//                .authorizedGrantTypes(
//                        "authorization_code",
//                        "client_credentials",
//                        "refresh_token",                // enable refresh token
//                        "password")                     // grant_type = code + client_credentials + password
//                .scopes("app");                         // client scope , required , usually use app
    }

    /***
     * Get client info from DB , used in UserDetailService
     * @return
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /***
     * authorization server endpoints (/oauth/token, /oauth/authorize etc...) config
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.accessTokenConverter(jwtAccessTokenConverter) // convert UserJwt to LinkedHashMap
                .authenticationManager(authenticationManager) // SpringSecurity AM, used to get authorities and username
                .tokenStore(tokenStore)                       // JWTTokenStore with JwtAccessTokenConverter
                .userDetailsService(userDetailsService);     // customized userDetailService implementation
    }

    /***
     * authorization server security config, allow endpoints access
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients()         // allow getting username and password by form submission
                .passwordEncoder(new BCryptPasswordEncoder())  // BCrypt password encoder
                .tokenKeyAccess("permitAll()")                  // allow all access to url : /oauth/token_key
                .checkTokenAccess("isAuthenticated()");         // allow authenticated access to url : /oauth/check_token
    }

    @Bean
    @Autowired
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /***
     * config for reading RSA key pair
     * @return
     */
    @Bean("keyProp")
    public KeyProperties keyProperties(){
        return new KeyProperties();
    }

    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    /****
     * JWT token converter
     * @param customUserAuthenticationConverter
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(CustomUserAuthenticationConverter customUserAuthenticationConverter) {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // setup RSA key pair for converter
        KeyPair keyPair = new KeyStoreKeyFactory(
                    keyProperties.getKeyStore().getLocation(),                  // java key store location
                    keyProperties.getKeyStore().getSecret().toCharArray()       // java key store secret
                )
                .getKeyPair(
                            keyProperties.getKeyStore().getAlias(),                    // key store alias
                            keyProperties.getKeyStore().getPassword().toCharArray()    // key store password
                        );

        converter.setKeyPair(keyPair);

        //config UserTokenConverter as customUserAuthenticationConverter for JwtAccessTokenConverter
        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter);

        return converter;
    }
}
