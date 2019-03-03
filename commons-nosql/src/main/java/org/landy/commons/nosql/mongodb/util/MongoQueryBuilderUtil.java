package org.landy.commons.nosql.mongodb.util;

import org.springframework.util.StringUtils;

public class MongoQueryBuilderUtil {

    public static String like(String value) {
        if (StringUtils.isEmpty(value)) {
            value = "";
        }
        return "^.*" + value + ".*$";
    }

    public static String leftLike(String value) {
        if (StringUtils.isEmpty(value)) {
            value = "";
        }
        return "^.*" + value + "$";
    }

    public static String rightLike(String value) {
        if (StringUtils.isEmpty(value)) {
            value = "";
        }
        return "^" + value + ".*$";
    }

}
