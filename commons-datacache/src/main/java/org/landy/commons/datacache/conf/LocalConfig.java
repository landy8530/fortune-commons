package org.landy.commons.datacache.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan("org.landy.commons.datacache")
public class LocalConfig extends AbstractCacheConfig {

    public LocalConfig() {
    }

}
