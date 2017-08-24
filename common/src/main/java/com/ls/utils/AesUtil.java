package com.ls.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * Created by ls on 2017/8/24.
 */
public class AesUtil {
    private static final String KEY_TYPE = "AES";
    private static final String CIPHER_TYPE = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param content
     *            要加密的内容
     * @param password
     *            密码
     * @return 加密后的字符串
     */
    public static String encrypt(String content, String password) {
        String realKey = AesUtil.md5Encode(password);
        SecretKeySpec sks = new SecretKeySpec(AesUtil.hex2byte(realKey),
                KEY_TYPE);
        byte[] cipherByte = null;
        try {
            Cipher c1 = Cipher.getInstance(CIPHER_TYPE);
            c1.init(Cipher.ENCRYPT_MODE, sks);
            cipherByte = c1.doFinal(content.getBytes("UTF-8"));
            // 返回密文的十六进制形式
            return byte2hex(cipherByte);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 解密
     *
     * @param content
     *            要解密的内容
     * @param password
     *            密码
     * @return 解密结果
     */
    public static String decrypt(String content, String password) {
        String realKey = AesUtil.md5Encode(password);
        SecretKeySpec sks = new SecretKeySpec(AesUtil.hex2byte(realKey),
                KEY_TYPE);
        byte[] cipherByte = null;
        try {
            Cipher c1 = Cipher.getInstance(CIPHER_TYPE);
            c1.init(Cipher.DECRYPT_MODE, sks);
            cipherByte = c1.doFinal(hex2byte(content));
            return new String(cipherByte,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解密失败", e);
        }
    }

    /**
     * 生成MD5摘要
     */
    public static String md5Encode(String info) {
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(info.getBytes("UTF-8"));
            return byte2hex(alga.digest());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将二进制转化为16进制字符串
     */
    public static String byte2hex(byte[] b) {

        StringBuilder bder = new StringBuilder();
        String stmp = null;
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                bder.append("0");
            }
            bder.append(stmp);

        }
        return bder.toString().toUpperCase();
    }

    /**
     * 十六进制字符串转化为byte数组
     */
    private static byte[] hex2byte(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("参数长度不合法");
        }
        byte[] result = new byte[hex.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = uniteBytes(hex.charAt(i * 2), hex.charAt(i * 2 + 1));
        }
        return result;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
     *
     * @param c1
     * @param c2
     * @return byte
     */
    private static byte uniteBytes(char c1, char c2) {
        byte _b0 = Byte.decode("0x" + c1);
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + c2);
        return (byte) (_b0 ^ _b1);
    }
}
