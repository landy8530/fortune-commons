package org.landy.commons.datacache.plugins.support.load;

import org.landy.commons.datacache.plugins.support.FetchOperate;

public class FromMemcached extends FromCache {

    private FetchOperate operate;

    public FromMemcached(FetchOperate operate) {
        this.operate = operate;
    }

    public Object fetch(String key) {
        return this.operate.get(key);
    }

}
