package com.life.hz.service;

import com.life.hz.mapper.UserMapper;
import com.life.hz.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
       User dbuser = userMapper.findByAccountId(user.getAccountId());
       if(dbuser==null){
           //插入
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           userMapper.insert(user);
       }else {
           //更新
           dbuser.setGmtModified(System.currentTimeMillis());
           dbuser.setToken(user.getToken());
           dbuser.setBio(user.getBio());
           dbuser.setAvatarUrl(user.getAvatarUrl());
           dbuser.setName(user.getName());
           userMapper.update(dbuser);

       }
    }
}
