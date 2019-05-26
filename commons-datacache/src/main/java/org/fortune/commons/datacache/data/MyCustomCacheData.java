package org.fortune.commons.datacache.data;

import java.io.Serializable;
import java.util.Map;

public abstract class MyCustomCacheData <T> implements Map, Serializable {
    public MyCustomCacheData() {
    }

    public abstract boolean put(String var1, T var2);

    public abstract Object get(String var1);

    public abstract boolean contains(String var1);
}