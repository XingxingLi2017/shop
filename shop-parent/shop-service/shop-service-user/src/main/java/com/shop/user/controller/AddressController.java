package com.shop.user.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.user.pojo.Address;
import com.shop.user.service.AddressService;
import com.shop.util.TokenDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/user/list")
    public Result<List<Address>> list(){
        Map<String, String> userInfo = TokenDecoder.getUserInfo();
        List<Address> list = addressService.list(userInfo.get("username"));
        return new Result(true, StatusCode.OK, "Get user addresses successfully." , list);
    }

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Address address, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Address> pageInfo = addressService.findPage(address, page, size);
        return new Result(true,StatusCode.OK,"Get Address successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Address> pageInfo = addressService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get Address successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Address>> findList(@RequestBody(required = false)  Address address){
        List<Address> list = addressService.findList(address);
        return new Result<List<Address>>(true,StatusCode.OK,"Get Address successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable Integer id){
        addressService.delete(id);
        return new Result(true,StatusCode.OK,"Delete Address successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  Address address,@PathVariable Integer id){
        address.setId(id);
        addressService.update(address);
        return new Result(true,StatusCode.OK,"Update Address successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Address address){
        addressService.add(address);
        return new Result(true,StatusCode.OK,"Add Address successfully.");
    }

    @GetMapping("/{id}")
    public Result<Address> findById(@PathVariable Integer id){
        Address address = addressService.findById(id);
        return new Result<Address>(true,StatusCode.OK,"Get Address successfully.",address);
    }

    @GetMapping
    public Result<List<Address>> findAll(){
        List<Address> list = addressService.findAll();
        return new Result<List<Address>>(true, StatusCode.OK,"Get Address successfully.",list) ;
    }
}
