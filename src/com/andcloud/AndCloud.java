package com.andcloud;

import android.content.Context;

import com.andcloud.model.Deploy;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

public class AndCloud implements LoadDeployListener{

	@SuppressWarnings("serial")
	public static Deploy Deploy = new Deploy(){{setRemark("default");}};
	private static String mChannel;
	private static String mDefchannel;
	private static boolean mDebug;

	private static void postEvent(Object event) {
		EventBus.getDefault().post(event);
	}

	@Override
	public void onLoadDeployFailed() {
		postEvent(new CloudEvent(CloudEvent.CLOUD_DEPLOY_FAILED));
	}

	@Override
	public void onLoadDeployFinish(Deploy deploy) {
		postEvent(new CloudEvent(CloudEvent.CLOUD_DEPLOY_FINISHED,""+deploy.isBusinessModel()));
	}

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
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AndCloud.registerSubclass"));
		}
	}

	public static void initializeDeploy(Context context, String defchannel, String channel, boolean isDebug) {
		mDebug = isDebug;
		mChannel = channel;
		mDefchannel = defchannel;
		//DeployCheckTask.deploy(context, listener,defchannel,channel);
	}

	public static void deploy(Context context,LoadDeployListener listener){
		if (mChannel != null && mDefchannel != null) {
			DeployCheckTask.deploy(context, listener,mDefchannel,mChannel);
		}
	}

	public static void initializeAvos(Context context,String appid,String appkey,String channel) {
		try {
			AndCloud.registerSubclass(Deploy.class);
			// 初始化应用 Id 和 应用 Key，您可以在应用设置菜单里找到这些信息
			AVOSCloud.initialize(context,appid,appkey);
//		    AVAnalytics.start(this);
			AVAnalytics.setAppChannel(channel);
//		    AVAnalytics.enableCrashReport(context, true);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AndCloud.initialize"));
		}
	}

	public static void initializeUmeng(Context context,String appkey,String channel) {
		try {
			AnalyticsConfig.setAppkey(appkey);
			AnalyticsConfig.setChannel(channel);
			MobclickAgent.setDebugMode(mDebug);
//			SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//			然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
			MobclickAgent.openActivityDurationTrack(false);
//			MobclickAgent.setAutoLocation(true);
//			MobclickAgent.setSessionContinueMillis(1000);
			MobclickAgent.updateOnlineConfig(context);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AndCloud.updateOnlineConfig"));
		}
	}

}
