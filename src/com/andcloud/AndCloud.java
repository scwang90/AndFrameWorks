package com.andcloud;

import android.annotation.SuppressLint;
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
	public static boolean mDebug = false;
	@SuppressLint("StaticFieldLeak")
	private static Context mContext;

	public static void postEvent(Object event) {
		EventBus.getDefault().post(event);
	}

	public static Context getContext() {
		return mContext;
	}

	@Override
	public void onLoadDeployFailed() {
		postEvent(new CloudEvent(CloudEvent.CLOUD_DEPLOY_FAILED));
	}

	@Override
	public void onLoadDeployFinish(Deploy deploy) {
		postEvent(new CloudDeployEvent(deploy));
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
		mContext = context.getApplicationContext();
		mDebug = isDebug;
		mChannel = channel;
		mDefchannel = defchannel;
//		DeployCheckTask.deploy(context, listener,defchannel,channel);
	}

	public static void deploy(Context context, LoadDeployListener listener) {
		if (mChannel != null && mDefchannel != null) {
			DeployCheckTask.deploy(context, listener, mDefchannel, mChannel);
		}
	}

	public static void initializeAvos(Context context, String appid, String appkey, String channel) {
		try {
			AndCloud.registerSubclass(Deploy.class);
			// 初始化应用 Id 和 应用 Key，您可以在应用设置菜单里找到这些信息
			AVOSCloud.initialize(context, appid, appkey);
//		    AVAnalytics.start(this);
			AVAnalytics.setAppChannel(channel);
		    AVAnalytics.enableCrashReport(context, true);
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

	public static void pause(Context context) {
		try {
			AVAnalytics.onPause(context);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.pause.AVAnalytics"));
		}
		try {
			MobclickAgent.onPageEnd( context.getClass().getSimpleName());
			MobclickAgent.onPause(context);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.pause.MobclickAgent"));
		}
	}

	public static void resume(Context context) {
		try {
			AVAnalytics.onResume(context);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.resume.AVAnalytics"));
		}
		try {
			MobclickAgent.onPageStart(context.getClass().getSimpleName());
			MobclickAgent.onResume(context);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.resume.MobclickAgent"));
		}
	}

	public static void pause(Context context,String pagename) {
		try {
			AVAnalytics.onPause(context, pagename);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.pause.AVAnalytics"));
		}
		try {
			MobclickAgent.onPageEnd(pagename);
			MobclickAgent.onPause(context);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.pause.MobclickAgent"));
		}
	}

	public static void resume(Context context,String pagename) {
		try {
			AVAnalytics.onResume(context, pagename);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.resume.AVAnalytics"));
		}
		try {
			MobclickAgent.onPageStart(pagename);
			MobclickAgent.onResume(context);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.resume.MobclickAgent"));
		}
	}
	public static void event(Context context,String eventId) {
		try {
			eventId = eventId.replace('.', '_');
			AVAnalytics.onEvent(context, eventId);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.event.AVAnalytics"));
		}
		try {
			MobclickAgent.onEvent(context, eventId);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.event.MobclickAgent"));
		}
	}

	public static void event(Context context, String eventId, String tag) {
		try {
			eventId = eventId.replace('.', '_');
			AVAnalytics.onEvent(context, eventId,tag);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.event.AVAnalytics.tag"));
		}
		try {
			MobclickAgent.onEvent(context, eventId,tag);
		} catch (Throwable e) {
			postEvent(new CloudExceptionEvent(e, "AbActivity.event.MobclickAgent.tag"));
		}
	}

}
