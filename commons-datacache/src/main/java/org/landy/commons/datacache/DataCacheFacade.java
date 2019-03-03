package org.landy.commons.datacache;

import org.landy.commons.datacache.adapter.CacheDataLoadAdapter;
import org.landy.commons.datacache.conf.LocalConfig;
import org.landy.commons.datacache.conf.MemCachedConfig;
import org.landy.commons.datacache.conf.MongoConfig;
import org.landy.commons.datacache.handler.LoadFromCache;
import org.landy.commons.datacache.handler.StoreToCache;
import org.landy.commons.datacache.help.AbstractApplicationContextHelper;
import org.landy.commons.datacache.plugins.local.load.LoadFromLocalMemory;
import org.landy.commons.datacache.plugins.local.store.StoreToLocalMemory;
import org.landy.commons.datacache.plugins.memcached.MemCachedOperator;
import org.landy.commons.datacache.plugins.memcached.load.LoadFromMemCached;
import org.landy.commons.datacache.plugins.memcached.store.StoreToMemCached;
import org.landy.commons.datacache.plugins.mongo.MongoDBOperator;
import org.landy.commons.datacache.plugins.mongo.load.LoadFromMongo;
import org.landy.commons.datacache.plugins.mongo.store.StoreToMongo;
import org.landy.commons.datacache.service.LoadCacheDataService;
import org.landy.commons.datacache.service.StoreCacheDataService;
import org.landy.commons.datacache.service.impl.LoadCacheDataServiceImpl;
import org.landy.commons.datacache.service.impl.StoreCacheDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;

public class DataCacheFacade extends AbstractApplicationContextHelper {
    public static final String BEAN_NAME_DATA_CACHE_FACADE = "dataCacheFacade";
    private static final String CACHE_KEY_PREFIX = "fortune_" + System.currentTimeMillis() + "_";
    private static Logger LOGGER = LoggerFactory.getLogger(DataCacheFacade.class);

    private List<CacheDataLoadAdapter> cacheDataAdapterList;
    private LoadCacheDataService fetchCacheDataService;
    private StoreCacheDataService storeCacheDataService;

    @Autowired
    private MongoConfig mongoConfig;
    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private MemCachedConfig memCachedConfig;
    @Autowired
    private LocalConfig localConfig;

    public DataCacheFacade() {
    }

    public static DataCacheFacade getInstance() {
        return (DataCacheFacade) getBean(BEAN_NAME_DATA_CACHE_FACADE);
    }

    public void init() {
        StoreToCache storeToCache;
        LoadFromCache fetchFromCache;

        DataCacheStrategy dataCacheStrategy = DataCacheStrategy.fromStrategy(localConfig.getCacheStrategy());
        switch (dataCacheStrategy) {
            case MEMCACHED:
                MemCachedOperator memcachedOperate = new MemCachedOperator(memCachedConfig.memCachedClient());
                storeToCache = new StoreToMemCached(memcachedOperate);
                fetchFromCache = new LoadFromMemCached(memcachedOperate, memCachedConfig.getExpiredTime(), memCachedConfig.isMappingLocalFlag());
                break;
            case MONGO:
                MongoDBOperator mongoDBOperate = new MongoDBOperator(mongoTemplate,mongoConfig.getMongoCollectionName());
                storeToCache = new StoreToMongo(mongoDBOperate);
                fetchFromCache = new LoadFromMongo(mongoDBOperate, mongoConfig.getExpiredTime(), mongoConfig.isMappingLocalFlag());
                break;
            case LOCAL:
            default:
                storeToCache = new StoreToLocalMemory();
                fetchFromCache = new LoadFromLocalMemory();
                break;

        }
        LOGGER.info("******************创建存储数据Service******************");
        this.storeCacheDataService = new StoreCacheDataServiceImpl();
        this.storeCacheDataService.buildStoreToCache(storeToCache);
        this.storeCacheDataService.buildLoadFromCache(fetchFromCache);
        LOGGER.info("******************创建获取数据Service******************");
        this.fetchCacheDataService = new LoadCacheDataServiceImpl();
        this.fetchCacheDataService.buildStoreToCache(storeToCache);
        this.fetchCacheDataService.buildLoadFromCache(fetchFromCache);
        if (localConfig.isLoadFlag()) {
            if (this.cacheDataAdapterList != null) {
                LOGGER.info("******************清除缓存数据******************start");
                this.storeCacheDataService.deleteAll();
                LOGGER.info("******************清除缓存数据******************end");
                LOGGER.info("******************缓存数据加载******************start");
                Iterator i$ = this.cacheDataAdapterList.iterator();

                while(i$.hasNext()) {
                    CacheDataLoadAdapter item = (CacheDataLoadAdapter)i$.next();
                    item.buildStoreCacheDataService(this.storeCacheDataService);
                    item.loadData();
                }

                LOGGER.info("******************缓存数据加载******************end");
            }
        } else {
            LOGGER.info("******************设置了不加载缓存******************");
        }

    }

    public String getCacheKeyPrefix() {
        if(StringUtils.isEmpty(localConfig.getCacheKeyPrefix())) return CACHE_KEY_PREFIX;
        return localConfig.getCacheKeyPrefix();
    }

    public List<CacheDataLoadAdapter> getCacheDataAdapterList() {
        return this.cacheDataAdapterList;
    }

    public void setCacheDataAdapterList(List<CacheDataLoadAdapter> cacheDataAdapterList) {
        this.cacheDataAdapterList = cacheDataAdapterList;
    }

    public static LoadCacheDataService fetchService() {
        return getInstance().fetchCacheDataService;
    }

    public static StoreCacheDataService storeService() {
        return getInstance().storeCacheDataService;
    }
}
