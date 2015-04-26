package com.andadvert.util;

import com.andframe.application.AfApplication;
import com.andframe.helper.android.AfDesHelper;


public class DS {
	
	private static String key = "92abf7a35f196b52";//¹ã¸æ

	static AfDesHelper u = new AfDesHelper(AfApplication.getApp().getDesKey());
	
	public static String d(String v) {
		// TODO Auto-generated method stub
		try {
			return u.decrypt(v);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			return v;
		}
	}

	public static String ad() {
		// TODO Auto-generated method stub
		return d(key);
	}
}
