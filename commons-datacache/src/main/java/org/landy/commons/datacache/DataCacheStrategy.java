package org.landy.commons.datacache;

import org.springframework.util.StringUtils;

public enum DataCacheStrategy {

    LOCAL(DataCacheStrategy.CACHE_STRATEGY_OF_LOCAL),
    MEMCACHED(DataCacheStrategy.CACHE_STRATEGY_OF_MEMCACHED),
    MONGO(DataCacheStrategy.CACHE_STRATEGY_OF_MONGO),
    REDIS(DataCacheStrategy.CACHE_STRATEGY_OF_REDIS),

    ;

    static final String CACHE_STRATEGY_OF_LOCAL = "local";
    static final String CACHE_STRATEGY_OF_MEMCACHED = "memcached";
    static final String CACHE_STRATEGY_OF_MONGO = "mongodb";
    static final String CACHE_STRATEGY_OF_REDIS = "redis";

    private String strategy;

    DataCacheStrategy(String strategy) {
        this.strategy = strategy;
    }

    public static DataCacheStrategy fromStrategy(String strategy) {
        for(DataCacheStrategy dataCacheStrategy : DataCacheStrategy.values()) {
            if(dataCacheStrategy.strategy.equalsIgnoreCase(strategy)) {
                return dataCacheStrategy;
            }
        }
        return DataCacheStrategy.LOCAL;
    }

    public static boolean isMemCached(String strategy) {
        return MEMCACHED.strategy.equalsIgnoreCase(strategy);
    }

    public static boolean isLocal(String strategy) {
        if(StringUtils.isEmpty(strategy)) return true;
        return LOCAL.strategy.equalsIgnoreCase(strategy);
    }

    public static boolean isMongoDB(String strategy) {
        return MONGO.strategy.equalsIgnoreCase(strategy);
    }

    public static boolean isRedis(String strategy) {
        return REDIS.strategy.equalsIgnoreCase(strategy);
    }
}
