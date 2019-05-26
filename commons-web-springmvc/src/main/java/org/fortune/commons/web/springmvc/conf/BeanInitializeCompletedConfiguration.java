package org.fortune.commons.web.springmvc.conf;

import org.fortune.commons.core.help.AbstractApplicationContextHelper;
import org.fortune.commons.core.help.BeanInitializeCompletedListener;
import org.fortune.commons.datacache.DataCacheFacade;
import org.fortune.commons.web.conf.ApplicationContextConfiguration;
import org.fortune.commons.web.springmvc.load.ControllerConfigurationLoader;
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
@ComponentScan("org.fortune.commons")
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
