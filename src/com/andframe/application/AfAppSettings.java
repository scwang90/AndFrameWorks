package com.andframe.application;

import android.content.Context;

import com.andframe.caches.AfJsonCache;

public class AfAppSettings {

	protected static final String  EM_SHARENAME = "AppSettings";

	//提示声音
	public static final String KEY_BL_ISNOTIFYSOUND = "KEY_BL_NOTIFYSOUND";	
	//2G/3G网络无图模式
	public static final String KEY_BL_ISNOIMAGEMODE = "KEY_BL_NOIMAGEMODE";		
	//是否开启自动更新
	public static final String KEY_BL_ISAUTOUPDATE = "KEY_BL_ISAUTOUPDATE";	
	//是否开启自动登录
	public static final String KEY_BL_ISAUTOLOGIN = "KEY_BL_ISAUTOLOGIN";	
	//登录账户
	public static final String KEY_ST_LOGINACCOUNT = "KEY_ST_LOGINACCOUNT";	
	//登录密码
	public static final String KEY_ST_LOGINPASSWORD = "KEY_ST_LOGINPASSWORD";	
	//使用GPS定位
	public static final String KEY_BL_ISUSEPGSFIXED = "KEY_BL_USEPGSFIXED";
	//定位时间间隔
	public static final String KEY_LG_FIXEDDURATION = "KEY_IT_FIXEDDURATION";	
	//数据服务器域名或IP
	public static final String KEY_ST_DATASERVERIP = "KEY_ST_DATESERVERIP";	
	//数据服务器端口
	public static final String KEY_IT_DATASERVERPORT = "KEY_IT_DATESERVERPORT";	
	//文件服务器域名或IP
	public static final String KEY_ST_FILESERVERIP = "KEY_ST_FILESERVERIP";	
	//文件服务器端口
	public static final String KEY_IT_FILESERVERPORT = "KEY_IT_FILESERVERPORT";	
	//文件服务器端口
	public static final String KEY_IT_APPDEBUGMODE = "KEY_IT_APPDEBUGMODE";	

	protected AfJsonCache mShared = null;
	
	protected static AfAppSettings mInstance;

    protected AfAppSettings(Context context) {
		// TODO Auto-generated constructor stub
    	int mode = Context.MODE_PRIVATE;
    	mShared = new AfJsonCache(context,EM_SHARENAME,mode);
    	setDefault(KEY_ST_LOGINACCOUNT, "");
    	setDefault(KEY_ST_LOGINPASSWORD, "");
    	setDefault(KEY_BL_ISNOIMAGEMODE, false);
    	setDefault(KEY_BL_ISNOTIFYSOUND, true);
    	setDefault(KEY_BL_ISUSEPGSFIXED, true);
    	setDefault(KEY_BL_ISAUTOUPDATE, true);
    	setDefault(KEY_BL_ISAUTOLOGIN, false);
    	setDefault(KEY_ST_FILESERVERIP, getDefaultFileServerIP());
    	setDefault(KEY_IT_FILESERVERPORT, getDefaultFileServerPort());
    	setDefault(KEY_ST_DATASERVERIP, getDefaultDataServerIP());
    	setDefault(KEY_IT_DATASERVERPORT, getDefaultDataServerPort());
    	setDefault(KEY_LG_FIXEDDURATION, 10*60*1000);
    	setDefault(KEY_IT_APPDEBUGMODE, AfApplication.DEBUG_NONE);
	}

    protected int getDefaultFileServerPort() {
		// TODO Auto-generated method stub
		return 8089;
	}

    protected String getDefaultFileServerIP() {
		// TODO Auto-generated method stub
		return "127.0.0.1";
	}

	protected int getDefaultDataServerPort() {
		// TODO Auto-generated method stub
		return 8088;
	}

	protected String getDefaultDataServerIP() {
		// TODO Auto-generated method stub
		return "127.0.0.1";
	}

	protected void setDefault(String key, Object defaul) {
		// TODO Auto-generated method stub
		if(mShared.isEmpty(key)){
			mShared.put(key, defaul);
		}
	}

	public static void initialize(Context context){
		if(mInstance == null){
			mInstance = AfApplication.getApp().getAppSetting();
		}
	}

	public static AfAppSettings getInstance(){
		return mInstance;
	}

	public long getFixedDuration(){
		return mShared.get(KEY_LG_FIXEDDURATION,0l, long.class);
	}
	
	public void setFixedDuration(long value) {
		mShared.put(KEY_LG_FIXEDDURATION, value);
	}
	
	public boolean isNoImage() {
		return mShared.get(KEY_BL_ISNOIMAGEMODE,false, boolean.class);
	}

	public void setNoImage(boolean value) {
		mShared.put(KEY_BL_ISNOIMAGEMODE, value);
	}

	public boolean isNotifySound() {
		return mShared.get(KEY_BL_ISNOTIFYSOUND,false, boolean.class);
	}

	public void setNotifySound(boolean value) {
		mShared.put(KEY_BL_ISNOTIFYSOUND, value);
	}

	public boolean isUseGpsFixed() {
		return mShared.get(KEY_BL_ISUSEPGSFIXED,false, boolean.class);
	}
	
	public void setUserGpsFixed(boolean value) {
		mShared.put(KEY_BL_ISUSEPGSFIXED, value);
	}

	public boolean isAutoUpdate() {
		return mShared.get(KEY_BL_ISAUTOUPDATE,false, boolean.class);
	}
	
	public void setAutoUpdate(boolean value) {
		mShared.put(KEY_BL_ISAUTOUPDATE, value);
	}

	public boolean isAutoLogin() {
		return mShared.get(KEY_BL_ISAUTOLOGIN,false, boolean.class);
	}
	
	public void setAutoLogin(boolean value) {
		mShared.put(KEY_BL_ISAUTOLOGIN, value);
	}

	public String getLoginAccount() {
		return mShared.get(KEY_ST_LOGINACCOUNT,"", String.class);
	}
	
	public void setLoginAccount(String value) {
		mShared.put(KEY_ST_LOGINACCOUNT, value);
	}

	public String getLoginPassword() {
		return mShared.get(KEY_ST_LOGINPASSWORD,"", String.class);
	}
	
	public void setLoginPassword(String value) {
		mShared.put(KEY_ST_LOGINPASSWORD, value);
	}

	public String getDataServerIP() {
		return mShared.get(KEY_ST_DATASERVERIP, String.class);
	}

	public void setDataServerIP(String ip) {
		mShared.put(KEY_ST_DATASERVERIP, ip);
	}
	
	public String getFileServerIP() {
		return mShared.get(KEY_ST_FILESERVERIP, String.class);
	}

	public void setFileServerIP(String ip) {
		mShared.put(KEY_ST_FILESERVERIP, ip);
	}

	public int getDataServerPort() {
		return mShared.get(KEY_IT_DATASERVERPORT,0, int.class);
	}
	
	public void setDataServerPort(int port) {
		mShared.put(KEY_IT_DATASERVERPORT, port);
	}
	
	public int getFileServerPort() {
		return mShared.get(KEY_IT_FILESERVERPORT,0, int.class);
	}
	
	public void setFileServerPort(int port) {
		mShared.put(KEY_IT_FILESERVERPORT, port);
	}

	public void setDebugMode(int mode) {
		// TODO Auto-generated method stub
		mShared.put(KEY_IT_APPDEBUGMODE, mode);
	}

	public int getDebugMode() {
		// TODO Auto-generated method stub
		return mShared.get(KEY_IT_APPDEBUGMODE,0, int.class);
	}
	

	public void setSetting(String key,Object value){
		mShared.put(key, value);
	}
	
	public void setSetting(String key,Object value,Class<?> clazz){
		mShared.put(key, value, clazz);
	}

    public<T> T getSetting(String key,Class<T> clazz) {
    	return mShared.get(key, clazz);
    }
}
