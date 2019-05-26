package org.fortune.commons.nosql.mongodb.dao.impl;

import org.fortune.commons.nosql.mongodb.dao.SpringMongoDao;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class SpringMongoDaoImpl implements SpringMongoDao {

    private MongoOperations mongoTemplate;

    @Override
    public <T> void save(T obj) {
        this.mongoTemplate.save(obj);
    }

    @Override
    public <T> void save(T obj, String collectionName) {
        this.mongoTemplate.save(obj,collectionName);
    }

    @Override
    public <T> T queryForObject(Query query, Class<T> clazz) {
        return this.mongoTemplate.findOne(query,clazz);
    }

    @Override
    public <T> List<T> queryForList(Query query, Class<T> clazz) {
        return this.mongoTemplate.find(query,clazz);
    }

    @Override
    public <T> List<T> queryForList(Query query, Class<T> clazz, String collectionName) {
        return this.mongoTemplate.find(query,clazz,collectionName);
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        return this.mongoTemplate.findAll(clazz);
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz, String collectionName) {
        return this.mongoTemplate.findAll(clazz,collectionName);
    }

    @Override
    public <T> T findById(Object id, Class<T> clazz) {
        return this.mongoTemplate.findById(id,clazz);
    }

    @Override
    public <T> T findById(Object id, Class<T> clazz, String collectionName) {
        return this.mongoTemplate.findById(id,clazz,collectionName);
    }

    @Override
    public void updateOrInsert(Query query, Update update, Class<?> entityClass, String collectionName) {
        this.mongoTemplate.upsert(query,update,entityClass,collectionName);
    }

    @Override
    public void updateOrInsert(Query query, Update update, Class<?> entityClass) {
        this.mongoTemplate.upsert(query,update,entityClass);
    }

    @Override
    public void update(Query query, Update update, Class<?> entityClass) {
        this.mongoTemplate.updateFirst(query,update,entityClass);
    }

    @Override
    public void update(Query query, Update update, Class<?> entityClass, String collectionName) {
        this.mongoTemplate.updateFirst(query,update,entityClass,collectionName);
    }

    @Override
    public void updateMulti(Query query, Update update, Class<?> entityClass) {
        this.mongoTemplate.updateMulti(query,update,entityClass);
    }

    @Override
    public void updateMulti(Query query, Update update, Class<?> entityClass, String collectionName) {
        this.mongoTemplate.updateMulti(query,update,entityClass,collectionName);
    }

    @Override
    public void remove(Query query, Class<?> entityClass) {
        this.mongoTemplate.remove(query,entityClass);
    }

    @Override
    public void remove(Query query, String collectionName) {
        this.mongoTemplate.remove(query,collectionName);
    }

    @Override
    public void remove(Query query, Class<?> entityClass, String collectionName) {
        this.remove(query,entityClass,collectionName);
    }

    public void setMongoTemplate(MongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
