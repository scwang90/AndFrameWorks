package com.andadvert.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DS {
	
	private static String key = "92abf7a35f196b52";//广告

	static AfDesHelper u = new AfDesHelper(getDesKey());
	
	public static String d(String v) {
		try {
			return u.decrypt(v);
		} catch (Throwable e) {
			return v;
		}
	}

	public static String e(String v) {
		try {
			return u.encrypt(v);
		} catch (Throwable e) {
			return v;
		}
	}

	public static String ad() {
		return d(key);
	}

	public static String getDesKey() {
		try {
			return new BigInteger(1, MessageDigest.getInstance("MD5").digest(new byte[0])).toString(16);
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}
}
