package org.fortune.commons.core.conf;

import org.fortune.commons.core.help.ApplicationContextHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: landy
 * @date: 2019/6/14 21:00
 * @description:
 */
@Configuration
public class ApplicationContextHelperConfiguration {

    @Bean
    public ApplicationContextHelper applicationContextHelper() {
        return new ApplicationContextHelper();
    }

}
