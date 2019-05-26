package org.fortune.commons.nosql.mongodb.bean;

import org.springframework.util.Assert;

public class MongoFieldBean extends MongoAssistBean {

    public MongoFieldBean show(String fieldName) {
        Assert.notNull(fieldName, "字段名称不为空");
        this.document.put(fieldName, 1);
        return this;
    }

    public MongoFieldBean show$id() {
        return this.show(MONGO_ID);
    }

    public MongoFieldBean hide(String fieldName) {
        Assert.notNull(fieldName, "字段名称不为空");
        this.document.put(fieldName, 0);
        return this;
    }

    public MongoFieldBean hide$id() {
        return this.hide(MONGO_ID);
    }

    public static MongoFieldBean createBean() {
        return new MongoFieldBean();
    }

}
