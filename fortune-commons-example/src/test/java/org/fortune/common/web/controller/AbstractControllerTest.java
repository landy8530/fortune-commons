package org.fortune.common.web.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.fortune.commons.web.springmvc.conf.RootApplicationContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author: Landy
 * @date: 2019/5/7 22:27
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class) //调用Spring单元测试类
@WebAppConfiguration  //调用Java Web组件，如自动注入ServletContext Bean等,即自动加载WebApplicationAutoInitializer类的初始化动作
@ContextConfiguration(classes = {
        RootApplicationContextConfiguration.class
}) //加载Spring配置文件
public abstract class AbstractControllerTest {

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    /**
     * 在Before注解的方法中，我们需要初始化spring特定的mock对象：MockMvc，spring mvc服务器端测试支持的主入口。
     */
    @Before
    public void setup() {
        //MockMvcBuilders 是访问特定MockMvcBuilder的实现接口的主要类。
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }

}
