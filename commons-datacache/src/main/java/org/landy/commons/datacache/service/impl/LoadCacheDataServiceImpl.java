package org.landy.commons.datacache.service.impl;

import org.landy.commons.datacache.data.MyCustomCacheData;
import org.landy.commons.datacache.service.AbstractCacheDataService;
import org.landy.commons.datacache.service.LoadCacheDataService;

import java.util.List;
import java.util.Map;

public class LoadCacheDataServiceImpl extends AbstractCacheDataService implements
		LoadCacheDataService {

	public <T> MyCustomCacheData<T> fetchOfCustomCacheData(String key) {
		return super.getLoadFromCache().load(key);
	}

	public Object fetchOfObject(String key) {
		return super.getLoadFromCache().load(key);
	}

	public String fetchOfString(String key) {
		Object obj=this.fetchOfObject(key);
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}
	}

	public Map fetchOfMap(String key) {
		return this.getLoadFromCache().load(key);
	}

	public List fetchOfList(String key) {
		return this.getLoadFromCache().load(key);
	}
}
