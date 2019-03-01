package org.landy.commons.datacache.plugins.support.load;

public abstract class FromCache {

    public abstract <T> T  fetch(String key);

}
