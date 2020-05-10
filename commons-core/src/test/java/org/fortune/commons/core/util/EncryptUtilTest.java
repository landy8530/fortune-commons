package org.fortune.commons.core.util;

import org.fortune.commons.core.BaseJunit4Test;
import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.*;

/**
 * @author: landy
 * @date: 2020/5/10 17:53
 * @description:
 */
public class EncryptUtilTest extends BaseJunit4Test {

    /**
     * 功能简述: 测试MD5单向加密.
     * @throws Exception
     */
    @Test
    public void test01() throws Exception {
        String plainText = "Hello , world !";
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] cipherData = md5.digest(plainText.getBytes());
        StringBuilder builder = new StringBuilder();
        for(byte cipher : cipherData) {
            String toHexStr = Integer.toHexString(cipher & 0xff);
            builder.append(toHexStr.length() == 1 ? "0" + toHexStr : toHexStr);
        }
        System.out.println(builder.toString());
        //c0bb4f54f1d8b14caf6fe1069e5f93ad
    }

    /**
     * 功能简述: 使用BASE64进行双向加密/解密.
     * @throws Exception
     */
    @Test
    public void test02() throws Exception {
        BASE64Encoder encoder = new BASE64Encoder();
        BASE64Decoder decoder = new BASE64Decoder();
        String plainText = "Hello , world !";
        String cipherText = encoder.encode(plainText.getBytes());
        System.out.println("cipherText : " + cipherText);
        //cipherText : SGVsbG8gLCB3b3JsZCAh
        System.out.println("plainText : " +
                new String(decoder.decodeBuffer(cipherText)));
        //plainText : Hello , world !
    }

    /**
     * 功能简述: 使用DES对称加密/解密.
     * @throws Exception
     */
    @Test
    public void test03() throws Exception {
        String plainText = "Hello , world !";
        String key = "12345678";    //要求key至少长度为8个字符

        SecureRandom random = new SecureRandom();
        DESKeySpec keySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("des");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
        byte[] cipherData = cipher.doFinal(plainText.getBytes());
        System.out.println("cipherText : " + new BASE64Encoder().encode(cipherData));
        //PtRYi3sp7TOR69UrKEIicA==

        cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
        byte[] plainData = cipher.doFinal(cipherData);
        System.out.println("plainText : " + new String(plainData));
        //Hello , world !
    }

    /**
     * 功能简述: 使用RSA非对称加密/解密.
     * @throws Exception
     */
    @Test
    public void test04() throws Exception {
        String plainText = "Hello , world !";

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("rsa");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        Cipher cipher = Cipher.getInstance("rsa");
        SecureRandom random = new SecureRandom();

        cipher.init(Cipher.ENCRYPT_MODE, privateKey, random);
        byte[] cipherData = cipher.doFinal(plainText.getBytes());
        System.out.println("cipherText : " + new BASE64Encoder().encode(cipherData));
        //gDsJxZM98U2GzHUtUTyZ/Ir/NXqRWKUJkl6olrLYCZHY3RnlF3olkWPZ35Dwz9BMRqaTL3oPuyVq
        //sehvHExxj9RyrWpIYnYLBSURB1KVUSLMsd/ONFOD0fnJoGtIk+T/+3yybVL8M+RI+HzbE/jdYa/+
        //yQ+vHwHqXhuzZ/N8iNg=

        cipher.init(Cipher.DECRYPT_MODE, publicKey, random);
        byte[] plainData = cipher.doFinal(cipherData);
        System.out.println("plainText : " + new String(plainData));
        //Hello , world !

        Signature signature  = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(cipherData);
        byte[] signData = signature.sign();
        System.out.println("signature : " + new BASE64Encoder().encode(signData));
        //ADfoeKQn6eEHgLF8ETMXan3TfFO03R5u+cQEWtAQ2lRblLZw1DpzTlJJt1RXjU451I84v3297LhR
        //co64p6Sq3kVt84wnRsQw5mucZnY+jRZNdXpcbwh2qsh8287NM2hxWqp4OOCf/+vKKXZ3pbJMNT/4
        ///t9ewo+KYCWKOgvu5QQ=

        signature.initVerify(publicKey);
        signature.update(cipherData);
        boolean status = signature.verify(signData);
        System.out.println("status : " + status);
        //true
    }
}
