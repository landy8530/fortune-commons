package org.landy.commons.nosql.mongodb.dao.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.landy.commons.core.page.PageInfo;
import org.landy.commons.nosql.mongodb.dao.NativeMongoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NativeMongoDaoImpl implements NativeMongoDao {

    private static final Logger logger = LoggerFactory.getLogger(NativeMongoDaoImpl.class);

    private MongoOperations mongoTemplate;

    public void setMongoTemplate(MongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private MongoCollection<Document> getDBCollection(String collectionName) {
        return this.mongoTemplate.getCollection(collectionName);
    }

    public void insert(Document document, String collectionName) {
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        dbCollection.insertOne(document);
    }

    public void insert(List<Document> list, String collectionName) {
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        dbCollection.insertMany(list);
    }

    public long deleteById(String collectionName, Serializable id) {
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        Bson filterId = Filters.eq(id);
        DeleteResult deleteResult = dbCollection.deleteOne(filterId);
        return deleteResult.getDeletedCount();
    }

    public long deleteAll(String collectionName, Document dbObj) {
        Assert.notNull(dbObj, "删除条件不为空");
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        Bson filter = buildFilter(dbObj);
        DeleteResult deleteResult = dbCollection.deleteMany(filter);
        return deleteResult.getDeletedCount();
    }

    @Override
    public long deleteAll(String collectionName) {
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        return dbCollection.deleteMany(new Document()).getDeletedCount();
    }

    public long update(String collectionName, Serializable id, Document updateDbObj) {
        Assert.notNull(updateDbObj, "修改对象不为空");
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        Bson filterId = Filters.eq(id);
        UpdateResult updateResult = dbCollection.updateOne(filterId,updateDbObj);
        return updateResult.getModifiedCount();
    }

    public long update(String collectionName, Document queryDbObj, Document updateDbObj) {
        Assert.notNull(queryDbObj, "修改的查询条件不为空");
        Assert.notNull(updateDbObj, "修改的字段不为空");
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        Bson filter = buildFilter(queryDbObj);
        UpdateResult updateResult = dbCollection.updateMany(filter,updateDbObj);
        return updateResult.getModifiedCount();
    }

    public Document getDocumentById(String collectionName, Serializable id) {
        Assert.notNull(id, "ID不为空");
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        Bson filterId = Filters.eq(id);
        FindIterable<Document> resultIterable = dbCollection.find(filterId);
        return resultIterable.first();
    }

    private List<Document> findForList(String collectionName, Document queryFields, Document queryCnd, Document orderBy, int limit) {
        Assert.notNull(queryCnd, "查询不为空");
        MongoCollection dbCollection = this.getDBCollection(collectionName);
        Bson filter = buildFilter(queryCnd);
        FindIterable<Document> dbIterable = null;
        if (orderBy != null) {
            if (limit != 0) {
                if (queryFields != null) {
                    dbIterable = dbCollection.find(filter).projection(queryFields).sort(orderBy).limit(limit);
                } else {
                    dbIterable = dbCollection.find(filter).sort(orderBy).limit(limit);
                }
            } else if (queryFields != null) {
                dbIterable = dbCollection.find(filter).projection(queryFields).sort(orderBy);
            } else {
                dbIterable = dbCollection.find(filter).sort(orderBy);
            }
        } else if (queryFields != null) {
            dbIterable = dbCollection.find(filter).projection(queryFields);
        } else {
            dbIterable = dbCollection.find(filter);
        }
        List<Document> list = list(dbIterable);

        return list;
    }

    public List<Document> queryForListSomeOfFields(String collectionName, Document queryFields, Document queryCnd) {
        return this.findForList(collectionName, queryFields, queryCnd, (Document)null, 0);
    }

    public List<Document> queryForList(String collectionName, Document queryCnd) {
        return this.findForList(collectionName, (Document)null, queryCnd, (Document)null, 0);
    }

    public List<Document> queryForList(String collectionName, Document queryCnd, Document orderBy) {
        return this.findForList(collectionName, (Document)null, queryCnd, orderBy, 0);
    }

    public List<Document> queryForList(String collectionName, Document queryFields, Document queryCnd, Document orderBy) {
        return this.findForList(collectionName, queryFields, queryCnd, orderBy, 0);
    }

    public List<Document> queryForList(String collectionName, Document queryCnd, Document orderBy, int limit) {
        return this.findForList(collectionName, (Document)null, queryCnd, orderBy, limit);
    }

    public List<Document> queryForList(String collectionName, Document queryFields, Document queryCnd, Document orderBy, int limit) {
        return this.findForList(collectionName, queryFields, queryCnd, orderBy, limit);
    }

    private PageInfo findForPage(String collectionName, Document queryFields, Document queryCnd, Document orderBy, PageInfo pageInfo) {
        if (pageInfo == null) {
            pageInfo = new PageInfo();
        }

        if (pageInfo.getPageSize() == -1) {
            pageInfo.setPageCount(1);
            pageInfo.setPageIndex(1);
        }

        MongoCollection dbCollection = this.getDBCollection(collectionName);
        Bson filter = buildFilter(queryCnd);
        int startPage;
        if (pageInfo.getTotalCount() == 0) {
            startPage = Long.valueOf(dbCollection.countDocuments(filter)).intValue();
            pageInfo.setTotalCount(startPage);
        }

        int pageIndex = pageInfo.getPageIndex();
        if (pageIndex == 0) {
            pageInfo.setPageIndex(1);
        } else {
            pageInfo.setPageIndex(pageIndex);
        }

        startPage = 0;
        int maxPage = 0;
        if (pageInfo.getPageSize() != -1) {
            startPage = (pageIndex - 1) * pageInfo.getPageSize();
            maxPage = pageInfo.getPageSize();
        }

        FindIterable<Document>  dbIterable = null;
        if (orderBy != null) {
            if (queryFields != null) {
                dbIterable = dbCollection.find(filter).projection(queryFields).skip(startPage).limit(maxPage).sort(orderBy);
            } else {
                dbIterable = dbCollection.find(filter).skip(startPage).limit(maxPage).sort(orderBy);
            }
        } else if (queryFields != null) {
            dbIterable = dbCollection.find(filter).projection(queryFields).skip(startPage).limit(maxPage);
        } else {
            dbIterable = dbCollection.find(filter).skip(startPage).limit(maxPage);
        }

        List<Document> list = list(dbIterable);

        pageInfo.setDataList(list);
        return pageInfo;
    }

    public PageInfo queryForPage(String collectionName, Document queryCnd, PageInfo pageInfo) {
        return this.findForPage(collectionName, (Document)null, queryCnd, (Document)null, pageInfo);
    }

    public boolean dropCollection(String collectionName) {
        Assert.notNull(collectionName, "删除的集合名词不为空");
        MongoCollection dbCollection = this.getDBCollection(collectionName);

        try {
            dbCollection.drop();
            return true;
        } catch (Exception ex) {
            logger.error("删除集合失败",ex);
            return false;
        }
    }

    public PageInfo queryForPage(String collectionName, Document queryCnd, Document orderBy, PageInfo pageInfo) {
        return this.findForPage(collectionName, (Document)null, queryCnd, orderBy, pageInfo);
    }

    public PageInfo queryForPage(String collectionName, Document queryFields, Document queryCnd, Document orderBy, PageInfo pageInfo) {
        return this.findForPage(collectionName, queryFields, queryCnd, orderBy, pageInfo);
    }

    private Bson buildFilter(Document queryDbObj) {
        Set<String> keySet = queryDbObj.keySet();
        List<Bson> filters = new ArrayList<>();
        keySet.stream().forEach(fieldName -> {
            Object fieldValue = queryDbObj.get(fieldName);
            Bson filter = Filters.eq(fieldName,fieldValue);
            filters.add(filter);
        });
        Bson filter = Filters.and(filters);
        return filter;
    }

    private List<Document> list(FindIterable<Document> dbIterable) {
        List<Document> list = new ArrayList();
        MongoCursor<Document> mongoCursor = dbIterable.iterator();
        try {
            while(mongoCursor.hasNext()) {
                Document object = mongoCursor.next();
                list.add(object);
            }
        } finally {
            mongoCursor.close();
        }
        return list;
    }
}
