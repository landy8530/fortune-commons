package org.landy.commons.datacache.handler;

public abstract class LoadFromCache extends AbstractCacheHandler {

    public abstract <T> T load(String key);

}
