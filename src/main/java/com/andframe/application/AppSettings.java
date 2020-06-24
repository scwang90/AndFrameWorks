package com.andframe.application;

import android.content.Context;

import com.andframe.caches.AfJsonCache;

@SuppressWarnings("WeakerAccess")
public class AppSettings {

	protected static final String EM_SHARE_NAME = "AppSettings";

	//提示声音
	public static final String KEY_BL_IS_NOTIFY_SOUND = "KEY_BL_IS_NOTIFY_SOUND";
	//2G/3G网络无图模式
	public static final String KEY_BL_IS_NO_IMAGE_MODE = "KEY_BL_IS_NO_IMAGE_MODE";
	//是否开启自动更新
	public static final String KEY_BL_IS_AUTO_UPDATE = "KEY_BL_IS_AUTO_UPDATE";
	//是否开启自动登录
	public static final String KEY_BL_IS_AUTO_LOGIN = "KEY_BL_IS_AUTO_LOGIN";
	//登录账户
	public static final String KEY_ST_LOGIN_ACCOUNT = "KEY_ST_LOGIN_ACCOUNT";
	//登录密码
	public static final String KEY_ST_LOGIN_PASSWORD = "KEY_ST_LOGIN_PASSWORD";
	//使用GPS定位
	public static final String KEY_BL_IS_USE_GPS_FIXED = "KEY_BL_IS_USE_GPS_FIXED";
	//定位时间间隔
	public static final String KEY_LG_FIXED_DURATION = "KEY_IT_FIXED_DURATION";
	//数据服务器域名或IP
	public static final String KEY_ST_DATA_SERVER_IP = "KEY_ST_DATA_SERVER_IP";
	//数据服务器端口
	public static final String KEY_IT_DATA_SERVER_PORT = "KEY_IT_DATA_SERVER_PORT";

	protected AfJsonCache mShared = null;

	protected static AppSettings mInstance;

    protected AppSettings(Context context) {
    	int mode = Context.MODE_PRIVATE;
    	mShared = new AfJsonCache(context, EM_SHARE_NAME,mode);
    	setDefault(KEY_ST_LOGIN_ACCOUNT, "");
    	setDefault(KEY_ST_LOGIN_PASSWORD, "");
    	setDefault(KEY_BL_IS_NO_IMAGE_MODE, false);
    	setDefault(KEY_BL_IS_NOTIFY_SOUND, true);
    	setDefault(KEY_BL_IS_USE_GPS_FIXED, true);
    	setDefault(KEY_BL_IS_AUTO_UPDATE, true);
    	setDefault(KEY_BL_IS_AUTO_LOGIN, false);
    	setDefault(KEY_ST_DATA_SERVER_IP, getDefaultDataServerIP());
    	setDefault(KEY_IT_DATA_SERVER_PORT, getDefaultDataServerPort());
    	setDefault(KEY_LG_FIXED_DURATION, 10*60*1000);
	}

	protected int getDefaultDataServerPort() {
		return 8088;
	}

	protected String getDefaultDataServerIP() {
		return "127.0.0.1";
	}

	protected void setDefault(String key, Object defaul) {
		if(mShared.isEmpty(key)){
			mShared.put(key, defaul);
		}
	}

	public static AppSettings getInstance(){
		if(mInstance == null){
			mInstance = AfApp.get().newAppSetting();
		}
		return mInstance;
	}

	public long getFixedDuration(){
		return mShared.get(KEY_LG_FIXED_DURATION,0l, long.class);
	}
	
	public void setFixedDuration(long value) {
		mShared.put(KEY_LG_FIXED_DURATION, value);
	}
	
	public boolean isNoImage() {
		return mShared.get(KEY_BL_IS_NO_IMAGE_MODE,false, boolean.class);
	}

	public void setNoImage(boolean value) {
		mShared.put(KEY_BL_IS_NO_IMAGE_MODE, value);
	}

	public boolean isNotifySound() {
		return mShared.get(KEY_BL_IS_NOTIFY_SOUND,false, boolean.class);
	}

	public void setNotifySound(boolean value) {
		mShared.put(KEY_BL_IS_NOTIFY_SOUND, value);
	}

	public boolean isUseGpsFixed() {
		return mShared.get(KEY_BL_IS_USE_GPS_FIXED,false, boolean.class);
	}
	
	public void setUserGpsFixed(boolean value) {
		mShared.put(KEY_BL_IS_USE_GPS_FIXED, value);
	}

	public boolean isAutoUpdate() {
		return mShared.get(KEY_BL_IS_AUTO_UPDATE,false, boolean.class);
	}
	
	public void setAutoUpdate(boolean value) {
		mShared.put(KEY_BL_IS_AUTO_UPDATE, value);
	}

	public boolean isAutoLogin() {
		return mShared.get(KEY_BL_IS_AUTO_LOGIN,false, boolean.class);
	}
	
	public void setAutoLogin(boolean value) {
		mShared.put(KEY_BL_IS_AUTO_LOGIN, value);
	}

	public String getLoginAccount() {
		return mShared.get(KEY_ST_LOGIN_ACCOUNT,"", String.class);
	}
	
	public void setLoginAccount(String value) {
		mShared.put(KEY_ST_LOGIN_ACCOUNT, value);
	}

	public String getLoginPassword() {
		return mShared.get(KEY_ST_LOGIN_PASSWORD,"", String.class);
	}
	
	public void setLoginPassword(String value) {
		mShared.put(KEY_ST_LOGIN_PASSWORD, value);
	}

	public String getDataServerIP() {
		return mShared.get(KEY_ST_DATA_SERVER_IP, String.class);
	}

	public void setDataServerIP(String ip) {
		mShared.put(KEY_ST_DATA_SERVER_IP, ip);
	}
	
	public int getDataServerPort() {
		return mShared.get(KEY_IT_DATA_SERVER_PORT,0, int.class);
	}
	
	public void setDataServerPort(int port) {
		mShared.put(KEY_IT_DATA_SERVER_PORT, port);
	}
	
	public void setSetting(String key,Object value){
		mShared.put(key, value);
	}

	public<T> T getSetting(String key,Class<T> clazz) {
		return mShared.get(key, clazz);
	}
	public<T> T getSetting(String key, Class<T> clazz, T def) {
		return mShared.get(key, def, clazz);
	}
}
