package org.landy.commons.core.help;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractApplicationContextHelper implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

    public AbstractApplicationContextHelper() {
    }

    public abstract void init();

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextHolder.setApplicationContext(applicationContext);
    }

    public static ApplicationContext accessApplicationContext() {
        return ApplicationContextHolder.accessApplicationContext();
    }

    public static <T> T getBean(String beanName,Class<T> requiredType) {
        return accessApplicationContext().getBean(beanName,requiredType);
    }
}
