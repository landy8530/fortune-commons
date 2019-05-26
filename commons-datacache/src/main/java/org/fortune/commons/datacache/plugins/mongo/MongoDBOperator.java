package org.fortune.commons.datacache.plugins.mongo;

import org.bson.Document;
import org.fortune.commons.core.util.DateUtil;
import org.fortune.commons.datacache.plugins.support.FetchOperator;
import org.fortune.commons.datacache.data.MyCustomCacheData;
import org.fortune.commons.nosql.mongodb.dao.impl.NativeMongoDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MongoDBOperator implements FetchOperator {
    private static final String MONGO_ID = "_id";
    private static final String MONGO_CLASS = "_class";
    private static final String MONGO_VALUE = "value";
    private static final String MONGO_START_LOAD_TIME = "_startLoadTime";

    private static final Logger logger = LoggerFactory.getLogger(MongoDBOperator.class);
    private NativeMongoDaoImpl nativeMongoDao;
    private String collectionName;

    public MongoDBOperator(MongoOperations mongoTemplate, String collectionName) {
        nativeMongoDao = new NativeMongoDaoImpl();
        nativeMongoDao.setMongoTemplate(mongoTemplate);
        this.collectionName = collectionName;
    }

    private <T> Document createDocument(String key, T value) {
        Document dbObj = new Document();
        dbObj.put(MONGO_ID, key);
        dbObj.put(MONGO_CLASS, value.getClass().getName());
        dbObj.put(MONGO_START_LOAD_TIME, DateUtil.getCurrentDateTime());
        if (value instanceof Map) {
            Map<String, Object> map = (Map)value;
            map.remove((Object)null);
            map.remove("");
        }
        dbObj.put(MONGO_VALUE, value);
        return dbObj;
    }

    public <T> boolean save(String key, T value) {
        Document dbObj = this.createDocument(key, value);
        nativeMongoDao.insert(dbObj,collectionName);
        return true;
    }

    public <T> boolean save(String key, MyCustomCacheData<T> value) {
        Document dbObj = this.createDocument(key, value);
        try {
            nativeMongoDao.insert(dbObj,collectionName);
        } catch (Exception ex) {
            logger.error("Mongo存储出错，Key=" + key + ",value=" + value,ex);
        }
        return true;
    }

    public <T> boolean update(String key, T value) {
        Document dbObj = this.createDocument(key, value);
        nativeMongoDao.update(collectionName,key,dbObj);
        return true;
    }

    public boolean delete(String key) {
        nativeMongoDao.deleteById(collectionName,key);
        return true;
    }

    public boolean deleteAll() {
        nativeMongoDao.deleteAll(collectionName);
        return true;
    }

    public boolean delete(List<String> keyList) {
        Iterator i$ = keyList.iterator();
        while(i$.hasNext()) {
            String key = (String)i$.next();
            nativeMongoDao.deleteById(collectionName,key);
        }
        return true;
    }

    public Object get(String key) {
        Document queryDBObj = nativeMongoDao.getDocumentById(collectionName,key);
        if (queryDBObj == null) {
            return null;
        } else {
            Object valObj = queryDBObj.get(MONGO_VALUE);
            if (valObj instanceof Document) {
                String clazz = queryDBObj.getString(MONGO_CLASS);
                Document mapDBObj = (Document)valObj;

                Map valMap;
                try {
                    valMap = (Map)Class.forName(clazz).newInstance();
                    valMap.putAll(new LinkedHashMap(mapDBObj));
                } catch (Exception ex) {
                    logger.error("以Map类型转换错误,直接将Document转为Map返回",ex);
                    valMap = new LinkedHashMap(mapDBObj);
                }
                return valMap;
            } else {
                return valObj;
            }
        }
    }
}
