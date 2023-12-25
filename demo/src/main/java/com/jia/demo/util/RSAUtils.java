package com.jia.demo.util;/**
 * @author ChenJia
 * @create 2023-12-19 10:36
 */

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 *@ClassName RSAUtils
 *@Description TODO
 *@Author jia
 *@Date 2023/12/19 10:36
 *@Version 1.0
 **/
@Slf4j
public class RSAUtils {
    private static final String ALGORITHM = "RSA";

    /**
     * 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 512;
    // 公钥
    private static final String PUBLIC_KEY = "RSAPublicKey";

    // 私钥
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    //公钥
    public static final String PBULICKEY = "";
    //私钥
    public static final String PRIVATEKEY = "";

    /**
     * RSA解密
     *
     * @param securityBASE64 用RSA加密后的字符串
     * @param key            私钥
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String securityBASE64, String key) throws Exception {
        Key privateKey = getPrivateKeyFromString(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataDecrypt = cipher.doFinal(Base64.decodeBase64(securityBASE64));

        return new String(dataDecrypt);
    }

    public static String decrypt(String securityBASE64) throws Exception {
        return decrypt(securityBASE64, PRIVATEKEY);
    }

    /**
     * 将私钥字符串转成私钥对象
     *
     * @param keyStr
     * @return
     */
    private static Key getPrivateKeyFromString(String keyStr) {
        try {
            // 解密密钥
            byte[] keyBytes = Base64.decodeBase64(keyStr);
            // 取私钥
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            Key key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            return key;
        } catch (Exception e) {
            log.error("",e);
        }

        return null;
    }

    /**
     * 利用RSA公钥加密text
     *
     * @param text
     * @param publicKeyStr
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encrypt(String text, String publicKeyStr) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        Key publicKey = getPublicKeyFromString(publicKeyStr);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] dataEncrypt = cipher.doFinal(text.getBytes());

        return Base64.encodeBase64String(dataEncrypt);
    }

    public static String encrypt(String text) throws Exception {
        return encrypt(text, PBULICKEY);
    }

    public static String encrypBySegment(String text, String publicKeyStr) throws Exception {
        StringBuilder s = new StringBuilder();
        String data[] = StrUtil.cut(text,53);
        int len = data.length;
        for(int i =0;i<len;i++){
            s.append(encrypt(data[i],publicKeyStr)).append("@");
        }
        String str = s.toString();
        return str.substring(0, str.length() - 1);
    }

    /**
     * 将公钥字符串转成公钥对象
     *
     * @param keyStr
     * @return
     */
    private static Key getPublicKeyFromString(String keyStr) {
        try {
            //对公钥解密
            byte[] keyBytes = Base64.decodeBase64(keyStr);
            //取公钥
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            return publicKey;
        } catch (Exception e) {
            log.error("",e);
        }

        return null;
    }


    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        // 实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        // 初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    public static void main(String[] args) throws Exception {
        // 初始化密钥, 生成密钥对
//		Map<String, Object> keyMap = RSAUtils.initKey();
//		byte[] pub=RSAUtils.getPublicKey(keyMap);
//
//		byte[] prv=RSAUtils.getPrivateKey(keyMap);
//
//		System.out.println("公钥：/n" + Base64.encodeBase64String(pub));
//		System.out.println("私钥：/n" + Base64.encodeBase64String(prv));

//        String a = encrypt("o8leMjhIxrA1HAow4czUsio8GlQY");
        String a = encrypt("ojzRA6Y4BrWug2mawiQlHEw4zWTc");
        System.out.println(a);
        String b = decrypt("237cbce44a711f0489a8c873a4511df3");
        System.out.println(b);
    }
}


