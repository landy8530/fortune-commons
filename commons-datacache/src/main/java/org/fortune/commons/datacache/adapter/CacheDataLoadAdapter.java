package org.fortune.commons.datacache.adapter;

import org.fortune.commons.datacache.data.MyCustomCacheData;
import org.fortune.commons.datacache.service.StoreCacheDataService;

import java.util.List;

public abstract class CacheDataLoadAdapter {

    private StoreCacheDataService storeCacheDataService;

    public CacheDataLoadAdapter() {
    }

    protected <T> boolean put(String key, MyCustomCacheData<T> value) {
        return this.storeCacheDataService.store(key, value);
    }

    public abstract boolean loadData();

    public abstract List<String> getStoreKeys();

    public StoreCacheDataService getStoreCacheDataService() {
        return this.storeCacheDataService;
    }

    public void buildStoreCacheDataService(StoreCacheDataService storeCacheDataService) {
        this.storeCacheDataService = storeCacheDataService;
    }

}
