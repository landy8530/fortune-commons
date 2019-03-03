package org.landy.commons.nosql.mongodb.dao;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public interface SpringMongoDao {

    <T> void save(T obj);

    <T> void save(T obj, String collectionName);

    <T> T queryForObject(Query query, Class<T> clazz);

    <T> List<T> queryForList(Query query, Class<T> clazz);

    <T> List<T> queryForList(Query query, Class<T> clazz, String collectionName);

    <T> List<T> findAll(Class<T> clazz);

    <T> List<T> findAll(Class<T> clazz, String collectionName);

    <T> T findById(Object obj, Class<T> clazz);

    <T> T findById(Object obj, Class<T> clazz, String collectionName);

    void updateOrInsert(Query query, Update update, Class<?> entityClass, String collectionName);

    void updateOrInsert(Query query, Update update, Class<?> entityClass);

    void update(Query query, Update update, Class<?> entityClass);

    void update(Query query, Update update, Class<?> entityClass, String collectionName);

    void updateMulti(Query query, Update update, Class<?> entityClass);

    void updateMulti(Query query, Update update, Class<?> entityClass, String collectionName);

    void remove(Query query, Class<?> entityClass);

    void remove(Query query, String collectionName);

    void remove(Query query, Class<?> entityClass, String collectionName);

}
