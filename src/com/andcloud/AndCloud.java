package com.andcloud;

import android.content.Context;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

public class AndCloud {

	public static abstract class QQAuthHelper{
		public abstract Tencent getQQAuth();
	}

	private static Tencent mTencent;
	public static void initializeQQAuth(Context appcontext,String appid){
		mTencent = Tencent.createInstance(appid, appcontext);
	}

	public static QQAuthHelper helper = new QQAuthHelper(){
		public Tencent getQQAuth(){
			return mTencent;
		}
	};

	public static void registerSubclass(Class<? extends AVObject> clazz){
		try {
			AVObject.registerSubclass(clazz);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AndCloud.registerSubclass");
		}
	}

	public static void initializeAvos(Context context,String appid,String appkey,String channel) {
		try {
		    // 初始化应用 Id 和 应用 Key，您可以在应用设置菜单里找到这些信息
		    AVOSCloud.initialize(context,appid,appkey);
//		    AVAnalytics.start(this);
		    AVAnalytics.setAppChannel(channel);
//		    AVAnalytics.enableCrashReport(context, true);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AndCloud.initialize");
		}
	}

	public static void initializeUmeng(Context context,String appkey,String channel) {
		try {
			boolean isDebug = AfApplication.getApp().isDebug();
			AnalyticsConfig.setAppkey(appkey);
			AnalyticsConfig.setChannel(channel);
			MobclickAgent.setDebugMode(isDebug);
//			SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//			然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
			MobclickAgent.openActivityDurationTrack(false);
//			MobclickAgent.setAutoLocation(true);
//			MobclickAgent.setSessionContinueMillis(1000);
			MobclickAgent.updateOnlineConfig(context);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "MobclickAgent.updateOnlineConfig");
		}
	}
}
