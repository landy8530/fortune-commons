package org.fortune.doc.server.conf.web;

import org.fortune.commons.core.conf.BeanCopierConfiguration;
import org.fortune.commons.core.conf.SettingsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Application Context上下文基本配置
 */
@Configuration
@ComponentScan("org.fortune.commons")
@Import({SettingsConfiguration.class,  // common settings configuration
        BeanCopierConfiguration.class // Bean copier component configuration
        })
public class ApplicationContextConfiguration {

}
