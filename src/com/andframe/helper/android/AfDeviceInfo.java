package com.andframe.helper.android;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class AfDeviceInfo {

	private DisplayMetrics mDisplay = null;
	private WifiManager mWifiManager = null;
	private TelephonyManager mPhoneManager = null;

	public AfDeviceInfo(Context context) {
		String tmserver = Context.TELEPHONY_SERVICE;
		String wiserver = Context.WIFI_SERVICE;
		mDisplay = context.getResources().getDisplayMetrics();
		mWifiManager = (WifiManager) context.getSystemService(wiserver);
		mPhoneManager = (TelephonyManager)context.getSystemService(tmserver);
	}
	
	public TelephonyManager getManager() {
		return mPhoneManager;
	}
	
	public String getDeviceId()	{
		try {
			return mPhoneManager.getDeviceId();
		} catch (Throwable e) {
			return ("获取失败，请添加 android.permission.READ_PHONE_STATE 权限！");
		}
	}

	public String getMacAddress() {
		try {
			return mWifiManager.getConnectionInfo().getMacAddress();
		} catch (Throwable e) {
			return ("获取失败，请添加 android.permission.ACCESS_WIFI_STATE 权限！");
		}
	}

	public String getDeviceMessage() {
		StringBuffer sb = new StringBuffer();
		sb.append("设备型号： ");
		sb.append(android.os.Build.MODEL);
		sb.append("\r\n系统版本： ");
		sb.append(android.os.Build.VERSION.RELEASE);
		if(mDisplay != null){
			sb.append("\r\n像素尺寸： ");
			sb.append(String.format("%dx%d", mDisplay.widthPixels,mDisplay.heightPixels));
		}
		sb.append("\r\n设备编号： ");
		try {
			sb.append(mPhoneManager.getDeviceId());
			sb.append("\r\n软件版本： ");
			sb.append(mPhoneManager.getDeviceSoftwareVersion());
			sb.append("\r\n服务名称： ");
			sb.append(mPhoneManager.getSimOperatorName());
			sb.append("\r\n");
		} catch (Throwable e) {
			sb.append("获取失败，请添加 android.permission.READ_PHONE_STATE 权限！");
		}
		return sb.toString();
	}
	
	private static AfDeviceInfo mInstance = null;
	
	public static AfDeviceInfo initialize(Context context){
		if(mInstance == null){
			mInstance = new AfDeviceInfo(context);
		}
		return mInstance;
	}
	
	public static String detDeviceMessage() {
		if(mInstance != null){
			return mInstance.getDeviceMessage();
		}
		return "";
	}

}
