package org.landy.common.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.landy.commons.web.springmvc.conf.WebApplicationContextConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author: Landy
 * @date: 2019/4/16 22:28
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class) //调用Spring单元测试类
@WebAppConfiguration  //调用Java Web组件，如自动注入ServletContext Bean等
@ContextConfiguration(classes = {
        WebApplicationContextConfiguration.class
}) //加载Spring配置文件
public class ExportTest {

    @Test
    public void test() {

    }

}
