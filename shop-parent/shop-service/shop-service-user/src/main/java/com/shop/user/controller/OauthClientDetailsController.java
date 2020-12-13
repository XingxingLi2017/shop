package com.shop.user.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.user.pojo.OauthClientDetails;
import com.shop.user.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/oauthClientDetails")
@CrossOrigin
public class OauthClientDetailsController {

    @Autowired
    private OauthClientDetailsService oauthClientDetailsService;

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  OauthClientDetails oauthClientDetails, @PathVariable  int page, @PathVariable  int size){
        PageInfo<OauthClientDetails> pageInfo = oauthClientDetailsService.findPage(oauthClientDetails, page, size);
        return new Result(true,StatusCode.OK,"Get OauthClientDetails successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<OauthClientDetails> pageInfo = oauthClientDetailsService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get OauthClientDetails successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<OauthClientDetails>> findList(@RequestBody(required = false)  OauthClientDetails oauthClientDetails){
        List<OauthClientDetails> list = oauthClientDetailsService.findList(oauthClientDetails);
        return new Result<List<OauthClientDetails>>(true,StatusCode.OK,"Get OauthClientDetails successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        oauthClientDetailsService.delete(id);
        return new Result(true,StatusCode.OK,"Delete OauthClientDetails successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  OauthClientDetails oauthClientDetails,@PathVariable String id){
        oauthClientDetails.setClientId(id);
        oauthClientDetailsService.update(oauthClientDetails);
        return new Result(true,StatusCode.OK,"Update OauthClientDetails successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   OauthClientDetails oauthClientDetails){
        oauthClientDetailsService.add(oauthClientDetails);
        return new Result(true,StatusCode.OK,"Add OauthClientDetails successfully.");
    }

    @GetMapping("/{id}")
    public Result<OauthClientDetails> findById(@PathVariable String id){
        OauthClientDetails oauthClientDetails = oauthClientDetailsService.findById(id);
        return new Result<OauthClientDetails>(true,StatusCode.OK,"Get OauthClientDetails successfully.",oauthClientDetails);
    }

    @GetMapping
    public Result<List<OauthClientDetails>> findAll(){
        List<OauthClientDetails> list = oauthClientDetailsService.findAll();
        return new Result<List<OauthClientDetails>>(true, StatusCode.OK,"Get OauthClientDetails successfully.",list) ;
    }
}
