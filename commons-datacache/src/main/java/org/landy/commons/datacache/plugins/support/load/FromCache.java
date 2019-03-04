package org.landy.commons.datacache.plugins.support.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FromCache {
    Logger LOGGER  = LoggerFactory.getLogger(FromCache.class);

    public abstract <T> T  fetch(String key);
}
