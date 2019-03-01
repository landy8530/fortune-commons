package org.landy.commons.datacache;

import org.landy.commons.datacache.conf.AbstractCacheConfig;
import org.landy.commons.datacache.conf.LocalConfig;
import org.landy.commons.datacache.conf.MemCachedConfig;
import org.landy.commons.datacache.conf.MongoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public class DataCacheConfig {

    private static Logger log = LoggerFactory.getLogger(DataCacheConfig.class);
    private AbstractCacheConfig dataCacheConfigBean;
    private String cacheStrategy = "local";
    public static final String DATA_CACHE_CONFIG_FILE_NAME = "datacache-conf";
    public static final String CACHE_STRATEGY_OF_LOCAL = "local";
    public static final String CACHE_STRATEGY_OF_MEMCACHED = "memcached";
    public static final String CACHE_STRATEGY_OF_MONGO = "mongo";

    public DataCacheConfig() {
        ResourceBundle resBund = ResourceBundle.getBundle("datacache-conf");
        log.info("******************解析缓存配置参数******************");
        String val = resBund.getString("cacheStrategy");
        this.cacheStrategy = val;
        if ("local".equalsIgnoreCase(val)) {
            log.info("配置缓存策略：本地缓存，" + this.cacheStrategy);
            this.dataCacheConfigBean = new LocalConfig();
        } else if ("memcached".equalsIgnoreCase(val)) {
            log.info("配置缓存策略：Memcached缓存" + this.cacheStrategy);
            log.info("创建MemCachedClient");
            this.dataCacheConfigBean = new MemCachedConfig();
        } else if ("mongo".equalsIgnoreCase(val)) {
            log.info("配置缓存策略：Mongo缓存" + this.cacheStrategy);
            log.info("创建Mongo");
            this.dataCacheConfigBean = new MongoConfig();
        } else {
            log.info("未配置缓存策略，将采用本地缓存" + this.cacheStrategy);
        }

        this.dataCacheConfigBean.parseConfig(resBund);
    }

    public String getCacheStrategy() {
        return this.cacheStrategy;
    }

    public void setCacheStrategy(String cacheStrategy) {
        this.cacheStrategy = cacheStrategy;
    }

    public AbstractCacheConfig getDataCacheConfigBean() {
        return this.dataCacheConfigBean;
    }

    public boolean isLocalCache() {
        return "local".equalsIgnoreCase(this.cacheStrategy);
    }

    public boolean isMemcachedCache() {
        return "memcached".equalsIgnoreCase(this.cacheStrategy);
    }

    public boolean isMongoCache() {
        return "mongo".equalsIgnoreCase(this.cacheStrategy);
    }

}
