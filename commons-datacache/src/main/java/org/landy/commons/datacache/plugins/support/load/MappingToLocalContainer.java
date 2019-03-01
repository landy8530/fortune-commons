package org.landy.commons.datacache.plugins.support.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappingToLocalContainer {

    private Logger logger = LoggerFactory.getLogger(MappingToLocalContainer.class);
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
