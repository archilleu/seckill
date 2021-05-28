package com.hoya.core.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
abstract public class BaseTests {

    private Logger logger;

    private Class clazz;

    public BaseTests(Class clazz) {
        this.clazz = clazz;
        logger = LoggerFactory.getLogger(this.clazz);
    }

    abstract public void init();

    abstract public void done();

    @Before
    public void before() {
        logger.info(clazz.getName() + "开始测试-------------------------------------------------");
        this.init();
    }

    @After
    public void after() {
        this.done();
        logger.info(this.clazz.getName() + "结束测试-------------------------------------------------");
    }
}
