package com.andcloud.activity;

import android.content.Context;

import com.andframe.activity.framework.AfActivity;
import com.andframe.application.AfExceptionHandler;
import com.avos.avoscloud.AVAnalytics;
import com.umeng.analytics.MobclickAgent;

public class AbActivity extends AfActivity {

	protected void onPause() {
		super.onPause();
		AbActivity.pause(this);
	}

	protected void onResume() {
		super.onResume();
		AbActivity.resume(this);
	}

	public static void pause(Context context) {
		try {
			AVAnalytics.onPause(context);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AbActivity.pause.AVAnalytics");
		}
		try {
			MobclickAgent.onPageEnd( context.getClass().getSimpleName());
			MobclickAgent.onPause(context);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AbActivity.pause.MobclickAgent");
		}
	}
	public static void resume(Context context) {
		try {
			AVAnalytics.onResume(context);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AbActivity.resume.AVAnalytics");
		}
		try {
			MobclickAgent.onPageStart(context.getClass().getSimpleName());
			MobclickAgent.onResume(context);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AbActivity.resume.MobclickAgent");
		}
	}

	public static void event(Context context,String eventId) {
		try {
			eventId = eventId.replace('.', '_');
			AVAnalytics.onEvent(context, eventId);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AbActivity.event.AVAnalytics");
		}
		try {
			MobclickAgent.onEvent(context, eventId);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AbActivity.event.MobclickAgent");
		}
	}

	public static void event(Context context, String eventId, String tag) {
		try {
			eventId = eventId.replace('.', '_');
			AVAnalytics.onEvent(context, eventId,tag);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AbActivity.event.AVAnalytics.tag");
		}
		try {
			MobclickAgent.onEvent(context, eventId,tag);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AbActivity.event.MobclickAgent.tag");
		}
	}
	
}
