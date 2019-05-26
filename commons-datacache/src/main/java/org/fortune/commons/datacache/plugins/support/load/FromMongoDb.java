package org.fortune.commons.datacache.plugins.support.load;

import org.fortune.commons.datacache.plugins.support.FetchOperator;

public class FromMongoDb extends FromCache {
    private FetchOperator operate;

    public FromMongoDb(FetchOperator operate) {
        this.operate = operate;
    }

    public Object fetch(String key) {
        return this.operate.get(key);
    }
}
