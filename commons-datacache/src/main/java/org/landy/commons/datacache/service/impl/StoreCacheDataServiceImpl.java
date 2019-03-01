package org.landy.commons.datacache.service.impl;


import org.landy.commons.datacache.data.MyCustomCacheData;
import org.landy.commons.datacache.service.AbstractCacheDataService;
import org.landy.commons.datacache.service.StoreCacheDataService;

import java.util.List;

public class StoreCacheDataServiceImpl  extends AbstractCacheDataService implements StoreCacheDataService {

	public <T> boolean store(String key,T value) {
		return super.getStoreToCache().store(key, value);
	}

	public <T> boolean store(String key, MyCustomCacheData<T> value) {
		return super.getStoreToCache().store(key, value);
	}

	public <T> boolean replace(String key, T value) {
		return super.getStoreToCache().replace(key, value);
	}

	public   boolean delete(String key) {
		return super.getStoreToCache().delete(key);
	}

	public boolean deleteAll() {
		return this.getStoreToCache().deleteAll();
	}

	public boolean delete(List<String> keyList) {
		return false;
	}

}
