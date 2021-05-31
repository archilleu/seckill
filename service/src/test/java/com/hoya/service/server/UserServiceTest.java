package com.hoya.service.server;

import com.hoya.service.BaseTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends BaseTests {

    @Autowired
    UserService userService;

    public UserServiceTest() {
        super(UserServiceTest.class);
    }

    @Override
    public void init() {
    }

    @Override
    public void done() {
    }

    @Test
    public void test() {
        System.out.println("get user:" + userService.getUser(1));
    }

}
