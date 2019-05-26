package org.fortune.commons.datacache.service;

import org.fortune.commons.datacache.data.MyCustomCacheData;

import java.util.List;

public interface StoreCacheDataService extends CacheDataOperateService {

    /**
     * 写数据
     *
     * @param key
     * @param value
     * @return
     */
    <T> boolean store(String key, MyCustomCacheData<T> value);

    /**
     * 存储单值
     *
     * @param key
     * @param value
     * @return
     */
    <T> boolean store(String key, T value);

    /**
     * 替换
     *
     * @param key
     * @param value
     * @return
     */
    <T> boolean replace(String key, T value);

    /**
     * 删除操作
     *
     * @param key
     * @return
     */
    boolean delete(String key);

    /**
     * 删除所有
     *
     * @return
     */
    boolean deleteAll();

    /**
     * 根据key进行删除
     *
     * @param keyList
     * @return
     */
    boolean delete(List<String> keyList);

}
