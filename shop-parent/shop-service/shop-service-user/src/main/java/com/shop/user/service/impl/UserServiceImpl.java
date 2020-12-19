package com.shop.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.user.dao.UserMapper;
import com.shop.user.pojo.User;
import com.shop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public PageInfo<User> findPage(User user, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(user);
        return new PageInfo<User>(userMapper.selectByExample(example));
    }

    @Override
    public PageInfo<User> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<User>(userMapper.selectAll());
    }

    @Override
    public List<User> findList(User user){
        Example example = createExample(user);
        return userMapper.selectByExample(example);
    }

    public Example createExample(User user){
        Example example=new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if(user!=null){
            if(!StringUtils.isEmpty(user.getUsername())){
                    criteria.andLike("username","%"+user.getUsername()+"%");
            }
            if(!StringUtils.isEmpty(user.getPassword())){
                    criteria.andEqualTo("password",user.getPassword());
            }
            if(!StringUtils.isEmpty(user.getPhone())){
                    criteria.andEqualTo("phone",user.getPhone());
            }
            if(!StringUtils.isEmpty(user.getEmail())){
                    criteria.andEqualTo("email",user.getEmail());
            }
            if(!StringUtils.isEmpty(user.getCreated())){
                    criteria.andEqualTo("created",user.getCreated());
            }
            if(!StringUtils.isEmpty(user.getUpdated())){
                    criteria.andEqualTo("updated",user.getUpdated());
            }
            if(!StringUtils.isEmpty(user.getSourceType())){
                    criteria.andEqualTo("sourceType",user.getSourceType());
            }
            if(!StringUtils.isEmpty(user.getNickName())){
                    criteria.andEqualTo("nickName",user.getNickName());
            }
            if(!StringUtils.isEmpty(user.getName())){
                    criteria.andLike("name","%"+user.getName()+"%");
            }
            if(!StringUtils.isEmpty(user.getStatus())){
                    criteria.andEqualTo("status",user.getStatus());
            }
            if(!StringUtils.isEmpty(user.getHeadPic())){
                    criteria.andEqualTo("headPic",user.getHeadPic());
            }
            if(!StringUtils.isEmpty(user.getQq())){
                    criteria.andEqualTo("qq",user.getQq());
            }
            if(!StringUtils.isEmpty(user.getIsMobileCheck())){
                    criteria.andEqualTo("isMobileCheck",user.getIsMobileCheck());
            }
            if(!StringUtils.isEmpty(user.getIsEmailCheck())){
                    criteria.andEqualTo("isEmailCheck",user.getIsEmailCheck());
            }
            if(!StringUtils.isEmpty(user.getSex())){
                    criteria.andEqualTo("sex",user.getSex());
            }
            if(!StringUtils.isEmpty(user.getUserLevel())){
                    criteria.andEqualTo("userLevel",user.getUserLevel());
            }
            if(!StringUtils.isEmpty(user.getPoints())){
                    criteria.andEqualTo("points",user.getPoints());
            }
            if(!StringUtils.isEmpty(user.getExperienceValue())){
                    criteria.andEqualTo("experienceValue",user.getExperienceValue());
            }
            if(!StringUtils.isEmpty(user.getBirthday())){
                    criteria.andEqualTo("birthday",user.getBirthday());
            }
            if(!StringUtils.isEmpty(user.getLastLoginTime())){
                    criteria.andEqualTo("lastLoginTime",user.getLastLoginTime());
            }
        }
        return example;
    }

    @Override
    public void delete(String id){
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(User user){
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void add(User user){
        userMapper.insertSelective(user);
    }

    @Override
    public User findById(String id){
        return  userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public void addPoints(String username, Integer points) {
        userMapper.addPoints(username, points);
    }
}
