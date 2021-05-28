package com.hoya.service.dao;

import com.hoya.service.model.User;

public interface UserMapper {

    User getUser(Integer id);

    int insert(User user);

    int update(User user);

    int delete(Integer id);

}
