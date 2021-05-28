package com.hoya.core.test;

import com.alibaba.fastjson.JSONObject;
import com.hoya.core.controller.EnableGlobalResultResponseController;
import com.hoya.core.controller.TestModel;
import com.hoya.core.exception.HttpResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResultResponseTests extends BaseTests {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    public ResultResponseTests() {
        super(ResultResponseTests.class);
    }

    @Override
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Override
    public void done() {
        System.out.println("done");
    }

    @Test
    public void testRight() throws Exception {
        RequestBuilder request = get(URL_VOID)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andReturn();
        String res = result.getResponse().getContentAsString();
        JSONObject val = JSONObject.parseObject(res);
        Assert.assertEquals(val.getLongValue("code"), 2000l);

        request = get(URL_INTEGER)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);
        result = mockMvc.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andReturn();
        res = result.getResponse().getContentAsString();
        HttpResult httpResult = JSONObject.parseObject(res, HttpResult.class);
        Assert.assertEquals(httpResult.getCode().longValue(), 2000);
        Assert.assertEquals((Integer) httpResult.getData(), EnableGlobalResultResponseController.STANDARD_INT);

        request = get(URL_STRING)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);
        result = mockMvc.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andReturn();
        res = result.getResponse().getContentAsString();
        httpResult = JSONObject.parseObject(res, HttpResult.class);
        Assert.assertEquals(httpResult.getCode().longValue(), 2000);
//        Assert.assertEquals((String) httpResult.getData(), EnableGlobalResultResponseController.STANDARD_STRING);

        request = get(URL_OBJECT)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);
        result = mockMvc.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andReturn();
        res = result.getResponse().getContentAsString();
        httpResult = JSONObject.parseObject(res, HttpResult.class);
        Assert.assertEquals(httpResult.getCode().longValue(), 2000);
        TestModel testModel = ((JSONObject) httpResult.getData()).toJavaObject(TestModel.class);
        Assert.assertEquals(testModel, EnableGlobalResultResponseController.STANDARD_OBJECT);

    }

    final private String URL_VOID = "/void";
    final private String URL_INTEGER = "/int";
    final private String URL_STRING = "/string";
    final private String URL_OBJECT = "/object";
}
