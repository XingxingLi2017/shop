package com.shop.oauth.config;

import com.shop.oauth.util.UserJwt;
import com.shop.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/***
 * customized UserDetail class for SpringSecurity , query user from DB by username
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    UserFeign userFeign;

    /****
     * get user info from DB
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // if not authenticated , used in authorization_code mode
        if(authentication == null) {

            // use http basic authentication , Authorization: BASIC Base64[client_id:client_secret]
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);

            if(clientDetails != null) {
                String clientSecret = clientDetails.getClientSecret();

                // static
                return new User(username,new BCryptPasswordEncoder().encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));

                // dynamically get client info from DB
//                return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }

        // when authenticated by password
        if(StringUtils.isEmpty(username)) {
            return null;
        }

        String pwd = new BCryptPasswordEncoder().encode("myshop");

        // get User by username
//        String pwd = userFeign.findByUsername(username).getData().getPassword();

        // authorize to user
        String permissions = "ROLE_USER";
        if(username.equals("xing")) {
            permissions +=",ROLE_ADMIN";
        }

        UserJwt userDetails = new UserJwt(username,pwd,AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

        return userDetails;

    }
}
