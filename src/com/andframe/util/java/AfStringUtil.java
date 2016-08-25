package com.andframe.util.java;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 字符串常用工具类
 * @author 树朾
 */
@SuppressWarnings("unused")
public class AfStringUtil {

	/**
	 * 验证为空
	 */
	public static boolean isEmpty(CharSequence string) {
		return TextUtils.isEmpty(string);
	}

	/**
	 * 验证不为空
	 */
	public static boolean isNotEmpty(CharSequence string) {
		return string != null && string.length() > 0;
	}

	/**
	 * 验证l,r是否相等(传null不会空指针异常，但是无法验证返回false)
	 * @return 是否相等
	 */
	public static boolean equals(CharSequence l,CharSequence r){
		return TextUtils.equals(l, r);
	}

	/**
	 * 验证输入的邮箱格式是否符合
	 * @return 是否合法
	 */
	public static boolean emailFormat(CharSequence email) {
		String pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		return Pattern.compile(pattern).matcher(email).find();
	}

	/**
	 * 验证 是否为中文
	 */
	public static boolean isChinese(CharSequence value) {
		return !isEmpty(value) && value.toString().matches("[\\u4E00-\\u9FA5]+");
	}

	/**
	 * 验证 是否为num个中文
	 */
	public static boolean isChineseNum(CharSequence value,int num) {
		return !isEmpty(value) && value.toString().matches("[\\u4E00-\\u9FA5]{" + num + "}");
	}

	/**
	 * 验证 是否为min-max个中文
	 */
	public static boolean isChineseNum(CharSequence value,int min,int max) {
		return !isEmpty(value) && value.toString().matches("[\\u4E00-\\u9FA5]{" + min + "," + max + "}");
	}

	/**
	 * 验证 是否为中文名称
	 */
	public static boolean isChineseName(CharSequence value) {
		return !isEmpty(value) && value.toString().matches("[\\u4E00-\\u9FA5]{2,6}");
	}
}
