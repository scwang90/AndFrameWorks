package com.andadvert;

import java.util.Locale;

import android.content.Context;

import com.andframe.application.AfExceptionHandler;
import com.andadvert.AdvertAdapter;

public class OnlineKey {
	
	/**自定义广告单价**/
	public static final String KEY_UNITPRICE = "KEY_UNITPRICE";

	/**广告删除时间长度（天 浮点数）**/
	public static final String KEY_ADREMOVETIME = "KEY_ADREMOVETIME";

	/**广告删除所需点数（整数）**/
	public static final String KEY_ADREMOVENEED = "KEY_ADREMOVENEED";

	/**显示自家更多**/
	public static final String KEY_SHOWMORE = "KEY_SHOWMORE";
	
	/**是否隐藏广告**/
	public static final String KEY_ISHIDEAD = "KEY_ISHIDEAD";

	/**刷量地址**/
	public static final String KEY_WEBDOWNLOAD = "KEY_WEBDOWNLOAD";

	/**在线配置**/
	public static final String KEY_DEPLOY = "KEY_DEPLOY";
	/**
	 * 获取在线配置 Boolean 值
	 * @param context 
	 * @param key
	 * @param defaul 默认值
	 * @param detail 获取内容描述
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,boolean defaul,String detail) {
		// TODO Auto-generated method stub
		String bool = AdvertAdapter.getInstance().getConfig(context, key, "");
		if (bool != null && bool.length() > 0) {
			try {
				return "true".equals(bool.toLowerCase(Locale.ENGLISH));
			} catch (Throwable e) {
				// TODO: handle exception
				AfExceptionHandler.handler(e, "获取服务器【"+detail+"】出现异常");
			}
		}
		return defaul;
	}

	/**
	 * 获取在线配置 Integer 值
	 * @param context 
	 * @param key
	 * @param defaul 默认值
	 * @param detail 获取内容描述
	 * @return
	 */
	public static int getInteger(Context context, String key,int defaul,String detail) {
		// TODO Auto-generated method stub
		String integer = AdvertAdapter.getInstance().getConfig(context,key, "");
		if (integer != null && integer.length() > 0) {
			try {
				return Integer.valueOf(integer);
			} catch (Throwable e) {
				// TODO: handle exception
				AfExceptionHandler.handler(e, "获取服务器【"+detail+"】出现异常");
			}
		}
		return defaul;
	}

	/**
	 * 获取在线配置 Integer 值
	 * @param context 
	 * @param key
	 * @param defaul 默认值
	 * @param detail 获取内容描述
	 * @return
	 */
	public static double getDouble(Context context, String key,double defaul,String detail) {
		// TODO Auto-generated method stub
		String integer = AdvertAdapter.getInstance().getConfig(context,key, "");
		if (integer != null && integer.length() > 0) {
			try {
				return Double.valueOf(integer);
			} catch (Throwable e) {
				// TODO: handle exception
				AfExceptionHandler.handler(e, "获取服务器【"+detail+"】出现异常");
			}
		}
		return defaul;
	}

	/**
	 * 获取在线配置 http 连接
	 * @param context 
	 * @param key
	 * @param defaul 默认值
	 * @param detail 获取内容描述
	 * @return
	 */
	public static String getHttp(Context context, String key,String defaul,String detail) {
		// TODO Auto-generated method stub
		String http = AdvertAdapter.getInstance().getConfig(context,key, "");
		if (http != null && http.startsWith("http://")) {
			return http;
		}
		return defaul;
	}

	/**
	 * 获取在线配置 字符串
	 * @param context 
	 * @param key
	 * @param defaul 默认值
	 * @param detail 获取内容描述
	 * @return
	 */
	public static String getString(Context context, String key,String defaul,String detail) {
		// TODO Auto-generated method stub
		String value = AdvertAdapter.getInstance().getConfig(context,key, "");
		if (value != null && value.length() > 0) {
			return value;
		}
		return defaul;
	}
}
