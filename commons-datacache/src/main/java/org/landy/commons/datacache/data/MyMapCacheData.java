package org.landy.commons.datacache.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyMapCacheData extends MyCustomCacheData<Map<String, Object>> implements Serializable {
    private Map<String, Object> data;

    public MyMapCacheData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean put(String key, Map<String, Object> data) {
        this.data.put(key, data);
        return true;
    }

    public Object get(String key) {
        return this.data.get(key);
    }

    public boolean contains(String key) {
        return this.data.containsKey(key);
    }

    public String toString() {
        return "MyMapCacheData [data=" + this.data + "]";
    }

    public int size() {
        return this.data.size();
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.data.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.data.containsValue(value);
    }

    public Object get(Object key) {
        return this.data.get(key);
    }

    public Object put(Object key, Object value) {
        return this.data.put(key.toString(), value);
    }

    public Object remove(Object key) {
        return this.data.remove(key);
    }

    public void putAll(Map m) {
        this.data.putAll(m);
    }

    public void clear() {
        this.data.clear();
    }

    public Set keySet() {
        return this.data.keySet();
    }

    public Collection values() {
        return this.data.values();
    }

    public Set entrySet() {
        return this.data.entrySet();
    }
}
