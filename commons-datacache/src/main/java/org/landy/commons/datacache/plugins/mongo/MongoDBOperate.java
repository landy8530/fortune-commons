package org.landy.commons.datacache.plugins.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.landy.commons.datacache.data.MyCustomCacheData;
import org.landy.commons.datacache.plugins.support.FetchOperate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MongoDBOperate implements FetchOperate {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBOperate.class);
    private DB db;
    private String collectionName;

    public MongoDBOperate(DB db, String collectionName) {
        this.db = db;
        this.collectionName = collectionName;
    }

    private DBCollection getDBCollection() {
        return this.db.getCollection(this.collectionName);
    }

    private <T> BasicDBObject createDBObject(String key, T value) {
        BasicDBObject dbObj = new BasicDBObject();
        dbObj.put("_id", key);
        dbObj.put("_class", value.getClass().getName());
        if (value instanceof Map) {
            Map<String, Object> map = (Map)value;
            map.remove((Object)null);
            map.remove("");
        }

        dbObj.put("value", value);
        return dbObj;
    }

    public <T> boolean save(String key, T value) {
        DBCollection dbCol = this.getDBCollection();
        BasicDBObject dbObj = this.createDBObject(key, value);
        dbCol.save(dbObj);
        return true;
    }

    public <T> boolean save(String key, MyCustomCacheData<T> value) {
        DBCollection dbCol = this.getDBCollection();
        BasicDBObject dbObj = this.createDBObject(key, value);

        try {
            dbCol.save(dbObj);
        } catch (Exception var6) {
            logger.error("Mongo存储出错，Key=" + key + ",value=" + value);
            //Assert.fail("使用MogoDB缓存中间表数据，加载失败," + var6.getMessage());
        }

        return true;
    }

    public <T> boolean update(String key, T value) {
        DBCollection dbCol = this.getDBCollection();
        BasicDBObject dbObj = this.createDBObject(key, value);
        dbCol.save(dbObj);
        return true;
    }

    public boolean delete(String key) {
        DBCollection dbCol = this.getDBCollection();
        BasicDBObject dbObj = new BasicDBObject();
        dbObj.put("_id", key);
        dbCol.remove(dbObj);
        return true;
    }

    public boolean deleteAll() {
        DBCollection dbCol = this.getDBCollection();
        dbCol.drop();
        return true;
    }

    public boolean delete(List<String> keyList) {
        DBCollection dbCol = this.getDBCollection();
        BasicDBObject dbObj = new BasicDBObject();
        Iterator i$ = keyList.iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            dbObj.put("_id", key);
            dbCol.remove(dbObj);
        }

        return true;
    }

    public Object get(String key) {
        DBCollection dbCol = this.getDBCollection();
        BasicDBObject dbObj = new BasicDBObject();
        dbObj.put("_id", key);
        BasicDBObject queryDBObj = (BasicDBObject)dbCol.findOne(dbObj);
        if (queryDBObj == null) {
            return null;
        } else {
            Object valObj = queryDBObj.get("value");
            if (valObj instanceof BasicDBObject) {
                String clazz = queryDBObj.getString("_class");
                BasicDBObject mapDBObj = (BasicDBObject)valObj;

                Map valMap;
                try {
                    valMap = (Map)Class.forName(clazz).newInstance();
                    valMap.putAll(mapDBObj.toMap());
                } catch (Exception var10) {
                    logger.error("以Map类型转换错误,直接将BasicDBObject转为Map返回");
                    valMap = mapDBObj.toMap();
                }

                return valMap;
            } else {
                return valObj;
            }
        }
    }

}
