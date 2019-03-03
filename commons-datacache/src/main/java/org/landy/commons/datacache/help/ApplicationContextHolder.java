package org.landy.commons.datacache.help;

import org.springframework.context.ApplicationContext;

public class ApplicationContextHolder {
    private static ApplicationContext applicationContext;

    private ApplicationContextHolder(){}

    public static void setApplicationContext(ApplicationContext context){
        applicationContext = context;
    }

    public static ApplicationContext accessApplicationContext() {
        return applicationContext;
    }
}
