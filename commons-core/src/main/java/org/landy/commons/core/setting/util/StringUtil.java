package org.landy.commons.core.setting.util;

import java.util.StringTokenizer;

public class StringUtil {

    public static final String DEFAULT_SPLIT = ";";

    public static String toCamelCase(String value) {
        return toCamelCase(value, null);
    }

    public static String toCamelCase(String value, StringBuilder sb) {
        boolean changed = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '_') {
                if (!changed) {
                    if (sb != null) {
                        sb.setLength(0);
                    } else {
                        sb = new StringBuilder();
                    }
                    // copy it over here
                    for (int j = 0; j < i; j++) {
                        sb.append(value.charAt(j));
                    }
                    changed = true;
                }
                sb.append(Character.toUpperCase(value.charAt(++i)));
            } else {
                if (changed) {
                    sb.append(c);
                }
            }
        }
        if (!changed) {
            return value;
        }
        return sb.toString();
    }

    public static String null2String(Object value) {
        if (value == null) return "";

        return value.toString().trim();
    }

    public static boolean nullEqual(String str1, String str2) {
        return null2String(str1).equals(null2String(str2));
    }

    public static String trim(String str){
        if (str == null) return "";
        return str.replaceAll("^[\\s" + ((char) 160) + "]*|[\\s" + ((char) 160) + "]*$", "");
    }

    public static boolean hasValue(String values, String value, String split) {
        StringTokenizer token = new StringTokenizer(values, split);
        while (token.hasMoreTokens()) {
            if (token.nextToken().equals(value))
                return true;
        }
        return false;
    }
}
