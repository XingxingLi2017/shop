package com.shop.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.user.dao.OauthClientDetailsMapper;
import com.shop.user.pojo.OauthClientDetails;
import com.shop.user.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OauthClientDetailsServiceImpl implements OauthClientDetailsService {

    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;


    @Override
    public PageInfo<OauthClientDetails> findPage(OauthClientDetails oauthClientDetails, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(oauthClientDetails);
        return new PageInfo<OauthClientDetails>(oauthClientDetailsMapper.selectByExample(example));
    }

    @Override
    public PageInfo<OauthClientDetails> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<OauthClientDetails>(oauthClientDetailsMapper.selectAll());
    }

    @Override
    public List<OauthClientDetails> findList(OauthClientDetails oauthClientDetails){
        Example example = createExample(oauthClientDetails);
        return oauthClientDetailsMapper.selectByExample(example);
    }

    public Example createExample(OauthClientDetails oauthClientDetails){
        Example example=new Example(OauthClientDetails.class);
        Example.Criteria criteria = example.createCriteria();
        if(oauthClientDetails!=null){
            if(!StringUtils.isEmpty(oauthClientDetails.getClientId())){
                    criteria.andEqualTo("clientId",oauthClientDetails.getClientId());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getResourceIds())){
                    criteria.andEqualTo("resourceIds",oauthClientDetails.getResourceIds());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getClientSecret())){
                    criteria.andEqualTo("clientSecret",oauthClientDetails.getClientSecret());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getScope())){
                    criteria.andEqualTo("scope",oauthClientDetails.getScope());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getAuthorizedGrantTypes())){
                    criteria.andEqualTo("authorizedGrantTypes",oauthClientDetails.getAuthorizedGrantTypes());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getWebServerRedirectUri())){
                    criteria.andEqualTo("webServerRedirectUri",oauthClientDetails.getWebServerRedirectUri());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getAuthorities())){
                    criteria.andEqualTo("authorities",oauthClientDetails.getAuthorities());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getAccessTokenValidity())){
                    criteria.andEqualTo("accessTokenValidity",oauthClientDetails.getAccessTokenValidity());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getRefreshTokenValidity())){
                    criteria.andEqualTo("refreshTokenValidity",oauthClientDetails.getRefreshTokenValidity());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getAdditionalInformation())){
                    criteria.andEqualTo("additionalInformation",oauthClientDetails.getAdditionalInformation());
            }
            if(!StringUtils.isEmpty(oauthClientDetails.getAutoapprove())){
                    criteria.andEqualTo("autoapprove",oauthClientDetails.getAutoapprove());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        oauthClientDetailsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OauthClientDetails oauthClientDetails){
        oauthClientDetailsMapper.updateByPrimaryKeySelective(oauthClientDetails);
    }

    @Override
    public void add(OauthClientDetails oauthClientDetails){
        oauthClientDetailsMapper.insertSelective(oauthClientDetails);
    }

    @Override
    public OauthClientDetails findById(String id){
        return  oauthClientDetailsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OauthClientDetails> findAll() {
        return oauthClientDetailsMapper.selectAll();
    }
}
