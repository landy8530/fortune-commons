package org.landy.commons.core.conf;

import org.landy.commons.core.cglib.BeanCopierContainer;
import org.landy.commons.core.setting.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanCopierConfiguration {

    private static final String BEAN_COPY_PATH_KEY = "model.package.path";

    @Autowired
    private Settings settings;

    private List<String> packagesToScanList;

    @Bean
    public BeanCopierContainer beanCopierContainer() {
        packagesToScanList = settings.getAsList(BEAN_COPY_PATH_KEY);
        BeanCopierContainer beanCopierContainer = new BeanCopierContainer();
        beanCopierContainer.setPackagesToScanList(packagesToScanList);
        beanCopierContainer.init();
        return beanCopierContainer;
    }
}
