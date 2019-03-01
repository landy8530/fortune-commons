package org.landy.commons.datacache.plugins.local.load;

import org.landy.commons.datacache.handler.LoadFromCache;
import org.landy.commons.datacache.plugins.local.LocalCacheContainer;

public class LoadFromLocalMemory extends LoadFromCache {

    public <T> T load(String key) {
        return (T) LocalCacheContainer.get(super.getKey(key));
    }

}
