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

    @DeleteMapping("/realDel/{id}")
    public Result realDel(@PathVariable("id") String id){
        spuService.realDel(id);
        return new Result(true,StatusCode.OK,"Delete successfully.");
    }

    @PutMapping("/put/many")
    public Result putMany(@RequestBody Long[] spuIds){
        spuService.putMany(spuIds);
        return new Result(true, StatusCode.OK, "Put goods successfully.");
    }

    @PutMapping("/put/{id}")
    public Result put(@PathVariable("id") Long spuId){
        spuService.put(spuId);
        return new Result(true, StatusCode.OK, "Put goods successfully.");
    }

    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable("id") Long spuId){
        spuService.pull(spuId);
        return new Result(true, StatusCode.OK, "Pull successfully.");
    }

    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable("id") Long spuId){
        spuService.audit(spuId);
        return new Result(true, StatusCode.OK, "Pass the audit.");
    }

    /***
     * get goods( sku list + spu ) by spu id
     * @param id
     * @return
     */
    @GetMapping("/goods/{id}")
    public Result findGoodsById(@PathVariable Long id) {
        Goods goods = spuService.findGoodsById(id);
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
    public Result<PageInfo> findPage(@RequestBody(required = false)  Spu spu, @PathVariable  int page, @PathVariable  int size){
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new Result(true, StatusCode.OK,"Get goods info successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get goods info successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<Spu>> findList(@RequestBody(required = false)  Spu spu){
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true,StatusCode.OK,"Get goods info successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        spuService.delete(id);
        return new Result(true,StatusCode.OK,"delete successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  Spu spu,@PathVariable String id){
        spu.setId(id);
        spuService.update(spu);
        return new Result(true,StatusCode.OK,"Update successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   Spu spu){
        spuService.add(spu);
        return new Result(true,StatusCode.OK,"Add successfully.");
    }

    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable String id){
        Spu spu = spuService.findById(id);
        return new Result<Spu>(true,StatusCode.OK,"Get goods info successfully.",spu);
    }

    @GetMapping
    public Result<List<Spu>> findAll(){
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK,"Get goods info successfully.",list) ;
    }
}
