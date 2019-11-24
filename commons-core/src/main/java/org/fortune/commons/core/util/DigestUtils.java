package org.fortune.commons.core.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

/**
 * @author: landy
 * @date: 2019/11/20 22:36
 * @description:
 */
public class DigestUtils {

    public static String base64(String text) {
        return base64(text.getBytes(Charset.defaultCharset()));
    }

    public static String base64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes), Charset.defaultCharset());
    }

    public static byte[] decodeBase64(String base64Text) {
        return decodeBase64(base64Text.getBytes(Charset.defaultCharset()));
    }

    public static byte[] decodeBase64(byte[] base64Bytes) {
        return Base64.decodeBase64(base64Bytes);
    }

    public static String md5(String text) {
        return md5(text.getBytes(Charset.defaultCharset()));
    }

    public static String md5(byte[] bytes) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(bytes);
    }

    private DigestUtils() {
    }

}
