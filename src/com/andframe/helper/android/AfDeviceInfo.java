package com.andframe.helper.android;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class AfDeviceInfo {

	private DisplayMetrics mDisplay = null;
	private TelephonyManager mPhoneManager = null;

	public AfDeviceInfo(Context context) {
		// TODO Auto-generated constructor stub
		String server = Context.TELEPHONY_SERVICE;
		mDisplay = context.getResources().getDisplayMetrics();
		mPhoneManager = (TelephonyManager)context.getSystemService(server);
	}
	
	public TelephonyManager getManager() {
		return mPhoneManager;
	}
	
	public String getDeviceId()
	{
		try {
			return mPhoneManager.getDeviceId();
		} catch (Throwable e) {
			// TODO: handle exception
			return ("获取失败，请添加 android.permission.READ_PHONE_STATE 权限！");
		}
	}

	public String getDeviceMessage() {
		// TODO Auto-generated method stub
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
			// TODO: handle exception
			sb.append("获取失败，请添加 android.permission.READ_PHONE_STATE 权限！");
		}
		return sb.toString();
	}
	
	private static AfDeviceInfo mInstance = null;
	
	public static AfDeviceInfo initialize(Context context){
		// TODO Auto-generated method stub
		if(mInstance == null){
			mInstance = new AfDeviceInfo(context);
		}
		return mInstance;
	}
	
	public static String detDeviceMessage() {
		// TODO Auto-generated method stub
		if(mInstance != null){
			return mInstance.getDeviceMessage();
		}
		return "";
	}

}
