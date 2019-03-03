package org.landy.commons.nosql.mongodb.dao;

import org.bson.Document;
import org.landy.commons.core.page.PageInfo;

import java.io.Serializable;
import java.util.List;

public interface NativeMongoDao {

    void insert(Document document, String collectionName);

    void insert(List<Document> list, String collectionName);

    long deleteById(String collectionName, Serializable id);

    long deleteAll(String collectionName, Document document);

    long deleteAll(String collectionName);

    long update(String collectionName, Serializable id, Document document);

    long update(String collectionName, Document queryDbObj, Document updateDbObj);

    Document getDocumentById(String collectionName, Serializable id);

    List<Document> queryForList(String collectionName, Document document);

    List<Document> queryForList(String collectionName, Document queryCnd, Document orderBy);

    List<Document> queryForListSomeOfFields(String collectionName, Document queryFields, Document queryCnd);

    List<Document> queryForList(String collectionName, Document queryFields, Document queryCnd, Document orderBy);

    List<Document> queryForList(String collectionName, Document queryCnd, Document orderBy, int limit);

    List<Document> queryForList(String collectionName, Document queryFields, Document queryCnd, Document orderBy, int limit);

    PageInfo queryForPage(String collectionName, Document queryCnd, PageInfo pageInfo);

    PageInfo queryForPage(String collectionName, Document queryCnd, Document orderBy, PageInfo pageInfo);

    PageInfo queryForPage(String collectionName, Document queryFields, Document queryCnd, Document orderBy, PageInfo pageInfo);

    boolean dropCollection(String collectionName);

}
