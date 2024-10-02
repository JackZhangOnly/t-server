package com.tstartup.tserver.util;

import cn.hutool.crypto.digest.DigestUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private static final String CHARSET_ENCODING = "UTF-8";
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    /**
     * 密钥
     */
    private static final String KEY = "202409eggplant11";
    /**
     * 偏移量
     */
    private static final String IV = "1234567890123412";

    /**
     * AES加密
     * @param sSrc
     * @return
     */
    public static String encrypt(String sSrc) {
        if(!StringUtils.hasText(sSrc)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            byte[] raw = KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());

            cipher.init(1, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(CHARSET_ENCODING));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  null;
    }

    /**
     * AES加密
     * @param sSrc
     * @return
     */
    public static String decrypt(String sSrc) {
        if(!StringUtils.hasText(sSrc)) {
            return null;
        }
        try {
            byte[] raw = KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            cipher.init(2, skeySpec, iv);
            byte[] encrypted = Base64.decodeBase64(sSrc);

            byte[] original = cipher.doFinal(encrypted);
            return new String(original, CHARSET_ENCODING);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
       // System.out.println(encrypt("1002"));

        System.out.println(DigestUtil.md5Hex("leisure16886"));
    }
}