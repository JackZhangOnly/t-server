package com.tstartup.tserver.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

public class Md5Utils {

    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }
}
