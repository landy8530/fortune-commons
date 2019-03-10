package org.landy.commons.datacache.conf;

import org.landy.commons.core.help.AbstractApplicationContextHelper;
import org.landy.commons.core.help.BeanInitializeCompletedListener;
import org.landy.commons.datacache.DataCacheFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan("org.landy.commons")
@Import({LocalConfig.class,MemCachedConfig.class,MongoConfig.class})
public class ApplicationContextConfiguration {
    @Autowired
    DataCacheFacade dataCacheFacade;

    @Bean
    public BeanInitializeCompletedListener beanInitializeCompletedListener() {
        BeanInitializeCompletedListener beanInitializeCompletedListener = new BeanInitializeCompletedListener();
        List<AbstractApplicationContextHelper> initSysHelperBeanList = new ArrayList<>();
        initSysHelperBeanList.add(dataCacheFacade);
        beanInitializeCompletedListener.setInitSysHelperBeanList(initSysHelperBeanList);
        return beanInitializeCompletedListener;
    }

}
