package org.landy.commons.datacache.service;

import org.landy.commons.datacache.data.MyCustomCacheData;

import java.util.List;
import java.util.Map;

public interface LoadCacheDataService extends  CacheDataOperateService {

	/**
	 * 获取的是自定义的缓存数据结构
	 * @param key
	 * @return
	 * @author:Landy
	 */
	<T> MyCustomCacheData<T> fetchOfCustomCacheData(String key);
	/**
	 * 获取的对象
	 * @param key
	 * @return
	 * @author:Landy
	 */
	Object fetchOfObject(String key);
	/**
	 * 获取字符串，如果不存在则返回 ""
	 * @param key
	 * @return
	 * @author:Landy
	 */
	String fetchOfString(String key);
	/**
	 * 获取Map 结构
	 * @param key
	 * @return
	 * @author:Landy
	 */
	Map fetchOfMap(String key);
	/**
	 * 获取List 结构
	 * @param key
	 * @return
	 * @author:Landy
	 */
	List fetchOfList(String key);
}
