package com.andframe.util.java;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Description: MD5 计算
 * @Author: scwang
 * @Version: V1.0, 2015-4-3 下午3:17:51
 * @Modified: 初次创建AfMD5类
 */
public class AfMD5 {
	
	public static String getMD5(byte[] byteArray) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Throwable e) {
			e.printStackTrace(); // handled
			return "";
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString().toLowerCase(Locale.ENGLISH);
	}

	public static String getMD5(String string) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Throwable e) {
			e.printStackTrace(); // handled
			return "";
		}
		byte[] md5Bytes = md5.digest(string.getBytes());
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString().toLowerCase(Locale.ENGLISH);
	}

	/**
	 * 获取单个文件的MD5值！
	 * @param file
	 * @return
	 */
	public static String getMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
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
	 * @param file
	 * @param listChild
	 *            ;true递归子目录中的文件
	 * @return
	 */
	public static Map<String, String> getDirMD5(File file, boolean listChild) {
		if (!file.isDirectory()) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String md5;
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && listChild) {
				map.putAll(getDirMD5(f, listChild));
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
		// TODO Auto-generated method stub
		if(vaule1 != null && vaule2 != null){
			vaule1 = vaule1.toUpperCase(Locale.ENGLISH);
			vaule2 = vaule2.toUpperCase(Locale.ENGLISH);
			return vaule1.equals(vaule2);
		}
		return false;
	}

	public static String getDoubleMD5(String string) {
		// TODO Auto-generated method stub
		return getMD5(getMD5(string));
	}

}
