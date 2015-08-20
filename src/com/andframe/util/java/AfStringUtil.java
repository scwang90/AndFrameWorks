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
		return string == null || string.trim().length() == 0;
	}

	/**
	 * 验证不为空
	 * @param string
	 * @return
	 */
	public static boolean isNotEmpty(String string) {
		return string != null && string.trim().length() > 0;
	}

	/**
	 * 验证l,r是否相等(传null不会空指针异常，但是无法验证返回false)
	 * @param l
	 * @param r
	 * @return 是否相等
	 */
	public static boolean equals(String l,String r){
		return ((l != null && r != null)) && l.equals(r);
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

	/**
	 * 验证 是否为中文
	 * @param value
	 * @return boolean
	 */
	public static boolean isChinese(String value) {
		if(isEmpty(value)){
			return false;
		}
		return value.matches("[\\u4E00-\\u9FA5]+");
	}

	/**
	 * 验证 是否为num个中文
	 * @param value
	 * @return boolean
	 */
	public static boolean isChineseNum(String value,int num) {
		if(isEmpty(value)){
			return false;
		}
		return value.matches("[\\u4E00-\\u9FA5]{"+num+"}");
	}

	/**
	 * 验证 是否为min-max个中文
	 * @param value
	 * @return boolean
	 */
	public static boolean isChineseNum(String value,int min,int max) {
		if(isEmpty(value)){
			return false;
		}
		return value.matches("[\\u4E00-\\u9FA5]{"+min+","+max+"}");
	}

	/**
	 * 验证 是否为中文名称
	 * @param value
	 * @return boolean
	 */
	public static boolean isChineseName(String value) {
		if(isEmpty(value)){
			return false;
		}
		return value.matches("[\\u4E00-\\u9FA5]{2,6}");
	}
}
