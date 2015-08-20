package com.andadvert.util;

import com.andframe.application.AfApplication;
import com.andframe.helper.android.AfDesHelper;


public class DS {
	
	private static String key = "92abf7a35f196b52";//广告

	static AfDesHelper u = new AfDesHelper(AfApplication.getApp().getDesKey());
	
	public static String d(String v) {
		try {
			return u.decrypt(v);
		} catch (Throwable e) {
			return v;
		}
	}

	public static String ad() {
		return d(key);
	}
}
