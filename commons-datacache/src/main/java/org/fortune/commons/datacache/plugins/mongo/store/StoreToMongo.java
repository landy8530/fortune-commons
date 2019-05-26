package org.fortune.commons.datacache.plugins.mongo.store;

import org.fortune.commons.datacache.data.MyCustomCacheData;
import org.fortune.commons.datacache.handler.StoreToCache;
import org.fortune.commons.datacache.plugins.mongo.MongoDBOperator;

import java.util.List;

public class StoreToMongo extends StoreToCache {
    private MongoDBOperator mongoCacheOperate;

    public StoreToMongo(MongoDBOperator mongoCacheOperate) {
        this.mongoCacheOperate = mongoCacheOperate;
    }

    public <T> boolean store(String key, T value) {
        return this.mongoCacheOperate.save(super.getKey(key), value);
    }

    public <T> boolean store(String key, MyCustomCacheData<T> value) {
        return this.mongoCacheOperate.save(super.getKey(key), value);
    }

    public <T> boolean replace(String key, T value) {
        return this.mongoCacheOperate.update(super.getKey(key), value);
    }

    public boolean delete(String key) {
        return this.mongoCacheOperate.delete(super.getKey(key));
    }

    public boolean deleteAll() {
        return this.mongoCacheOperate.deleteAll();
    }

    public boolean delete(List<String> keyList) {
        if (keyList != null) {
            for(int i = 0; i < keyList.size(); ++i) {
                keyList.add(i, super.getKey((String)keyList.get(i)));
            }
        }

        return this.mongoCacheOperate.delete(keyList);
    }
}
