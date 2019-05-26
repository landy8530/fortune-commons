package org.fortune.commons.datacache.plugins.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LocalCacheContainer {
	private static Logger log = LoggerFactory.getLogger(LocalCacheContainer.class);
	private static Map<String,Object> container = new ConcurrentHashMap<String,Object>(32);

	public static void  put(String key ,Object value){
		container.put(key, value);
	}
	
	public static Object get(String key){
		return container.get(key);
	}
	
	public static void replace(String key ,Object value){
		container.put(key, value);
	}
	
	public static void delete(String key){
		container.remove(key);
	}
	public static void deleteAll(){
		log.info("清空所有缓存数据........start");
		container.clear();
		log.info("清空所有缓存数据........end");
	}
}
