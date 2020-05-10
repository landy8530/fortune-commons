package org.fortune.commons.core.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.Charset;
import java.security.*;

/**
 * @author: landy
 * @date: 2019/11/20 22:36
 * @description:
 */
public class DigestUtils {

    private static final Logger logger = LoggerFactory.getLogger(DigestUtils.class);

    private static final String  DES_ALGORITHM= "des";

    private static SecureRandom random;

    static {
        random = new SecureRandom();
    }
    /**
     * 功能简述: 使用BASE64进行加密.
     * @param plainData 明文数据
     * @return 加密之后的文本内容
     */
    public static String base64(String plainData) {
        return base64(plainData.getBytes(Charset.defaultCharset()));
    }
    /**
     * 功能简述: 使用BASE64进行加密.
     * @param plainData 明文数据
     * @return 加密之后的文本内容
     */
    public static String base64(byte[] plainData) {
        return new String(Base64.encodeBase64(plainData), Charset.defaultCharset());
    }
    /**
     * 功能简述: 使用BASE64进行解密.
     * @param cipherText 密文文本
     * @return 解密之后的数据
     */
    public static byte[] decodeBase64(String cipherText) {
        return decodeBase64(cipherText.getBytes(Charset.defaultCharset()));
    }
    /**
     * 功能简述: 使用BASE64进行解密.
     * @param cipherText 密文文本
     * @return 解密之后的数据
     */
    public static byte[] decodeBase64(byte[] cipherText) {
        return Base64.decodeBase64(cipherText);
    }
    /**
     * 功能简述: 使用md5进行单向加密.
     */
    public static String md5(String text) {
        return md5(text.getBytes(Charset.defaultCharset()));
    }
    /**
     * 功能简述: 使用md5进行单向加密.
     */
    public static String md5(byte[] bytes) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(bytes);
    }

    public static String sha1(String text) {
        return sha1(text.getBytes(Charset.defaultCharset()));
    }

    public static String sha1(byte[] bytes) {
        return org.apache.commons.codec.digest.DigestUtils.sha1Hex(bytes);
    }

    /**
     * 功能简述: 使用DES算法进行加密.
     * @param plainData 明文数据
     * @param key   加密密钥 //要求key至少长度为8个字符
     * @return
     */
    public static byte[] encryptDES(byte[] plainData, String key) {
        return processCipher(plainData, createSecretKey(key), Cipher.ENCRYPT_MODE, DES_ALGORITHM);
    }

    /**
     * 功能简述: 使用DES算法进行解密.
     * @param cipherData    密文数据
     * @param key   解密密钥 //要求key至少长度为8个字符
     * @return
     */
    public static byte[] decryptDES(byte[] cipherData, String key) {
        return processCipher(cipherData, createSecretKey(key), Cipher.DECRYPT_MODE, DES_ALGORITHM);
    }

    /**
     * 功能简述: 根据key创建密钥SecretKey.
     * @param key
     * @return
     */
    private static SecretKey createSecretKey(String key) {
        SecretKey secretKey = null;
        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
            secretKey = keyFactory.generateSecret(keySpec);
        }
        catch (Exception e) {
            // Exception handler
            logger.error("根据key创建密钥SecretKey失败", e);
        }
        return secretKey;
    }

    /**
     * 功能简述: 加密/解密处理流程.
     * @param processData   待处理的数据
     * @param key   提供的密钥
     * @param opsMode   工作模式
     * @param algorithm   使用的算法
     * @return
     */
    private static byte[] processCipher(byte[] processData, Key key, int opsMode, String algorithm) {
        try{
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(opsMode, key, random);
            return cipher.doFinal(processData);
        }
        catch (Exception e) {
            // Exception handler
            logger.error("加密/解密处理流程失败", e);
        }
        return null;
    }

    private static final String src = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws Exception {
        byte[] cdata = encryptDES(src.getBytes(), "landy8530");
        System.out.println(base64(cdata));
        byte[] ddata = decryptDES(cdata, "landy8530");

        System.out.println(new String(ddata));
    }


}
