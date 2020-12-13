package com.shop.user.service;

import com.github.pagehelper.PageInfo;
import com.shop.user.pojo.OauthClientDetails;

import java.util.List;

public interface OauthClientDetailsService {

    PageInfo<OauthClientDetails> findPage(OauthClientDetails oauthClientDetails, int page, int size);

    PageInfo<OauthClientDetails> findPage(int page, int size);

    List<OauthClientDetails> findList(OauthClientDetails oauthClientDetails);

    void delete(String id);

    void update(OauthClientDetails oauthClientDetails);

    void add(OauthClientDetails oauthClientDetails);

     OauthClientDetails findById(String id);

    List<OauthClientDetails> findAll();
}
