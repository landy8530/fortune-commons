package org.landy.commons.datacache.help;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

public class ApplicationContextHelper extends AbstractApplicationContextHelper {
    private Logger logger = LoggerFactory.getLogger(ApplicationContextHelper.class);
    private static ApplicationContextHelper instance;

    private static ApplicationContextHelper factoryMethod() {
        Logger logger = LoggerFactory.getLogger(ApplicationContextHelper.class);
        logger.info("初始化----start");
        instance = new ApplicationContextHelper();
        logger.info("初始化----end");
        return instance;
    }

    public static ApplicationContextHelper getInstance() {
        return instance;
    }

    private ApplicationContextHelper() {
    }

    public void init() {
    }

    public Object getBean(String beanName) {
        return super.accessApplicationContext().getBean(beanName);
    }

    public void reload() {
        ((AbstractApplicationContext)super.accessApplicationContext()).close();
        ((AbstractApplicationContext)super.accessApplicationContext()).refresh();
    }

    public void autowireComponent(Object bean) {
        ((AbstractApplicationContext)super.accessApplicationContext()).getBeanFactory().autowireBeanProperties(bean, 1, false);
    }

    public void close() {
        ((AbstractApplicationContext)super.accessApplicationContext()).close();
    }

}
