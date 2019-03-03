package org.landy.commons.nosql.mongodb.bean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class MongoConditionBean extends MongoAssistBean {
    public MongoConditionBean append(String fieldName, Object value) {
        Assert.notNull(fieldName, "字段名称不为空");
        String val = "";
        if (value != null) {
            val = value.toString();
        }
        if (StringUtils.isNotBlank(val)) {
            val = val.trim();
            this.document.put(fieldName, val);
        }
        return this;
    }

    public MongoConditionBean append$Id(Object value) {
        return this.append(MONGO_ID, value);
    }

    public static MongoConditionBean createBean() {
        return new MongoConditionBean();
    }

}
