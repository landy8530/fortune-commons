package org.landy.commons.datacache;

import org.landy.commons.datacache.adapter.CacheDataLoadAdapter;
import org.landy.commons.datacache.conf.MemCachedConfig;
import org.landy.commons.datacache.conf.MongoConfig;
import org.landy.commons.datacache.handler.LoadFromCache;
import org.landy.commons.datacache.handler.StoreToCache;
import org.landy.commons.datacache.help.AbstractApplicationContextHelper;
import org.landy.commons.datacache.plugins.local.load.LoadFromLocalMemory;
import org.landy.commons.datacache.plugins.local.store.StoreToLocalMemory;
import org.landy.commons.datacache.plugins.memcached.MemCachedOperate;
import org.landy.commons.datacache.plugins.memcached.load.LoadFromMemCached;
import org.landy.commons.datacache.plugins.memcached.store.StoreToMemCached;
import org.landy.commons.datacache.plugins.mongo.MongoDBOperate;
import org.landy.commons.datacache.plugins.mongo.load.LoadFromMongo;
import org.landy.commons.datacache.plugins.mongo.store.StoreToMongo;
import org.landy.commons.datacache.service.LoadCacheDataService;
import org.landy.commons.datacache.service.StoreCacheDataService;
import org.landy.commons.datacache.service.impl.LoadCacheDataServiceImpl;
import org.landy.commons.datacache.service.impl.StoreCacheDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class DataCacheFacade extends AbstractApplicationContextHelper {

    private static Logger logger = LoggerFactory.getLogger(DataCacheFacade.class);
    private List<CacheDataLoadAdapter> cacheDataAdapterList;
    private LoadCacheDataService fetchCacheDataService;
    private StoreCacheDataService storeCacheDataService;
    private DataCacheConfig dataCacheConfig;
    private static DataCacheFacade instance;

    public DataCacheFacade() {
    }

    private static DataCacheFacade factoryMethod() {
        logger.info("初始化缓存上下文");
        instance = new DataCacheFacade();
        return instance;
    }

    public static DataCacheFacade getInstance() {
        if (instance == null) {
            instance = new DataCacheFacade();
        }
        return instance;
    }

    public void init() {
        this.dataCacheConfig = new DataCacheConfig();
        if (this.dataCacheConfig.isMemcachedCache()) {
            MemCachedConfig configBean = (MemCachedConfig)this.dataCacheConfig.getDataCacheConfigBean();
            MemCachedOperate memcachedOperate = new MemCachedOperate(configBean.getMemCachedClient());
            new StoreToMemCached(memcachedOperate);
            new LoadFromMemCached(memcachedOperate, configBean.getExpiredTime(), configBean.getMappingLocalFlag());
        }

        Object storeToCache;
        Object fetchFromCache;
        if (this.dataCacheConfig.isMongoCache()) {
            MongoConfig configBean = (MongoConfig)this.dataCacheConfig.getDataCacheConfigBean();
            MongoDBOperate mongoDBOperate = new MongoDBOperate(configBean.getDb(), configBean.getMongoCollectionName());
            storeToCache = new StoreToMongo(mongoDBOperate);
            fetchFromCache = new LoadFromMongo(mongoDBOperate, configBean.getExpiredTime(), configBean.getMappingLocalFlag());
        } else {
            storeToCache = new StoreToLocalMemory();
            fetchFromCache = new LoadFromLocalMemory();
        }

        logger.info("******************创建存储数据Service******************");
        this.storeCacheDataService = new StoreCacheDataServiceImpl();
        this.storeCacheDataService.buildStoreToCache((StoreToCache)storeToCache);
        this.storeCacheDataService.buildLoadFromCache((LoadFromCache)fetchFromCache);
        logger.info("******************创建获取数据Service******************");
        this.fetchCacheDataService = new LoadCacheDataServiceImpl();
        this.fetchCacheDataService.buildStoreToCache((StoreToCache)storeToCache);
        this.fetchCacheDataService.buildLoadFromCache((LoadFromCache)fetchFromCache);
        if (this.dataCacheConfig.getDataCacheConfigBean().getLoadFlag()) {
            if (this.cacheDataAdapterList != null) {
                logger.info("******************清除缓存数据******************start");
                this.storeCacheDataService.deleteAll();
                logger.info("******************清除缓存数据******************end");
                logger.info("******************缓存数据加载******************start");
                Iterator i$ = this.cacheDataAdapterList.iterator();

                while(i$.hasNext()) {
                    CacheDataLoadAdapter item = (CacheDataLoadAdapter)i$.next();
                    item.buildStoreCacheDataService(this.storeCacheDataService);
                    item.loaderData();
                }

                logger.info("******************缓存数据加载******************end");
            }
        } else {
            logger.info("******************设置了不加载缓存******************");
        }

    }

    public List<CacheDataLoadAdapter> getCacheDataAdapterList() {
        return this.cacheDataAdapterList;
    }

    public void setCacheDataAdapterList(List<CacheDataLoadAdapter> cacheDataAdapterList) {
        this.cacheDataAdapterList = cacheDataAdapterList;
    }

    public static LoadCacheDataService fetchService() {
        return instance.fetchCacheDataService;
    }

    public static StoreCacheDataService storeService() {
        return instance.storeCacheDataService;
    }

    public DataCacheConfig getDataCacheConfig() {
        return this.dataCacheConfig;
    }

}
