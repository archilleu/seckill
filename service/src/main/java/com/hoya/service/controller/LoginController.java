package com.hoya.service.controller;

import com.alibaba.fastjson.JSON;
import com.hoya.service.server.MiaoShaUserService;
import com.hoya.service.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @PostMapping("/login")
    public void login(@RequestBody @Validated LoginVo loginVo, HttpServletResponse response) {
        log.info("登录开始 start! login:{}", JSON.toJSON(loginVo));
        miaoShaUserService.login(loginVo, response);
    }

}
