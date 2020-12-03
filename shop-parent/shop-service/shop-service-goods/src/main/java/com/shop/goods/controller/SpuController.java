package com.shop.goods.controller;

import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Goods;
import com.shop.goods.pojo.Spu;
import com.shop.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController {

    @Autowired
    private SpuService spuService;

    @PutMapping("/restore/{id}")
    public Result restore(@PathVariable("id") String id){
        spuService.restore(id);
        return new Result(true,StatusCode.OK,"Restore successfully.");
    }

    @DeleteMapping("/realDelete/{id}")
    public Result realDel(@PathVariable("id") String id){
        spuService.realDelete(id);
        return new Result(true,StatusCode.OK,"Delete successfully.");
    }

    @PutMapping("/put/many")
    public Result putMany(@RequestBody String[] spuIds){
        spuService.putMany(spuIds);
        return new Result(true, StatusCode.OK, "Put goods successfully.");
    }

    @PutMapping("/put/{id}")
    public Result put(@PathVariable("id") String spuId){
        spuService.put(spuId);
        return new Result(true, StatusCode.OK, "Put goods successfully.");
    }

    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable("id") String spuId){
        spuService.pull(spuId);
        return new Result(true, StatusCode.OK, "Pull successfully.");
    }

    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable("id") String spuId){
        spuService.audit(spuId);
        return new Result(true, StatusCode.OK, "Pass the audit.");
    }

    /***
     * get goods( sku list + spu ) by spu id
     */
    @GetMapping("/goods/{id}")
    public Result findGoodsById(@PathVariable("id") String spuId) {
        Goods goods = spuService.findGoodsById(spuId);
        return new Result(true, StatusCode.OK, "Get goods info successfully." , goods);
    }

    /****
     * update and add goods info sku list + spu
     * @param goods
     * @return
     */
    @PostMapping("/save")
    public Result saveGoods(@RequestBody Goods goods) {
        spuService.saveGoods(goods);
        return new Result(true, StatusCode.OK, "Add goods info successfully.");
    }

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody Spu spu, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new Result(true, StatusCode.OK,"Get goods info successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable int page, @PathVariable  int size){
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get goods info successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Spu>> findList(@RequestBody Spu spu){
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true,StatusCode.OK,"Get goods info successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        spuService.delete(id);
        return new Result(true,StatusCode.OK,"delete successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Goods goods,@PathVariable String id){
        goods.getSpu().setId(id);
        spuService.update(goods);
        return new Result(true,StatusCode.OK,"Update goods info successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Spu spu){
        spuService.add(spu);
        return new Result(true,StatusCode.OK,"Add successfully.");
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        Goods goods = spuService.findGoodsById(id);
        return new Result(true,StatusCode.OK,"Get goods info successfully.",goods);
    }

    @GetMapping
    public Result<List<Spu>> findAll(){
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK,"Get goods info successfully.",list) ;
    }
}
