package org.landy.commons.datacache.plugins.memcached.store;

import org.landy.commons.datacache.data.MyCustomCacheData;
import org.landy.commons.datacache.handler.StoreToCache;
import org.landy.commons.datacache.plugins.memcached.MemCachedOperate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class StoreToMemCached extends StoreToCache {
	private static Logger log = LoggerFactory.getLogger(StoreToMemCached.class);
	
	private MemCachedOperate memcachedOperate;

	
	public StoreToMemCached() {
		log.info("存储策略：采用Memcached缓存");
	}
	
	


	public StoreToMemCached(MemCachedOperate memcachedOperate) {
		this();
		this.memcachedOperate = memcachedOperate;
	}




	public <T> boolean store(String key, T value) {
		return memcachedOperate.put(super.getKey(key), value);
	}

	public <T> boolean store(String key, MyCustomCacheData<T> value) {
		return memcachedOperate.put(super.getKey(key), value);
	}

	public <T> boolean replace(String key, T value) {
		return this.memcachedOperate.replace(super.getKey(key), value);
	}

	public   boolean delete(String key) {
		return this.memcachedOperate.delete(super.getKey(key));
	}
	public boolean delete(List<String> keyList) {
		for(String key:keyList){
			this.memcachedOperate.delete(key);
		}
		return true;
	}

	public boolean deleteAll() {
		this.memcachedOperate.deleteAll();
		return true;
	}
 
	public MemCachedOperate getMemcachedOperate() {
		return memcachedOperate;
	}

	public void setMemcachedOperate(MemCachedOperate memcachedOperate) {
		this.memcachedOperate = memcachedOperate;
	}




 

}
