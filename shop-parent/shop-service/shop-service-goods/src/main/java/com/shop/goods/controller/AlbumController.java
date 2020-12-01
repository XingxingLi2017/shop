package com.shop.goods.controller;

import com.github.pagehelper.Page;
import com.shop.entity.PageResult;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.goods.pojo.Album;
import com.shop.goods.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public Result findAll() {
        List<Album> list = albumService.findAll();
        return new Result(true, StatusCode.OK, "Get albums successfully.", list);
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        Album album = albumService.findById(id);
        return new Result(true, StatusCode.OK, "Get album successfully.", album);
    }

    @PostMapping
    public Result add(@RequestBody Album album) {
        albumService.add(album);
        return new Result(true, StatusCode.OK, "Add album successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Album album , @PathVariable Long id) {
        album.setId(id);
        albumService.update(album);
        return new Result(true, StatusCode.OK, "Update album successfully.");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        albumService.delete(id);
        return new Result(true, StatusCode.OK, "Delete album successfully.");
    }

    @GetMapping("/search")
    public Result findList(@RequestParam Map searchMap) {
        List<Album> list = albumService.findList(searchMap);
        return new Result(true, StatusCode.OK, "Get albums successfully.", list);
    }

    @GetMapping("/search/{page}/{size}")
    public Result findList(@RequestParam Map searchMap , @PathVariable Integer page, @PathVariable Integer size) {
        Page<Album> list = albumService.findPage(searchMap, page, size);
        PageResult<Album> pageResult = new PageResult<>(list.getTotal() , list.getResult());
        return new Result(true, StatusCode.OK, "Get albums successfully." , pageResult);
    }
}
