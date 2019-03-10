package org.landy.commons.core.help;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Iterator;
import java.util.List;

public class BeanInitializeCompletedListener implements ApplicationListener<ContextRefreshedEvent> {
    protected static Logger logger = LoggerFactory.getLogger(BeanInitializeCompletedListener.class);
    private List<AbstractApplicationContextHelper> initSysHelperBeanList;

    public BeanInitializeCompletedListener() {
    }

    public void initializeCompleted(ApplicationContext appContext) {
        if (this.initSysHelperBeanList != null) {
            Iterator i$ = this.initSysHelperBeanList.iterator();

            while(i$.hasNext()) {
                AbstractApplicationContextHelper helper = (AbstractApplicationContextHelper)i$.next();
                helper.init();
            }
        }

    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            logger.info("spring conf 容器初始化完毕..处理启动之后事件--start");
            this.initializeCompleted(event.getApplicationContext());
            logger.info("spring conf 容器初始化完毕..处理启动之后事件--end");
        }
    }

    public List<AbstractApplicationContextHelper> getInitSysHelperBeanList() {
        return this.initSysHelperBeanList;
    }

    public void setInitSysHelperBeanList(List<AbstractApplicationContextHelper> initSysHelperBeanList) {
        this.initSysHelperBeanList = initSysHelperBeanList;
    }
}
