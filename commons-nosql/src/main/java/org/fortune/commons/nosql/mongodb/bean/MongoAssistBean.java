package org.fortune.commons.nosql.mongodb.bean;

import org.bson.Document;

public class MongoAssistBean {
    protected static final String MONGO_ID = "_id";

    protected Document document = new Document();

    public MongoAssistBean() {
    }

    public Document toDocument() {
        return this.document;
    }

}
