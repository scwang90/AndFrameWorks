package com.andframe.annotation.inject.interpreter;

import android.content.Context;
import android.content.res.Resources;

import com.andframe.R;
import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.annotation.inject.Inject;
import com.andframe.annotation.inject.InjectExtra;
import com.andframe.annotation.inject.InjectInit;
import com.andframe.annotation.inject.InjectLayout;
import com.andframe.annotation.inject.InjectQueryChanged;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfDurableCache;
import com.andframe.caches.AfImageCaches;
import com.andframe.caches.AfJsonCache;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.caches.AfSharedPreference;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfDailog;
import com.andframe.feature.AfDensity;
import com.andframe.feature.AfDistance;
import com.andframe.feature.AfGifPlayer;
import com.andframe.feature.AfIntent;
import com.andframe.feature.AfSoftInputer;
import com.andframe.feature.framework.AfExtrater;
import com.andframe.fragment.AfFragment;
import com.andframe.helper.android.AfDesHelper;
import com.andframe.helper.android.AfDeviceInfo;
import com.andframe.helper.android.AfGifHelper;
import com.andframe.helper.android.AfImageHelper;
import com.andframe.helper.java.AfSQLHelper;
import com.andframe.layoutbind.framework.AfViewDelegate;
import com.andframe.network.AfImageService;
import com.andframe.util.android.AfMeasure;
import com.andframe.util.java.AfReflecter;
import com.andframe.util.java.AfStackTrace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * annotation.inject 解释器
 * @author 树朾
 */
public class Injecter {

	private Object mHandler;

	public Injecter(Object handler) {
		mHandler = handler;
	}

	protected String TAG(String tag) {
		return "Injecter("+mHandler.getClass().getName()+")."+tag;
	}

	public void doInject() {
		if (mHandler instanceof Context) {
			this.doInject((Context)(mHandler));
		}
	}

	public void doInject(Context context) {
		doInjectExtra(context);
		doInjectLayout(context);
		doInjectSystem(context);
		doInjectInit(context);
	}

	private void doInjectLayout(Context context) {
		InjectLayout layout = AfReflecter.getAnnotation(mHandler.getClass(), getStopType(), InjectLayout.class);
		if (mHandler instanceof AfActivity && layout != null) {
			try {
				((AfActivity) mHandler).setContentView(layout.value());
			}catch(Throwable e){
				e.printStackTrace();
				AfExceptionHandler.handler(e,TAG("doInjectLayout.setContentView"));
			}
		}
	}

	private void doInjectInit(Context context) {
		for (Method method : AfReflecter.getMethodAnnotation(mHandler.getClass(), getStopType(), InjectInit.class)) {
			InjectInit init = method.getAnnotation(InjectInit.class);
			try {
				invokeMethod(mHandler,method);
			}catch(Throwable e){
				e.printStackTrace();
				if (init.value()){
					throw new RuntimeException("调用初始化失败",e);
				}
				AfExceptionHandler.handler(e,TAG("doInjectInit.invokeMethod.")+method.getName());
			}
		}
	}


	public void doInjectQueryChanged() {
		for (Method method : AfReflecter.getMethodAnnotation(mHandler.getClass(), getStopType(), InjectQueryChanged.class)) {
			InjectQueryChanged init = method.getAnnotation(InjectQueryChanged.class);
			try {
				invokeMethod(mHandler,method);
			}catch(Throwable e){
				e.printStackTrace();
				if (init.value()){
					throw new RuntimeException("调用查询失败",e);
				}
				AfExceptionHandler.handler(e, TAG("doInjectQueryChanged.invokeMethod.")+method.getName());
			}
		}
	}

	private void doInjectExtra(Context context) {
		for (Field field : AfReflecter.getFieldAnnotation(mHandler.getClass(),InjectExtra.class)) {
			InjectExtra inject = field.getAnnotation(InjectExtra.class);
			try {
				if (mHandler instanceof AfPageable){
					AfExtrater intent = new AfIntent();
					if (mHandler instanceof AfActivity){
						intent = new AfIntent(((AfActivity) mHandler).getIntent());
					} else if (mHandler instanceof AfFragment){
						intent = new AfBundle(((AfFragment) mHandler).getArguments());
					}
					Class<?> clazz = field.getType();
					Object value = intent.get(inject.value(), clazz);
					field.setAccessible(true);
					field.set(mHandler, value);
				}
			} catch (Throwable e) {
				if (inject.necessary()){
					throw new RuntimeException("缺少必须参数",e);
				}
				AfExceptionHandler.handler(e,TAG("doInject.InjectExtra.")+ field.getName());
			}
		}
	}

	private void doInjectSystem(Context context) {
		for (Field field : AfReflecter.getFieldAnnotation(mHandler.getClass(),Inject.class)) {
			try {
				Object value = null;
				Class<?> clazz = field.getType();
				if (clazz.equals(Resources.class)) {
					value = context.getResources();
				} else if (clazz.equals(Random.class)) {
					value = new Random();
				} else if (clazz.equals(AfSoftInputer.class)) {
					value = new AfSoftInputer(context);
				} else if (clazz.equals(AfDailog.class)) {
					value = new AfDailog(context);
				} else if (clazz.equals(AfDensity.class)) {
					value = new AfDensity(context);
				} else if (clazz.equals(AfReflecter.class)) {
					value = new AfReflecter();
				} else if (clazz.equals(AfDesHelper.class)) {
					value = new AfDesHelper();
				} else if (clazz.equals(AfDeviceInfo.class)) {
					value = new AfDeviceInfo(context);
				} else if (clazz.equals(AfDistance.class)) {
					value = new AfDistance();
				} else if (clazz.equals(AfGifHelper.class)) {
					value = new AfGifHelper();
				} else if (clazz.equals(AfImageHelper.class)) {
					value = new AfImageHelper();
				} else if (clazz.equals(AfSharedPreference.class)) {
					value = new AfSharedPreference(context,field.getAnnotation(Inject.class).value());
				} else if (clazz.equals(AfDurableCache.class)) {
					value = AfDurableCache.getInstance(field.getAnnotation(Inject.class).value());
				} else if (clazz.equals(AfPrivateCaches.class)) {
					value = AfPrivateCaches.getInstance(field.getAnnotation(Inject.class).value());
				} else if (clazz.equals(AfJsonCache.class)) {
					value = new AfJsonCache(context,field.getAnnotation(Inject.class).value());
				} else if (clazz.equals(AfImageCaches.class)) {
					value = AfImageCaches.getInstance();
				} else if (clazz.equals(AfApplication.class)) {
					value = AfApplication.getApp();
				}
				field.setAccessible(true);
				field.set(mHandler, value);
			} catch (Throwable e) {
				AfExceptionHandler.handler(e,TAG("doInject.Inject")+ field.getName());
			}
		}
	}

	private Object invokeMethod(Object handler, Method method, Object... params) throws Exception {
		if (handler != null && method != null){
			method.setAccessible(true);
			return method.invoke(handler, params);
		}
		return null;
	}

	private Class<?> getStopType(){
		if (mHandler instanceof AfViewDelegate){
			return AfViewDelegate.class;
		}
		return Object.class;
	}
}
