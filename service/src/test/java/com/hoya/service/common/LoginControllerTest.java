package com.hoya.service.common;

import com.alibaba.fastjson.JSON;
import com.hoya.service.BaseTests;
import com.hoya.service.vo.LoginVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginControllerTest extends BaseTests {

    private MockMvc mockMvc;

    private LoginVo loginVo;

    @Autowired
    private WebApplicationContext webApplicationContext;

    public LoginControllerTest() {
        super(LoginControllerTest.class);
    }

    @Override
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        loginVo = new LoginVo();
        loginVo.setMobile("13000000000");
        loginVo.setPassword("123456");
    }

    @Override
    public void done() {
        System.out.println("done");
    }

    @Test
    public void testRight() throws Exception {
        RequestBuilder request = post(URL_LOGIN)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(JSON.toJSONString(loginVo)).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andReturn();
    }

    final private String URL_LOGIN = "/login";
}
