package org.fortune.doc.server.conf.web;

import org.fortune.commons.core.help.AbstractApplicationContextHelper;
import org.fortune.commons.core.help.BeanInitializeCompletedListener;
import org.fortune.doc.server.DocServer;
import org.fortune.doc.server.DocServerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

/**
 * spring 容器初始化完毕,处理启动之后事件配置管理
 * @author landyl
 * @date 2019/4/22 10:28
 */
@Configuration
@ComponentScan("org.fortune.doc")
@Import({ApplicationContextConfiguration.class,  // common Application context configuration
        })
public class BeanInitializeCompletedConfiguration {

    @Autowired
    DocServerContainer docServerContainer;

    @Autowired
    DocServer docServer;

    @Bean
    public BeanInitializeCompletedListener beanInitializeCompletedListener() {
        BeanInitializeCompletedListener beanInitializeCompletedListener = new BeanInitializeCompletedListener();
        List<AbstractApplicationContextHelper> initSysHelperBeanList = new ArrayList<>();
        initSysHelperBeanList.add(docServerContainer);
        initSysHelperBeanList.add(docServer);
        beanInitializeCompletedListener.setInitSysHelperBeanList(initSysHelperBeanList);
        return beanInitializeCompletedListener;
    }

}
