package com.shop.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.user.dao.AddressMapper;
import com.shop.user.pojo.Address;
import com.shop.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> list(String username) {
        Address addresss = new Address();
        addresss.setUsername(username);
        return  addressMapper.select(addresss);
    }

    @Override
    public PageInfo<Address> findPage(Address address, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(address);
        return new PageInfo<Address>(addressMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Address> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<Address>(addressMapper.selectAll());
    }

    @Override
    public List<Address> findList(Address address){
        Example example = createExample(address);
        return addressMapper.selectByExample(example);
    }

    public Example createExample(Address address){
        Example example=new Example(Address.class);
        Example.Criteria criteria = example.createCriteria();
        if(address!=null){
            if(!StringUtils.isEmpty(address.getId())){
                    criteria.andEqualTo("id",address.getId());
            }
            if(!StringUtils.isEmpty(address.getUsername())){
                    criteria.andLike("username","%"+address.getUsername()+"%");
            }
            if(!StringUtils.isEmpty(address.getProvinceid())){
                    criteria.andEqualTo("provinceid",address.getProvinceid());
            }
            if(!StringUtils.isEmpty(address.getCityid())){
                    criteria.andEqualTo("cityid",address.getCityid());
            }
            if(!StringUtils.isEmpty(address.getAreaid())){
                    criteria.andEqualTo("areaid",address.getAreaid());
            }
            if(!StringUtils.isEmpty(address.getPhone())){
                    criteria.andEqualTo("phone",address.getPhone());
            }
            if(!StringUtils.isEmpty(address.getAddress())){
                    criteria.andEqualTo("address",address.getAddress());
            }
            if(!StringUtils.isEmpty(address.getContact())){
                    criteria.andEqualTo("contact",address.getContact());
            }
            if(!StringUtils.isEmpty(address.getIsDefault())){
                    criteria.andEqualTo("isDefault",address.getIsDefault());
            }
            if(!StringUtils.isEmpty(address.getAlias())){
                    criteria.andEqualTo("alias",address.getAlias());
            }
        }
        return example;
    }

    @Override
    public void delete(Integer id){
        addressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Address address){
        addressMapper.updateByPrimaryKeySelective(address);
    }

    @Override
    public void add(Address address){
        addressMapper.insertSelective(address);
    }

    @Override
    public Address findById(Integer id){
        return  addressMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Address> findAll() {
        return addressMapper.selectAll();
    }
}
