package com.andcloud.activity;

import android.content.Context;

import com.andframe.activity.AfActivity;
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
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AbActivity.pause.AVAnalytics");
		}
		try {
			MobclickAgent.onPageEnd( context.getClass().getSimpleName());
			MobclickAgent.onPause(context);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AbActivity.pause.MobclickAgent");
		}
	}
	public static void resume(Context context) {
		try {
			AVAnalytics.onResume(context);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AbActivity.resume.AVAnalytics");
		}
		try {
			MobclickAgent.onPageStart(context.getClass().getSimpleName());
			MobclickAgent.onResume(context);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AbActivity.resume.MobclickAgent");
		}
	}

	public static void event(Context context,String eventId) {
		try {
			AVAnalytics.onEvent(context, eventId);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AbActivity.event.AVAnalytics");
		}
		try {
			MobclickAgent.onEvent(context, eventId);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AbActivity.event.MobclickAgent");
		}
	}

	public static void event(Context context, String eventId, String tag) {
		// TODO Auto-generated method stub
		try {
			AVAnalytics.onEvent(context, eventId,tag);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AbActivity.event.AVAnalytics.tag");
		}
		try {
			MobclickAgent.onEvent(context, eventId,tag);
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "AbActivity.event.MobclickAgent.tag");
		}
	}
	
}
