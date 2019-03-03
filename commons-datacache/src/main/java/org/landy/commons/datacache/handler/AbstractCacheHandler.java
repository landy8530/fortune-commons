package org.landy.commons.datacache.handler;

import org.landy.commons.datacache.DataCacheFacade;

public abstract class AbstractCacheHandler {

    protected String getKey(String key) {
        return DataCacheFacade.getInstance().getCacheKeyPrefix() + key;
    }

}
