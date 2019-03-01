package org.landy.commons.datacache.help;

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
    }

    public ApplicationContext accessApplicationContext() {
        return this.applicationContext;
    }

}
