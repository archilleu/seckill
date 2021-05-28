package com.hoya.service.server.impl;

import com.hoya.service.dao.UserMapper;
import com.hoya.service.model.User;
import com.hoya.service.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(Integer id) {
        return userMapper.getUser(id);
    }

}
