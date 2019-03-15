package org.landy.commons.datacache.conf;

import org.landy.commons.core.help.AbstractApplicationContextHelper;
import org.landy.commons.core.help.BeanInitializeCompletedListener;
import org.landy.commons.datacache.DataCacheFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此，必须要使用@PropertySource注解导入需要预先导入的一些资源属性文件，比如在这里的一些前期配置：data.cache.common.cacheStrategy=memcached
 * 因为，org.springframework.context.annotation.ConfigurationClassParser#doProcessConfigurationClass(org.springframework.context.annotation.ConfigurationClass, org.springframework.context.annotation.ConfigurationClassParser.SourceClass)
 * 中可以清晰的看到，解析processPropertySource(propertySource);方法比this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN))
 * 这个条件化配置方法提前，所以如果没有用@PropertySource注解，则在相应的条件化注解的实现类中，则读取不到需要根据配置文件中动态配置的条件，如
 * @ConditionalOnProperty(value = "data.cache.strategy",havingValue = "memcached")
 */
@Configuration
@ComponentScan("org.landy.commons")
@PropertySource(value = "classpath:applicationContext.properties")
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
