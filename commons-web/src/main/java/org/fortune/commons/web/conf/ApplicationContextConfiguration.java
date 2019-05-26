package org.fortune.commons.web.conf;

import org.fortune.commons.core.conf.BeanCopierConfiguration;
import org.fortune.commons.core.conf.SettingsConfiguration;
import org.fortune.commons.datacache.conf.DataCacheConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Application Context上下文基本配置
 */
@Configuration
@ComponentScan("org.fortune.commons")
@Import({SettingsConfiguration.class,  // common settings configuration
        DataCacheConfiguration.class,  // common data cache component configuration
        BeanCopierConfiguration.class // Bean copier component configuration
        })
public class ApplicationContextConfiguration {

}
