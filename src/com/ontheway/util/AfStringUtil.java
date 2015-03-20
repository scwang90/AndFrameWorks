package com.ontheway.util;

/**
 * @Description: 字符串常用工具类
 * @Author: scwang
 * @Version: V1.0, 2015-1-26 下午12:23:15
 * @Modified: 初次创建AfStringUtil类
 */
public class AfStringUtil {

	public static boolean isEmpty(String string) {
		// TODO Auto-generated method stub
		return string == null || string.trim().length() == 0;
	}
	
	public static boolean isNotEmpty(String string) {
		// TODO Auto-generated method stub
		return string != null && string.trim().length() > 0;
	}

	public static boolean equals(String l,String r){
		if (l != r) {
			if (l != null || r != null) {
				return l.equals(r);
			}
			return false;
		}
		return true;
	}
}
