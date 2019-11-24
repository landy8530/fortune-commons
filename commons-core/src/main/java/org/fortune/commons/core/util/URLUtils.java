package org.fortune.commons.core.util;

/**
 * @author: landy
 * @date: 2019/11/23 00:08
 * @description:
 */
public class URLUtils {

    /**
     * url格式校验
     */
    public static boolean isUrl(String url) {
        if (url!=null && url.trim().length()>0 && url.startsWith("http")) {
            return true;
        }
        return false;
    }

}
