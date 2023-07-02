package com.netby.common.util;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 对称算法加密/解密工具类
 *
 * @author: byg
 * @date 2022/4/17 19:11
 */
@SuppressWarnings("unused")
public class AesUtil {

    public static String encrypt(String sSrc, String sKey) {
        return encrypt(sSrc, sKey, null);
    }

    /**
     * 加密
     *
     * @param sSrc 待加密字符串
     * @param sKey 密钥
     * @param sIv  偏移量
     */
    public static String encrypt(String sSrc, String sKey, String sIv) {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        try {
            // "算法/模式/补码方式"
            String type = "AES/ECB/PKCS5Padding";
            if (StringUtil.isNotEmpty(sIv)) {
                type = "AES/CBC/PKCS5Padding";
            }
            Cipher cipher = Cipher.getInstance(type);
            if (StringUtil.isNotEmpty(sIv)) {
                byte[] bytes = sIv.getBytes();
                //使用CBC模式，需要一个向量iv，可增加加密算法的强度
                IvParameterSpec iv = new IvParameterSpec(bytes);
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            }
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
            // 此处使用BASE64做转码
            return StringUtil.encodeBase64(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String sSrc, String sKey) {
        return decrypt(sSrc, sKey, null);
    }

    /**
     * 解密
     *
     * @param sSrc 待解密字符串
     * @param sKey 密钥
     * @param sIv  偏移量
     */
    public static String decrypt(String sSrc, String sKey, String sIv) {
        try {
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            // "算法/模式/补码方式"
            String type = "AES/ECB/PKCS5Padding";
            if (StringUtil.isNotEmpty(sIv)) {
                type = "AES/CBC/PKCS5Padding";
            }
            Cipher cipher = Cipher.getInstance(type);
            if (StringUtil.isNotEmpty(sIv)) {
                byte[] bytes = sIv.getBytes();
                //使用CBC模式，需要一个向量iv，可增加加密算法的强度
                IvParameterSpec iv = new IvParameterSpec(bytes);
                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
            }
            // 先用base64解码
            byte[] encrypted1 = StringUtil.decodeBase64ToByte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        System.out.println(decrypt(
                "pFNcv3XOazyxD5rjvS+Z0UmLBr99L3ySU9F6x/ZkweuOY/xdsKQxl6zEFND4TkI6rC0hTNOZMXhRDQg0W6PRyuXVYnx7p8KzrO7d7ow2YngHZ2Ezsy7SYcJXK4QUwz6IqhAZbWZt9mdrC6E5ZLxMEtoIpM9r72omp6aUNPuVE9y++eU4GE6zucCtAxcamt/3hCVAJN8SRKoMbubVXaD6/vyKoLAJVIXggh2TEmPOc1MD9mTu+pzDCr79o8t2f2YKLzjalxxraH/Q+Auh8T+SorHZ99XlCjPSnLGztaHD7sLcciZt+Nn86vk1ioRqSaaEM2Th5OuizdYyi1+67bimpQ==",
                "1234567812345679", "1243567812346579"));
    }
}
