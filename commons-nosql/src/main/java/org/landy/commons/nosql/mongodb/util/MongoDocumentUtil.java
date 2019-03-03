package org.landy.commons.nosql.mongodb.util;

import org.bson.Document;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MongoDocumentUtil {

    public static void copy(Document source, Document target) {
        Set<String> keySet = source.keySet();
        Iterator<String> keyIterator = keySet.iterator();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object targetValue = target.get(key);
            Object sourceValue = source.get(key);
            if (target.containsKey(key)) {
                if (!Objects.equals(sourceValue, targetValue) && sourceValue != null) {
                    target.put(key, sourceValue);
                }
            } else {
                target.put(key, sourceValue);
            }
        }
    }

}
