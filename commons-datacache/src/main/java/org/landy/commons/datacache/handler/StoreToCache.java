package org.landy.commons.datacache.handler;

import org.landy.commons.datacache.data.MyCustomCacheData;

import java.util.List;

public abstract class StoreToCache extends AbstractCacheHandler {

    /**
     * 存储单个值
     * @param key
     * @param value
     * @return
     * @author:詹保山
     */
    public abstract <T> boolean store(String key,T value);

    /**
     * 存储对象
     * @param key
     * @param value
     * @return
     * @author:詹保山
     */
    public abstract <T> boolean store(String key, MyCustomCacheData<T> value);

    /**
     * 替换
     * @param key
     * @param value
     * @return
     * @author:詹保山
     */
    public abstract <T> boolean replace(String key,T value);
    /**
     * 删除操作
     * @param key
     * @return
     * @author:詹保山
     */
    public abstract boolean  delete(String key);
    /**
     * 删除所有数据
     * @return
     * @author:詹保山
     */
    public abstract boolean deleteAll();

    /**
     * 根据Key 列表进行删除
     * @param keyList
     * @return
     * @author:詹保山
     */
    public abstract boolean delete(List<String> keyList);

}
