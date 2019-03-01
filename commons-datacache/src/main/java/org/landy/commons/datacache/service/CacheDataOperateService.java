package org.landy.commons.datacache.service;

import org.landy.commons.datacache.handler.LoadFromCache;
import org.landy.commons.datacache.handler.StoreToCache;

public interface CacheDataOperateService {
	
	/**
	 * 设置数据存储句柄
	 * @return
	 */
	public void buildStoreToCache(StoreToCache storeToCache);
	/**
	 * 设置数据获取句柄
	 * @param fetchFromCache
	 */
	public void buildLoadFromCache(LoadFromCache fetchFromCache);
}
