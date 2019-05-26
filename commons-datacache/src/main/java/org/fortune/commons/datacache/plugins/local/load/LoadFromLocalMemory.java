package org.fortune.commons.datacache.plugins.local.load;

import org.fortune.commons.datacache.handler.LoadFromCache;
import org.fortune.commons.datacache.plugins.local.LocalCacheContainer;

public class LoadFromLocalMemory extends LoadFromCache {

    public <T> T load(String key) {
        return (T) LocalCacheContainer.get(super.getKey(key));
    }

}
