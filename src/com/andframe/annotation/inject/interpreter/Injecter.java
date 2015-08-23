package com.andframe.annotation.inject.interpreter;

import android.content.Context;
import android.content.res.Resources;

import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.annotation.inject.Inject;
import com.andframe.annotation.inject.InjectExtra;
import com.andframe.annotation.inject.InjectInit;
import com.andframe.annotation.inject.InjectQueryChanged;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfIntent;
import com.andframe.feature.framework.AfExtrater;
import com.andframe.fragment.AfFragment;
import com.andframe.layoutbind.framework.AfViewDelegate;
import com.andframe.util.java.AfReflecter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
		doInjectSystem(context);
		doInjectExtra(context);
		doInjectInit(context);
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
			} catch (Exception e) {
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
				}
				field.setAccessible(true);
				field.set(mHandler, value);
			} catch (Exception e) {
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
