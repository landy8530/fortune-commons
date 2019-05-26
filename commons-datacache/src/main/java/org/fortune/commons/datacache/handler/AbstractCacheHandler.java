package org.fortune.commons.datacache.handler;

import org.fortune.commons.datacache.DataCacheFacade;

public abstract class AbstractCacheHandler {

    protected String getKey(String key) {
        return DataCacheFacade.getInstance().getCacheKeyPrefix() + key;
    }

}
