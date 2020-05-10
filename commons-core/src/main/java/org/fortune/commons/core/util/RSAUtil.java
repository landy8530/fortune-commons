package org.fortune.commons.core.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author: landy
 * @date: 2020/5/10 20:06
 * @description: RSA加解密工具类，实现公钥加密私钥解密和私钥加密公钥解密
 */
public class RSAUtil {
    private static final Logger logger = LoggerFactory.getLogger(DigestUtils.class);

    private static final int KEY_SIZE = 1024;
    private static final String  RSA_ALGORITHM = "RSA";
    private static final String  SIGNATURE_ALGORITHM= "MD5withRSA";


    private static KeyPair keyPair;

    static {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (NoSuchAlgorithmException e) {
            // Exception handler
            logger.error("初始化加解密算法对象失败", e);
        }
    }

    private static final String src = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws Exception {
        System.out.println("\n");
        RSAKeyPair keyPair = generateKeyPair();
        System.out.println("公钥：" + keyPair.getPublicKey());
        System.out.println("私钥：" + keyPair.getPrivateKey());
        System.out.println("\n");
        test1(keyPair, src);
        System.out.println("\n");
        test2(keyPair, src);
        System.out.println("\n");
    }

    /**
     * 公钥加密私钥解密
     */
    private static void test1(RSAKeyPair keyPair, String source) throws Exception {
        System.out.println("***************** 公钥加密私钥解密开始 *****************");
        String text1 = encryptByPublicKey(keyPair.getPublicKey(), source);
        String text2 = decryptByPrivateKey(keyPair.getPrivateKey(), text1);
        System.out.println("加密前：" + source);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }

        byte signatureData[] = RSAUtil.createSignature(text1.getBytes(), keyPair.getPrivateKey());

        System.out.println(DigestUtils.base64(signatureData));

        boolean status = RSAUtil.verifySignature(text1.getBytes(), signatureData, keyPair.getPublicKey());

        System.out.println(status);

        System.out.println("***************** 公钥加密私钥解密结束 *****************");
    }

    /**
     * 私钥加密公钥解密
     *
     * @throws Exception
     */
    private static void test2(RSAKeyPair keyPair, String source) throws Exception {
        System.out.println("***************** 私钥加密公钥解密开始 *****************");
        String text1 = encryptByPrivateKey(keyPair.getPrivateKey(), source);
        String text2 = decryptByPublicKey(keyPair.getPublicKey(), text1);
        System.out.println("加密前：" + source);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }

        byte signatureData[] = RSAUtil.createSignature(text1.getBytes(), keyPair.getPrivateKey());

        System.out.println(DigestUtils.base64(signatureData));

        boolean status = RSAUtil.verifySignature(text1.getBytes(), signatureData, keyPair.getPublicKey());

        System.out.println(status);

        System.out.println("***************** 私钥加密公钥解密结束 *****************");
    }

    /**
     * 公钥解密
     *
     * @param publicKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
        PublicKey publicKey = createPublicKey(publicKeyText);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PrivateKey privateKey = createPrivateKey(privateKeyText);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PrivateKey privateKey = createPrivateKey(privateKeyText);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyText
     * @param text
     * @return
     */
    public static String encryptByPublicKey(String publicKeyText, String text) throws Exception {
        PublicKey publicKey = createPublicKey(publicKeyText);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return
     */
    public static RSAKeyPair generateKeyPair() {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        RSAKeyPair rsaKeyPair = new RSAKeyPair(publicKeyString, privateKeyString);
        return rsaKeyPair;
    }

    /**
     * 功能简述: 使用私钥对加密数据创建数字签名.
     * @param cipherData     已经加密过的数据
     * @param privateKeyText    私钥
     * @return
     */
    public static byte[] createSignature(byte[] cipherData, String privateKeyText) {
        try {
            PrivateKey privateKey = createPrivateKey(privateKeyText);
            return createSignature(cipherData, privateKey);
        } catch (NoSuchAlgorithmException e) {
            logger.error("使用私钥对加密数据创建数字签名失败",e);
        } catch (InvalidKeySpecException e) {
            logger.error("使用私钥对加密数据创建数字签名失败",e);
        }
        return null;
    }

    /**
     * 功能简述: 使用私钥对加密数据创建数字签名.
     * @param cipherData     已经加密过的数据
     * @param privateKey    私钥
     * @return
     */
    public static byte[] createSignature(byte[] cipherData, PrivateKey privateKey) {
        try {
            Signature signature  = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(cipherData);
            return signature.sign();
        } catch (Exception e) {
            // Exception handler
            logger.error("使用私钥对加密数据创建数字签名失败",e);
        }
        return null;
    }

    /**
     * 功能简述: 使用公钥对数字签名进行验证.
     * @param cipherData  已经加密过的数据
     * @param signData  数字签名
     * @param publicKeyText 公钥
     * @return
     */
    public static boolean verifySignature(byte[] cipherData, byte[] signData, String publicKeyText) {
        try {
            PublicKey publicKey = createPublicKey(publicKeyText);
            return verifySignature(cipherData, signData, publicKey);
        } catch (Exception e) {
            // Exception handler
            logger.error("使用公钥对数字签名进行验证失败", e);
        }
        return false;
    }

    /**
     * 功能简述: 使用公钥对数字签名进行验证.
     * @param cipherData  已经加密过的数据
     * @param signData  数字签名
     * @param publicKey 公钥
     * @return
     */
    public static boolean verifySignature(byte[] cipherData, byte[] signData, PublicKey publicKey) {
        try {
            Signature signature  = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(cipherData);
            return signature.verify(signData);
        }
        catch (Exception e) {
            // Exception handler
            logger.error("使用公钥对数字签名进行验证失败", e);
        }
        return false;
    }

    /**
     * 功能简述: 创建私钥，用于RSA非对称加密.
     * @return
     */
    public static PrivateKey createPrivateKey(String privateKeyText) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        return privateKey;
    }

    /**
     * 功能简述: 创建公钥，用于RSA非对称加密.
     * @return
     */
    public static PublicKey createPublicKey(String publicKeyText) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        return publicKey;
    }

    /**
     * RSA密钥对对象
     */
    public static class RSAKeyPair {

        private String publicKey;
        private String privateKey;

        public RSAKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

    }

}
