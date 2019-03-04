package org.landy.commons.datacache.plugins.support.load;

import org.landy.commons.datacache.plugins.support.FetchOperator;

public class FromProxyLocalMemory extends FromCache {
    private FetchOperator operate;
    private long expiredTime;

    public FromProxyLocalMemory(FetchOperator operate, long expiredTime) {
        this.operate = operate;
        this.expiredTime = expiredTime;
    }
    /**
     * 先从本地加载，如果过期再从响应的缓存中加载
     */
    private Object fetchLocalData(String key) {
        LocalData localData = MappingToLocalContainer.get(key);
        Object data = null;
        if (localData == null) {
            data = this.operate.get(key);
            localData = new LocalData();
            localData.setData(data);
            localData.setTime(System.currentTimeMillis());
            MappingToLocalContainer.put(key, localData);
        } else if (localData != null) {
            //判断时间是否过期
            long curerentTime = System.currentTimeMillis();
            long cs = curerentTime - localData.getTime();
            if (cs > this.expiredTime) { //过期数据重新加载
                LOGGER.info(key+"--本地数据过期，重新获取");
                data = this.operate.get(key);
                localData.setData(data);
                localData.setTime(System.currentTimeMillis());
                MappingToLocalContainer.put(key, localData);
            } else {
                data = localData.getData();
            }
        }

        return data;
    }

    public Object fetch(String key) {
        return this.fetchLocalData(key);
    }
}
