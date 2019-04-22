package org.landy.commons.web.springmvc.conf;

import org.landy.commons.core.help.AbstractApplicationContextHelper;
import org.landy.commons.core.help.BeanInitializeCompletedListener;
import org.landy.commons.datacache.DataCacheFacade;
import org.landy.commons.web.conf.ApplicationContextConfiguration;
import org.landy.commons.web.springmvc.load.ControllerConfigurationLoader;
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
@ComponentScan("org.landy.commons")
@Import({ApplicationContextConfiguration.class,  // common Application context configuration
        })
public class BeanInitializeCompletedConfiguration {
    /**
     * 缓存门面
     */
    @Autowired
    DataCacheFacade dataCacheFacade;

    /**
     * Controller配置加载器
     */
    @Autowired
    ControllerConfigurationLoader controllerConfigurationLoader;

    @Bean
    public BeanInitializeCompletedListener beanInitializeCompletedListener() {
        BeanInitializeCompletedListener beanInitializeCompletedListener = new BeanInitializeCompletedListener();
        List<AbstractApplicationContextHelper> initSysHelperBeanList = new ArrayList<>();
        initSysHelperBeanList.add(dataCacheFacade);
        initSysHelperBeanList.add(controllerConfigurationLoader);
        beanInitializeCompletedListener.setInitSysHelperBeanList(initSysHelperBeanList);
        return beanInitializeCompletedListener;
    }

}
