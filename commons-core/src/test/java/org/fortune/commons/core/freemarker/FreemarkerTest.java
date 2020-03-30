package org.fortune.commons.core.freemarker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: landy
 * @date: 2020/3/25 09:07
 * @description:
 */
public class FreemarkerTest {

    public static void main(String[] args) {
        FortuneFreemarkerResolver resolver = FortuneFreemarkerResolver.getInstance();
        //设置根路径
//        String classPath = FileUtil.getFilePathByClassPath("/");
//        resolver.setRootPath(classPath + "/template/");
//        resolver.init(); //初始化FTL解析组件

        FtlResourceData resourceData = new FtlResourceData();
        resourceData.setFtlTemplatePath("test.ftl");

        Map<String,Object> data = new HashMap<>();

        data.put("data", "Hello world!");

        resourceData.setData(data);

        String msg = resolver.process(resourceData);

        System.out.println(msg);
    }

}
