package com.shop.user.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.user.pojo.User;
import com.shop.user.service.UserService;
import com.shop.util.IdWorker;
import com.shop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public Result login(String username, String password, HttpServletResponse response){
        User user = userService.findById(username);
        if(BCrypt.checkpw(password, user.getPassword())) {
            // create JWT token for client
            Map<String,Object> info = new HashMap<String,Object>();
            info.put("role","USER");
            info.put("success","SUCCESS");
            info.put("username",username);

            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(info), null);
            Cookie cookie = new Cookie("Authorization", token);

            // cookie can be shared by other web apps under the same domain
            cookie.setDomain("localhost");
            cookie.setPath("/");

            response.addCookie(cookie);

            return new Result(true, StatusCode.OK, "login successfully.", token);
        }
        return new Result(false, StatusCode.LOGINERROR, "Username or password is wrong");
    }

    @PostMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  User user, @PathVariable  int page, @PathVariable  int size){
        PageInfo<User> pageInfo = userService.findPage(user, page, size);
        return new Result(true,StatusCode.OK,"Get User successfully.",pageInfo);
    }

    @GetMapping("/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        PageInfo<User> pageInfo = userService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"Get User successfully.",pageInfo);
    }

    @PostMapping("/search" )
    public Result<List<User>> findList(@RequestBody(required = false)  User user){
        List<User> list = userService.findList(user);
        return new Result<List<User>>(true,StatusCode.OK,"Get User successfully.",list);
    }

    @DeleteMapping("/{id}" )
    public Result delete(@PathVariable String id){
        userService.delete(id);
        return new Result(true,StatusCode.OK,"Delete User successfully.");
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody  User user,@PathVariable String id){
        user.setUsername(id);
        userService.update(user);
        return new Result(true,StatusCode.OK,"Update User successfully.");
    }

    @PostMapping
    public Result add(@RequestBody   User user){
        userService.add(user);
        return new Result(true,StatusCode.OK,"Add User successfully.");
    }

    @GetMapping({"/{id}","/load/{id}"})
    public Result<User> findById(@PathVariable("id") String id){
        User user = userService.findById(id);
        return new Result<User>(true,StatusCode.OK,"Get User successfully.",user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public Result<List<User>> findAll(){
        List<User> list = userService.findAll();
        return new Result<List<User>>(true, StatusCode.OK,"Get User successfully.",list) ;
    }
}
