package org.landy.commons.nosql.mongodb.bean;

import org.springframework.util.Assert;

public class MongoOrderBean extends MongoAssistBean {
    public MongoOrderBean asc(String fieldName) {
        Assert.notNull(fieldName, "字段名称不为空");
        this.document.put(fieldName, 1);
        return this;
    }

    public MongoOrderBean desc(String fieldName) {
        Assert.notNull(fieldName, "字段名称不为空");
        this.document.put(fieldName, -1);
        return this;
    }

    public static MongoOrderBean createBean() {
        return new MongoOrderBean();
    }
}
