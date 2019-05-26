package org.fortune.commons.datacache.plugins.memcached;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.fortune.commons.datacache.plugins.support.FetchOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class MemCachedOperator implements FetchOperator {
    private Logger logger = LoggerFactory.getLogger(MemCachedOperator.class);


    private MemcachedClient memcachedClient;
    private int expiredTime;

    public MemCachedOperator(MemcachedClient memcachedClient, int expiredTime) {
        this.memcachedClient = memcachedClient;
        this.expiredTime = expiredTime;
    }

    /**
     * 添加一个指定的值到缓存中，如果有Key存在则不会替换
     *
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, Object value) {
        try {
            return this.getMemcachedClient().add(key, expiredTime, value);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return false;
    }

    /**
     * @param key 存储的key名称
     * @param value 实际存储的数据，可以是任意的java可序列化类型。
     * @param expiredTime 是expire时间（单位秒），超过这个时间,memcached将这个数据替换出去，0表示永久存储（默认是一个月）
     * @return
     */
    public boolean add(String key, Object value, int expiredTime) {
        try {
            return this.getMemcachedClient().add(key, expiredTime, value);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return false;
    }
    /**
     * 添加一个指定的值到缓存中，如果有Key存在则会替换
     *
     * @param key
     * @param value
     * @return
     */
    public boolean put(String key, Object value) {
        try {
            return this.getMemcachedClient().set(key,expiredTime, value);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return false;
    }

    public boolean put(String key, Object value, int expiredTime) {
        try {
            return this.getMemcachedClient().set(key, expiredTime, value);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return false;
    }

    /**
     * 替换一个指定的值到缓存中.
     *
     * @param key
     * @param value
     * @return
     */
    public boolean replace(String key, Object value) {
        try {
            return this.getMemcachedClient().replace(key,expiredTime, value);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return false;
    }

    /**
     * 替换
     *
     * @param key
     * @param value
     * @param expiredTime
     * @return
     */
    public boolean replace(String key, Object value, int expiredTime) {
        try {
            return this.getMemcachedClient().replace(key, expiredTime,value);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return false;
    }

    /**
     * 删除一个指定的值到缓存中.
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        try {
            return this.getMemcachedClient().delete(key);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return false;
    }

    /**
     * 删除所有
     *
     * @author:Landy
     */
    public void deleteAll() {
        logger.info("清空所有缓存数据........start");
        try {
            this.getMemcachedClient().flushAll();
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        logger.info("清空所有缓存数据........end");
    }

    /**
     * 根据指定的关键字获取对象.
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        try {
            return this.getMemcachedClient().get(key);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return null;
    }

    /**
     * 获取多个
     *
     * @param keys
     * @return
     */
    public Map<String, Object> getMulti(String[] keys) {
        List<String> keyList = Arrays.asList(keys);
        try {
            return this.getMemcachedClient().get(keyList);
        } catch (TimeoutException e) {
            logger.error("unexpected exception:",e);
        } catch (InterruptedException e) {
            logger.error("unexpected exception:",e);
        } catch (MemcachedException e) {
            logger.error("unexpected exception:",e);
        }
        return null;
    }


    public MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }
}
