package com.andframe.util.java;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * MD5 计算
 *
 * @author 树朾
 */
@SuppressWarnings("unused")
public class AfMD5 {

    public static String getMD5(byte[] byteArray) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Throwable e) {
            e.printStackTrace(); // handled
            return "";
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toLowerCase(Locale.ENGLISH);
    }

    public static String getMD5(String string) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Throwable e) {
            e.printStackTrace(); // handled
            return "";
        }
        byte[] md5Bytes = md5.digest(string.getBytes());
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toLowerCase(Locale.ENGLISH);
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file 文件
     */
    public static String getMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest ;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取文件夹中文件的MD5值
     *
     * @param file 文件
     * @param listChild ;true递归子目录中的文件
     * @return MD5值
     */
    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String md5;
        File files[] = file.listFiles();
        for (File f : files) {
            if (f.isDirectory() && listChild) {
                //noinspection ConstantConditions
                map.putAll(getDirMD5(f, true));
            } else {
                md5 = getMD5(f);
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }

    public static boolean equals(String vaule1, String vaule2) {
        if (vaule1 != null && vaule2 != null) {
            vaule1 = vaule1.toUpperCase(Locale.ENGLISH);
            vaule2 = vaule2.toUpperCase(Locale.ENGLISH);
            return vaule1.equals(vaule2);
        }
        return false;
    }

    public static String getDoubleMD5(String string) {
        return getMD5(getMD5(string));
    }

}
