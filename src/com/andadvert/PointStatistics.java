package com.andadvert;

import java.util.Date;
import java.util.Timer;

import android.content.Context;

import com.andadvert.event.AdvertEvent;
import com.andadvert.listener.PointsNotifier;
import com.andframe.activity.framework.AfActivity;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfDurableCache;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.thread.AfTimerTask;
import com.andframe.util.java.AfDateFormat;

public class PointStatistics {

	//标记上次点数值 用于统计点数记录
	private static final String KEY_LASTPONT = "16928984921252804102";
	//标记 点数 增加改变次数
	private static final String KEY_PONTCHANGE = "82962752402252804102";
	//标记 点数 改变记录
	private static final String KEY_PONTNOTE = "09620090231252804102";
	//定时器时间周期（一分钟）
	private static final long TIME_PERIOD = 60*1000;
	//点数获取定时器
	private static Timer mTimer = new Timer();
	//定时器任务
	private static AfTimerTask mTimerTask = null;

	private static class MonitorTask extends AfTimerTask implements PointsNotifier{

		@Override
		protected void onTimer() {
			Context context = AfApplication.getApp();
			AfActivity activity = AfApplication.getApp().getCurActivity();
			if (activity != null && !activity.isRecycled()) {
				context = activity;
			}
			AdvertAdapter.getInstance().getPoints(context,this);
		}

		@Override
		public void getPointsFailed(String error) {

		}

		@Override
		public void getPoints(String currency, int point) {
			doStaticsPoint(point, currency);
		}
	}
	
	//读取老版本记录
	static {
		try {
			AfDurableCache cache = AfDurableCache.getInstance();
			if(cache.getInt(KEY_LASTPONT, 0) > 0){
				AfPrivateCaches pcache = AfPrivateCaches.getInstance();
				pcache.put(KEY_PONTNOTE, cache.getString(KEY_PONTNOTE, ""));
				pcache.put(KEY_LASTPONT, cache.getInt(KEY_LASTPONT, 0));
				pcache.put(KEY_PONTCHANGE, cache.getInt(KEY_PONTCHANGE, 0));
				cache.put(KEY_LASTPONT, 0);
				cache.put(KEY_PONTNOTE, "");
				cache.put(KEY_PONTCHANGE, 0);
			}
		} catch (Throwable e) {
		}
	}
	
	public static void start(){
		if (mTimerTask == null) {
			mTimerTask = new MonitorTask();
			try{
				mTimer.schedule(mTimerTask, TIME_PERIOD, TIME_PERIOD);
			}catch(Throwable e){
				AfExceptionHandler.handle(e, "PointStatistics.start.schedule");
			}
		}
	}

	public static void stop(){
		if (mTimerTask != null) {
			try {
				mTimerTask.cancel();
			} catch (Throwable e) {
				AfExceptionHandler.handleAttach(e, "PointStatistics.TimerTask.cancel");
			}
			mTimerTask = null;
		}
	}
	
	public static void doStaticsPoint(int point, String currency) {
		AfPrivateCaches cache = AfPrivateCaches.getInstance();
		int last = cache.get(KEY_LASTPONT, 0, Integer.class);
		cache.put(KEY_LASTPONT, point);
		if (last != point) {
			String notes = cache.getString(KEY_PONTNOTE, "");
			notes += AfDateFormat.FULL.format(new Date())+" ";
			if (last < point) {
				String local = last+"+"+(point-last)+"="+point+"\r\n";
				notes += local;
				AfApplication app = AfApplication.getApp();
				AdvertAdapter adapter = AdvertAdapter.getInstance();
				app.onUpdateAppinfo();
				if (last > 0 && (point-last) >= 50) {
					if ((point-last) > 1000) {
						AfApplication.getApp().onEvent(AdvertEvent.ADVERT_POINT_INCREASE_CHEAT,currency+"-"+local);
						//NotiftyMail.sendNotifty(currency+"点数作弊", local);
						adapter.spendPoints(AfApplication.getApp(), point-last, new PointsNotifier() {
							public void getPointsFailed(String error) {}
							public void getPoints(String currency, int point) {}
						});
						return;
					}else {

						//new NotiftyMail(adapter.getCurrency()+"点数增加", local).sendTask();
						cache.put(KEY_PONTCHANGE, 1+cache.getInt(KEY_PONTCHANGE, 0));
						AfApplication.getApp().onEvent(AdvertEvent.ADVERT_POINT_INCREASE,local);
//						AfApplication.getApp().onEvent("pointIncrease.poetry",local);
					}
				}
				if (last > 0 && point > last) {
				}
			}else {
				notes += last+"-"+(last-point)+"="+point+"\r\n";
			}
			cache.put(KEY_PONTNOTE, notes);
		}
	}

	public static String getPointStatistics() {
		AfPrivateCaches cache = AfPrivateCaches.getInstance();
		return cache.getString(KEY_PONTNOTE, "");
	}

	public static String getAttractStatistics() {
		AfPrivateCaches cache = AfPrivateCaches.getInstance();
		return cache.getString("66603395431241904102", "");
	}

	public static int getPoint() {
		AfPrivateCaches cache = AfPrivateCaches.getInstance();
		return cache.get(KEY_LASTPONT, 0, Integer.class);
	}

	public static int getPointIncreaseCount() {
		AfPrivateCaches cache = AfPrivateCaches.getInstance();
		return cache.get(KEY_PONTCHANGE, 0, Integer.class);
	}
	
}
