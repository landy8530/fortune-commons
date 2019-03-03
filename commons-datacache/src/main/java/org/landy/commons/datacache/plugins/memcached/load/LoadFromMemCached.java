package org.landy.commons.datacache.plugins.memcached.load;

import org.landy.commons.datacache.handler.LoadFromCache;
import org.landy.commons.datacache.plugins.memcached.MemCachedOperator;
import org.landy.commons.datacache.plugins.support.load.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadFromMemCached extends LoadFromCache {
	private Logger logger = LoggerFactory.getLogger(LoadFromMemCached.class);
	private MemCachedOperator memcachedOperate;
	/**
	 * 过期时间，以秒为单位
	 */
	private long expiredTime = 0L;
	/**
	 * 是否启用映射为本地的缓存数据
	 */
	private boolean mappingLocalFlag = true;
	/**
	 * 从那边加载数据
	 */
	private FromCache fromCache;

	public LoadFromMemCached(MemCachedOperator memcachedOperate, long expiredTime, boolean mappingLocalFlag) {
		this.memcachedOperate = memcachedOperate;
		this.setExpiredTime(expiredTime);
		this.setMappingLocalFlag(mappingLocalFlag);
	}

	public <T> T load(String key) {
		return this.fromCache.fetch(super.getKey(key));
	}

	public MemCachedOperator getMemcachedOperate() {
		return this.memcachedOperate;
	}

	public void setMemcachedOperate(MemCachedOperator memcachedOperate) {
		this.memcachedOperate = memcachedOperate;
	}

	public long getExpiredTime() {
		return this.expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
		this.expiredTime = expiredTime * 1000L;
		this.logger.info("映射本地缓存的过期时间(毫米)：" + this.expiredTime);
	}

	public boolean getMappingLocalFlag() {
		return this.mappingLocalFlag;
	}

	public void setMappingLocalFlag(boolean mappingLocalFlag) {
		this.mappingLocalFlag = mappingLocalFlag;
		if (this.mappingLocalFlag) {
			this.logger.info("您启用了将Memcached的数据映射到本地内存功能");
			this.fromCache = new FromProxyLocalMemory(this.memcachedOperate, this.expiredTime);
		} else {
			this.logger.info("直接从Memcached获取缓存数据");
			this.fromCache = new FromMemCached(this.memcachedOperate);
		}

	}

}
