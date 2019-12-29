package com.wzp.module.user.service.impl;

import com.wzp.module.user.bean.User;
import com.wzp.module.user.mapper.UserMapper;
import com.wzp.module.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Transactional
    @Override
    public void create(User user) throws Exception {
        this.userMapper.create(user);
    }
}
