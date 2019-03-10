package org.landy.commons.datacache.conf;

import org.springframework.context.annotation.Configuration;

@Configuration(LocalConfig.BEAN_NAME)
public class LocalConfig extends AbstractCacheConfig {
    public static final String BEAN_NAME = "localConfig";

    public LocalConfig() {
    }

}
