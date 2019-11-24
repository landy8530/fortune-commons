package org.fortune.commons.core.util;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class StringUtil {
    /**
     * 定义分割常量
     * #用于list中每个元素间的分割
     * |用于map中每一个kv对间的分割
     * =用于map中key与value间的分割
     */
    private static final String SEP1 = ",";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";

    public static final String DEFAULT_SPLIT = ";";

    public static String listToString(List<?> list,String delimiter) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(listToString((List<?>) list.get(i)));
                    sb.append(delimiter);
                } else if (list.get(i) instanceof Map) {
                    sb.append(mapToString((Map<?, ?>) list.get(i)));
                    sb.append(delimiter);
                } else {
                    sb.append(list.get(i));
                    sb.append(delimiter);
                }
            }
        }
        sb = sb.delete(sb.length()-1,sb.length());
        return sb.toString();
    }
    /**
     * List转换String,以逗号隔开
     *
     * @param list :需要转换的List
     * @return String转换后的字符串
     */
    public static String listToString(List<?> list) {
        return listToString(list,SEP1);
    }

    /**
     * Map转换String
     *
     * @param map :需要转换的Map
     * @return String转换后的字符串
     */
    public static String mapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + listToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1 + mapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP3 + value.toString());
                sb.append(SEP2);
            }
        }
        return sb.toString();
    }

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

    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    /**
     * 判断是否含有中文字符
     * @param str
     * @return
     * @author:zhanbs
     */
    public static final boolean containsChineseChar(String str) {
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static int compare(String text1, String text2) {
        if (text1 == null && text2 == null) {
            return 0;
        } else if (text1 != null && text2 == null) {
            return 1;
        } else {
            return text1 == null ? -1 : text1.compareTo(text2);
        }
    }

    public static boolean hasText(String text) {
        if (text == null) {
            return false;
        } else {
            for(int i = 0; i < text.length(); ++i) {
                if (!Character.isWhitespace(text.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean equals(String text1, String text2) {
        if (text1 == null) {
            return text2 == null;
        } else {
            return text1.equals(text2);
        }
    }

    public static String truncate(String text, int maxLength) {
        if (text == null) {
            return null;
        } else {
            return text.length() <= maxLength ? text : text.substring(0, maxLength);
        }
    }
}
