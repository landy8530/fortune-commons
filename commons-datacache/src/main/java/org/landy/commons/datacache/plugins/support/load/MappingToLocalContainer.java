package org.landy.commons.datacache.plugins.support.load;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappingToLocalContainer {

    private static Map<String, LocalData> container = new ConcurrentHashMap(32);

    public MappingToLocalContainer() {
    }

    public static void put(String key, LocalData value) {
        container.put(key, value);
    }

    public static LocalData get(String key) {
        return (LocalData)container.get(key);
    }

}
