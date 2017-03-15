package com.andadvert;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.andadvert.event.AdvertEvent;
import com.andadvert.listener.PointsNotifier;
import com.andadvert.util.ACache;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PointStatistics {

	//标记上次点数值 用于统计点数记录
	private static final String KEY_LASTPONT = "16928984921252804102";
	//标记 点数 增加改变次数
	private static final String KEY_PONTCHANGE = "82962752402252804102";
	//标记 点数 改变记录
	private static final String KEY_PONTNOTE = "09620090231252804102";
	//定时器时间周期（一分钟）
	private static final long TIME_PERIOD = 60 * 1000;
	//定时器任务
	static MonitorTask mTimerTask = null;

	private static class MonitorTask implements Runnable, PointsNotifier {

		private Context context;
		private Handler handler;

		public MonitorTask(Context context, Handler handler) {
			this.context = context.getApplicationContext();
			this.handler = handler;
			handler.post(this);
		}

		@Override
		public void run() {
//			Context context = AfApplication.getApp();
//			AfActivity activity = AfApplication.getApp().getCurActivity();
//			if (activity != null && !activity.isRecycled()) {
//				context = activity;
//			}
			if (handler != null) {
				AdvertAdapter.getInstance().getPoints(context, this);
				handler.postDelayed(this, TIME_PERIOD);
			} else {
				context = null;
			}
		}
		@Override
		public void getPointsFailed(String error) {
		}
		@Override
		public void getPoints(String currency, int point) {
			doStaticsPoint(context, point, currency);
		}

		public void cancel() {
			handler = null;
		}
	}
	
	//读取老版本记录
//	static {
//		try {
//			AfDurableCache cache = AfDurableCache.getInstance();
//			if(cache.getInt(KEY_LASTPONT, 0) > 0){
//				AfPrivateCaches pcache = AfPrivateCaches.getInstance();
//				pcache.put(KEY_PONTNOTE, cache.getString(KEY_PONTNOTE, ""));
//				pcache.put(KEY_LASTPONT, cache.getInt(KEY_LASTPONT, 0));
//				pcache.put(KEY_PONTCHANGE, cache.getInt(KEY_PONTCHANGE, 0));
//				cache.put(KEY_LASTPONT, 0);
//				cache.put(KEY_PONTNOTE, "");
//				cache.put(KEY_PONTCHANGE, 0);
//			}
//		} catch (Throwable e) {
//		}
//	}
	
	public static void start(Context context){
		if (mTimerTask == null) {
			mTimerTask = new MonitorTask(context, new Handler(Looper.getMainLooper()));
		}
	}

	public static void stop(){
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}
	
	public static void doStaticsPoint(Context context, int point, String currency) {
		ACache cache = ACache.get(context);
		DateFormat FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		int last = Integer.valueOf(cache.getAsString(KEY_LASTPONT, "0"));
		cache.put(KEY_LASTPONT, String.valueOf(point));
		if (last != point) {
			String notes = cache.getAsString(KEY_PONTNOTE, "");
			notes += FULL.format(new Date()) + " ";
			if (last < point) {
				String local = last + "+" + (point - last) + "=" + point + "\r\n";
				notes += local;
//				AfApplication app = AfApplication.getApp();
//				app.onUpdateAppinfo();
				AdvertAdapter adapter = AdvertAdapter.getInstance();
				if (last > 0 && (point - last) >= 50) {
					if ((point - last) > 1000) {//点数作弊
						EventBus.getDefault().post(new AdvertEvent(AdvertEvent.ADVERT_POINT_INCREASE_CHEAT, currency + "-" + local));
						adapter.spendPoints(context, point - last, new PointsNotifier() {
							public void getPointsFailed(String error) {
							}
							public void getPoints(String currency, int point) {
							}
						});
						return;
					} else {//点数增加
						cache.put(KEY_PONTCHANGE, String.valueOf(1 + Integer.valueOf(cache.getAsString(KEY_PONTCHANGE, String.valueOf(0)))));
						EventBus.getDefault().post(new AdvertEvent(AdvertEvent.ADVERT_POINT_INCREASE, local));
					}
				}
				if (last > 0 && point > last) {
				}
			}else {
				notes += last + "-" + (last - point) + "=" + point + "\r\n";
			}
			cache.put(KEY_PONTNOTE, notes);
		}
	}

	public static String getPointStatistics(Context context) {
		return ACache.get(context).getAsString(KEY_PONTNOTE, "");
	}

//	public static String getAttractStatistics() {
//		AfPrivateCaches cache = AfPrivateCaches.getInstance();
//		return cache.getString("66603395431241904102", "");
//	}

	public static int getPoint(Context context) {
		return Integer.valueOf(ACache.get(context).getAsString(KEY_LASTPONT, String.valueOf(0)));
	}

	public static int getPointIncreaseCount(Context context) {
		return Integer.valueOf(ACache.get(context).getAsString(KEY_PONTCHANGE, String.valueOf(0)));
	}
	
}
