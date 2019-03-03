package org.landy.commons.datacache.plugins.memcached;

import com.danga.MemCached.MemCachedClient;
import org.landy.commons.datacache.plugins.support.FetchOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

public class MemCachedOperator implements FetchOperator {
	private  Logger logger = LoggerFactory.getLogger(MemCachedOperator.class);
	

	private  MemCachedClient memCachedClient;
	
	 
	public MemCachedOperator(MemCachedClient memCachedClient){
		this.memCachedClient=memCachedClient;
	}
	
	/**
     * 添加一个指定的值到缓存中，如果有Key存在则不会替换
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, Object value)
    {
        return this.getMemCachedClient().add(key, value);
    }
     
    public boolean add(String key, Object value, Date expiry)
    {
        return this.getMemCachedClient().add(key, value, expiry);
    }
    public boolean put(String key ,Object value){
//    	logger.info("key="+key+",data="+value);
    	return this.getMemCachedClient().set(key, value);
    }
    public boolean put(String key, Object value, Date expiry){
        return this.getMemCachedClient().set(key, value, expiry);
    }
     
    /**
     * 替换一个指定的值到缓存中.
     * @param key
     * @param value
     * @return
     */
    public boolean replace(String key, Object value)
    {
        return this.getMemCachedClient().replace(key, value);
    }
    /**
     * 替换
     * @param key
     * @param value
     * @param expiry
     * @return
     */
    public boolean replace(String key, Object value, Date expiry)
    {
        return this.getMemCachedClient().replace(key, value, expiry);
    }
    /**
     * 删除一个指定的值到缓存中.
     * @param key
     * @return
     */
    public boolean delete(String key)
    {
        return this.getMemCachedClient().delete(key);
    }
    /**
     * 删除所有
     * 
     * @author:Landy
     */
    public void deleteAll(){
    	logger.info("清空所有缓存数据........start");
    	this.getMemCachedClient().flushAll();
    	logger.info("清空所有缓存数据........end");
    }
    /**
     * 根据指定的关键字获取对象.
     * @param key
     * @return
     */
    public Object get(String key)
    {
        return this.getMemCachedClient().get(key);
    }
    /**
     * 获取多个
     * @param keys
     * @return
     */
    public Map<String,Object> getMulti(String[] keys){
    	return this.getMemCachedClient().getMulti(keys);
    }

 
	
	public MemCachedClient getMemCachedClient(){
		return memCachedClient;
	}
 

 

 
}
