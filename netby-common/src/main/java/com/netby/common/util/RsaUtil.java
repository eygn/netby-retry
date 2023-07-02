package com.netby.common.util;

import com.google.common.collect.Lists;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;

/**
 * RSA 加密解密工具
 *
 * @author: byg
 */
@Slf4j
@SuppressWarnings("unused")
public class RsaUtil {

    public enum SignType {
        /**
         * 验签类型
         */
        MD5withRSA, SHA1withRSA, SHA256withRSA
    }

    private static final String ALGORITHM = "RSA";

    private static final Integer LENGTH_SECRET = 1024;

    public static void main(String[] args) {
        String source = "hello world";

        String text = encryptByRsa(source);
        System.out.println("encryption1:" + text);

        String result = encryptByRsa2(source);
        System.out.println("encryption2:" + result);

        String en = encryptByPublicKey("123456",
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJzgdkNO6JsFkV1CXf2XNeFx98LuaJSf/k2pQY4puYvbeo1MbAgzq/8X+dD6kXWeGKq3MF7Ps37INmdbJjAAdyECAwEAAQ==");

        System.out.println("en:" + en);
        String desc = decryptByPrivateKey(en,
            "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAnOB2Q07omwWRXUJd/Zc14XH3wu5olJ/+TalBjim5i9t6jUxsCDOr/xf50PqRdZ4YqrcwXs+zfsg2Z1smMAB3IQIDAQABAkA4WbSBZvXMwYo+upkyfz9edOavgZ3VTUc2tEaU/03j+2gvfGfi+BX3zELjwrGU3G7/zehrvcOomYx09EE/qcNVAiEA5zEYDkHH3qsGm0PYw0b4IOjfD7CEV6wVKhphwGxR0dcCIQCtten4BFd0z195dHZGonaOTZ33tYCTKP+SgpQWTeNPxwIhAIuD3+1ymufvs4vtFJk7ca+CeGHidurlaNxAkW7qrzF/AiBdA49ockqyDWg+DJJYeB2rTPxM6y8RnV5cAPdyifoxeQIgRplo4+c5o2nGHrW5wFdJttDnZd1+iGmk12y1zLnJoec=");

        System.out.println("DESC:" + desc);
        desc = decryptByPrivateKey("BWUsXITX+p0SKiW1OW0Pq3SjVbF2asi4nxiIjJ3onf4sVV/3QdY1akSXcpQ6wJcBeogWUrRF1j/+fLnJXHUwQA==",
            "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAnOB2Q07omwWRXUJd/Zc14XH3wu5olJ/+TalBjim5i9t6jUxsCDOr/xf50PqRdZ4YqrcwXs+zfsg2Z1smMAB3IQIDAQABAkA4WbSBZvXMwYo+upkyfz9edOavgZ3VTUc2tEaU/03j+2gvfGfi+BX3zELjwrGU3G7/zehrvcOomYx09EE/qcNVAiEA5zEYDkHH3qsGm0PYw0b4IOjfD7CEV6wVKhphwGxR0dcCIQCtten4BFd0z195dHZGonaOTZ33tYCTKP+SgpQWTeNPxwIhAIuD3+1ymufvs4vtFJk7ca+CeGHidurlaNxAkW7qrzF/AiBdA49ockqyDWg+DJJYeB2rTPxM6y8RnV5cAPdyifoxeQIgRplo4+c5o2nGHrW5wFdJttDnZd1+iGmk12y1zLnJoec=");

        System.out.println("DESC:" + desc);
    }

    /**
     * 得到Key
     *
     * @return List<String>
     */
    public static List<String> initKey() {
        try {
            //keyPair生成器
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            //init
            generator.initialize(LENGTH_SECRET);

            //生成keyPair
            final KeyPair keyPair = generator.generateKeyPair();

            //get PrivateKey
            String privateKey = StringUtil.encodeBase64(keyPair.getPrivate().getEncoded());

            //get PublicKey
            String publicKey = StringUtil.encodeBase64(keyPair.getPublic().getEncoded());

            log.info("Public Key:{}", publicKey);
            log.info("Private Key:{}", privateKey);

            return Lists.newArrayList(publicKey, privateKey);
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

    /**
     * 私钥加密，公钥解密
     */
    static String encryptByRsa2(String source) {
        try {
            List<String> keyList = initKey();

            //get PublicKey
            assert keyList != null;
            final String publicKey = keyList.get(0);

            //get PrivateKey
            final String privateKey = keyList.get(1);

            //获取keyFactory
            String result = encryptByPrivateKey(source, privateKey);

            System.out.println("私钥加密结果：" + result);

            //公钥解密
            result = decryptByPublicKey(result, publicKey);

            System.out.println("公钥解密结果：" + result);
            return result;
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 公钥加密，私钥解密
     */
    static String encryptByRsa(String source) {
        try {
            List<String> keyList = initKey();

            //get PublicKey
            assert keyList != null;
            final String publicKey = keyList.get(0);

            //get PrivateKey
            final String privateKey = keyList.get(1);

            //获取keyFactory
            String result = encryptByPublicKey(source, publicKey);

            System.out.println("公钥加密结果：" + result);

            //公钥解密
            result = decryptByPrivateKey(result, privateKey);

            System.out.println("私钥解密结果：" + result);
            return result;
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param source 明文
     * @return 加密结果
     */
    public static String encryptByPublicKey(String source, String pubKey) {
        try {
            //公钥加密
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            final PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(StringUtil.decodeBase64ToByte(pubKey)));

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(source.getBytes());

            return StringUtil.encodeBase64(result);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param text 加密结果
     * @return 揭秘结果
     */
    public static String decryptByPrivateKey(String text, String privateKey) {
        try {
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            final PrivateKey secretKey = factory.generatePrivate(new PKCS8EncodedKeySpec(StringUtil.decodeBase64ToByte(privateKey)));

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] result = cipher.doFinal(StringUtil.decodeBase64ToByte(text));

            return new String(result);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 私钥加密
     *
     * @param source 明文
     * @return 加密结果
     */
    public static String encryptByPrivateKey(String source, String privateKey) {
        try {
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            final PrivateKey secretKey = factory.generatePrivate(new PKCS8EncodedKeySpec(StringUtil.decodeBase64ToByte(privateKey)));

            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] result = cipher.doFinal(source.getBytes());

            return StringUtil.encodeBase64(result);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 公钥解密
     *
     * @param text 加密结果
     * @return 揭秘结果
     */
    public static String decryptByPublicKey(String text, String publicKey) {
        try {
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            final PublicKey pubKey = factory.generatePublic(new X509EncodedKeySpec(StringUtil.decodeBase64ToByte(publicKey)));
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] result = cipher.doFinal(StringUtil.decodeBase64ToByte(text));
            return new String(result);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    /**
     * 公钥验签
     *
     * @param source 明文
     * @param sign   验证码
     * @return 加密结果
     */
    public static boolean verifySignByPublicKey(String source, String sign, String pubKey) {
        return verifySignByPublicKey(source, sign, pubKey, SignType.MD5withRSA);
    }

    /**
     * 公钥验签
     *
     * @param source 明文
     * @param sign   验证码
     * @return 加密结果
     */
    public static boolean verifySignByPublicKey(String source, String sign, String pubKey, SignType signType) {
        try {
            //公钥加密
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            final PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(StringUtil.decodeBase64ToByte(pubKey)));

            byte[] signed = StringUtil.decodeBase64ToByte(sign);
            Signature signetCheck = Signature.getInstance(signType.name());
            signetCheck.initVerify(publicKey);
            signetCheck.update(source.getBytes());
            return signetCheck.verify(signed);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return false;
    }

    /**
     * 根据私钥得到验签
     *
     * @param source     source
     * @param privateKey privateKey
     * @return String
     */
    public static String getSignByPrivateKey(String source, String privateKey) {
        return getSignByPrivateKey(source, privateKey, SignType.MD5withRSA);
    }

    /**
     * 根据私钥得到验签
     *
     * @param source     source
     * @param privateKey privateKey
     * @return String
     */
    public static String getSignByPrivateKey(String source, String privateKey, SignType signType) {
        try {
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            final PrivateKey secretKey = factory.generatePrivate(new PKCS8EncodedKeySpec(StringUtil.decodeBase64ToByte(privateKey)));

            // 用私钥对信息生成数字签名
            Signature signet = Signature
                .getInstance(signType.name());
            signet.initSign(secretKey);
            signet.update(source.getBytes(StandardCharsets.UTF_8));
            byte[] signed = signet.sign();
            return StringUtil.encodeBase64(signed);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }
}
