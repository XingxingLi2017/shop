package com.shop.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/****
 * main configuration class for spring security
 */
@Configuration
@EnableWebSecurity
@Order(-1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /****
     * ignore paths , security:http securoty="none" pattern="/user/login"
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/user/login",
                "/user/logout",
                "/oauth/login",
                "/css/**",
                "/data/**",
                "/fonts/**",
                "/img/**",
                "/js/**");
    }

    /****
     * config security:http tag properties
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic()        // use [Authorization: Basic code] header
                .and()
                .formLogin()       // enable form login
//                .loginPage("/oauth/login") // specify customized login page
//                .loginProcessingUrl("/user/login") // specify customized login controller
                .and()
                .authorizeRequests()    // start config for requests authorization
                .anyRequest()
                .authenticated();       // access="isAuthenticated()"

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    /****
     * get authentication manager
     *
     *     <security:authentication-manager>
     *         <security:authentication-provider user-service-ref="springSecurityUserService">
     *             <security:password-encoder ref="passwordEncoder"/>
     *         </security:authentication-provider>
     *     </security:authentication-manager>
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /****
     * inject BCrypt bean for authentication manager
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
