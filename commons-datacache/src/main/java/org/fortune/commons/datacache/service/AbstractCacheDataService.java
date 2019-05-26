package org.fortune.commons.datacache.service;

import org.fortune.commons.datacache.handler.LoadFromCache;
import org.fortune.commons.datacache.handler.StoreToCache;

public class AbstractCacheDataService {

	/**
	 * 存储操作句柄
	 */
	private StoreToCache storeToCache;
	/**
	 * 提取操作句柄
	 */
	private LoadFromCache loadFromCache;

	public StoreToCache getStoreToCache() {
		return storeToCache;
	}

	public void setStoreToCache(StoreToCache storeToCache) {
		this.storeToCache = storeToCache;
	}

	public LoadFromCache getLoadFromCache() {
		return loadFromCache;
	}

	public void setLoadFromCache(LoadFromCache loadFromCache) {
		this.loadFromCache = loadFromCache;
	}
	
	public void buildStoreToCache(StoreToCache storeToCache) {
		this.setStoreToCache(storeToCache);
	} 
	public void buildLoadFromCache(LoadFromCache loadFromCache) {
		this.setLoadFromCache(loadFromCache);
	}
	

	 
	
}
