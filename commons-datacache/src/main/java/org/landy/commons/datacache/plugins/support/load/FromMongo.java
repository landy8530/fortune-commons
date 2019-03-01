package org.landy.commons.datacache.plugins.support.load;

import org.landy.commons.datacache.plugins.support.FetchOperate;

public class FromMongo extends FromCache {
    private FetchOperate operate;

    public FromMongo(FetchOperate operate) {
        this.operate = operate;
    }

    public Object fetch(String key) {
        return this.operate.get(key);
    }
}
