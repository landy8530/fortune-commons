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
        DocClient.uploadFile(new File("C:\\06_temp\\psb.jpg"), "psb.jpg",false);
//		DocClient.uploadFile(new File("D:\\tmp\\FUp_378131942802165004.pdf"), "FUp_378131942802165004.pdf",false);
//        DocClient.replaceFile(new File("C:\\06_temp\\psb.jpg"), "yt\\k\\171105144056_7470.jpg");
//        DocClient.deleteFile("yt\\k\\171105144056_7470.pdf");
    }

}
