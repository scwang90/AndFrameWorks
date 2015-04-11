package com.andframe.application;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;


public class AfNotifyCenter {

	public static final int ID_SCHEDULE = 1000;
	public static final int ID_NEWS = 1001;
	public static final int ID_ANNOUCE = 1002;

	private static NotificationManager mManager = null;

	public static void initailize(Context context) {
		if (mManager == null) {
			String service = Context.NOTIFICATION_SERVICE;
			mManager = (NotificationManager) context.getSystemService(service);
		}
	}
	
	public static Notification getNotification(int sicon,Bitmap licon,String ticker,String title,String content, Intent intent) {
		AfApplication app = AfApplication.getApp();
		
		Builder nBuilder = new Builder(app);
		nBuilder.setTicker(ticker);// 第一次提示消息的时候显示在通知栏上
		nBuilder.setContentTitle(title);
		nBuilder.setContentText(content);
		nBuilder.setSmallIcon(sicon);
		nBuilder.setAutoCancel(true);
		
		if(licon != null){
			nBuilder.setLargeIcon(licon);
		}
		
		if(intent != null){
			int flag = PendingIntent.FLAG_UPDATE_CURRENT;
			PendingIntent pint = PendingIntent.getActivity(app, 0, intent,flag);
			nBuilder.setContentIntent(pint);
		}

		AfAppSettings setting = AfAppSettings.getInstance();
		if (setting.isNotifySound()) {
			nBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS);
		}else{
			nBuilder.setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS);
		}
		
		return nBuilder.build();
	}

	public static Notification getNotification(String content, Intent intent) {
		AfApplication app = AfApplication.getApp();
		int sicon = app.getLogoId(); // 显示图标
		String title = app.getAppName();
		return getNotification(sicon, null,content, title, content, intent);
	}

	public static Notification getNotification(String title,String content, Intent intent) {
		AfApplication app = AfApplication.getApp();
		int sicon = app.getLogoId(); // 显示图标
		return getNotification(sicon, null, title+":"+content, title, content, intent);
	}

	public static void notify(Notification notify, int id) {
		notify.when = System.currentTimeMillis(); // 显示时间
		mManager.notify(id, notify);
	}

	public static void doNotify() {
		AfAppSettings setting = AfAppSettings.getInstance();
		if (!setting.isNotifySound()) {
			AfVibratorConsole.vibrator();
		} else {
			AfApplication app = AfApplication.getApp();
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			RingtoneManager.getRingtone(app, notification).play();
		}
	}
	
	public static NotificationManager getManager() {
		return mManager;
	}

	public static void cancel(int id) {
		// TODO Auto-generated method stub
		mManager.cancel(id);
	}

	public static void clear() {
		// TODO Auto-generated method stub
		mManager.cancelAll();
	}

}
