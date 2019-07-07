package org.fortune.doc.client;

import org.fortune.commons.core.conf.ApplicationContextHelperConfiguration;
import org.fortune.commons.core.conf.SettingsConfiguration;
import org.fortune.doc.client.conf.DocClientConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/5/30 23:37
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class) //调用Spring单元测试类
@ContextConfiguration(classes = {
        SettingsConfiguration.class,  // common settings configuration
        DocClientConfiguration.class,
        ApplicationContextHelperConfiguration.class
}) //加载Spring配置文件
public class DocClientTest {

    @Test
    public void test() {
        //DocClient.doImageUpload("psb.jpg", new File("C:\\06_temp\\psb.jpg"));
        //DocClient.doImageDelete("images\\fortune\\b\\190707114835_9573.jpg");
        DocClient.doImageReplace("images\\fortune\\f\\\\190707114616_3742.JPG", new File("C:\\06_temp\\psb.jpg"));
    }

    @Test
    public void docServerUrl() {
        System.out.println(DocClientContainer.getInstance().getDocServerUrl());
    }

}
