package com.shop.user.service;

import com.github.pagehelper.PageInfo;
import com.shop.user.pojo.Address;

import java.util.List;

public interface AddressService {

    List<Address> list(String username);

    PageInfo<Address> findPage(Address address, int page, int size);

    PageInfo<Address> findPage(int page, int size);

    List<Address> findList(Address address);

    void delete(Integer id);

    void update(Address address);

    void add(Address address);

     Address findById(Integer id);

    List<Address> findAll();
}
