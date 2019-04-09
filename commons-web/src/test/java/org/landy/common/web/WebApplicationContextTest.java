package org.landy.common.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.landy.commons.core.conf.SettingsConfiguration;
import org.landy.commons.datacache.DataCacheFacade;
import org.landy.commons.datacache.conf.DataCacheConfiguration;
import org.landy.commons.web.conf.WebApplicationContextConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class) //调用Spring单元测试类
@WebAppConfiguration  //调用Java Web组件，如自动注入ServletContext Bean等
@ContextConfiguration(classes = {
        WebApplicationContextConfiguration.class
}) //加载Spring配置文件
public class WebApplicationContextTest {

    @Test
    public void test() {
        System.out.println("hello,world!");
    }

}
