package com.andframe.util.java;

import java.util.regex.Pattern;

/**
 * 字符串常用工具类
 * @author 树朾
 */
public class AfStringUtil {

	/**
	 * 验证为空
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		// TODO Auto-generated method stub
		return string == null || string.trim().length() == 0;
	}

	/**
	 * 验证不为空
	 * @param string
	 * @return
	 */
	public static boolean isNotEmpty(String string) {
		// TODO Auto-generated method stub
		return string != null && string.trim().length() > 0;
	}

	/**
	 * 验证l,r是否相等(传null不会空指针异常，但是无法验证返回false)
	 * @param l
	 * @param r
	 * @return 是否相等
	 */
	public static boolean equals(String l,String r){
		return ((l != null || r != null)) && l.equals(r);
	}

	/**
	 * 验证输入的邮箱格式是否符合
	 * @param email
	 * @return 是否合法
	 */
	public static boolean emailFormat(String email) {
		String pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		return Pattern.compile(pattern).matcher(email).find();
	}
	
}
