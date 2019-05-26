/**
* 版权所有：福建邮科电信业务部厦门研发中心 
*====================================================
* 文件名称: LocalDataStorage.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    2013-5-28			Landy(创建:创建文件)
*====================================================
* 类描述：(说明未实现或其它不应生成javadoc的内容)
* 本地数据存储
*/
package org.fortune.commons.datacache.plugins.local.store;


import org.fortune.commons.datacache.data.MyCustomCacheData;
import org.fortune.commons.datacache.handler.StoreToCache;
import org.fortune.commons.datacache.plugins.local.LocalCacheContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StoreToLocalMemory extends StoreToCache {

	private static Logger log = LoggerFactory.getLogger(StoreToLocalMemory.class);

	public StoreToLocalMemory() {
		log.info("存储策略：采用本地缓存");
	}

	public <T> boolean store(String key, T value) {
//		log.info("加入缓存：key="+key+",value="+value);
		LocalCacheContainer.put(super.getKey(key), value);
		return true;
	}
	
	public <T> boolean store(String key, MyCustomCacheData<T> value) {
//		log.info("加入缓存：key="+key+",value="+value);
		LocalCacheContainer.put(super.getKey(key), value);
		return true;
	}
 

	public <T> boolean replace(String key, T value) {
		 LocalCacheContainer.replace(super.getKey(key), value);
		return true;
	}

	public  boolean delete(String key) {
		 LocalCacheContainer.delete(super.getKey(key));
		 return true;
	}

	public boolean deleteAll() {
		LocalCacheContainer.deleteAll();
		return true;
	}

	public boolean delete(List<String> keyList) {
		for(String key:keyList){
			LocalCacheContainer.delete(key);
		}
		return true;
	}
	
	

 

}
