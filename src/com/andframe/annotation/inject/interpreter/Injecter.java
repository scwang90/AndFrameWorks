package com.andframe.annotation.inject.interpreter;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;

import com.andframe.annotation.inject.Inject;
import com.andframe.application.AfExceptionHandler;
import com.andframe.util.java.AfReflecter;
/**
 * annotation.inject 解释器
 * @author scwang
 */
public class Injecter {
	
	private Object mHandler;

	public Injecter(Object handler) {
		// TODO Auto-generated constructor stub
		mHandler = handler;
	}
	
	protected String TAG(String tag) {
		// TODO Auto-generated method stub
		return "Injecter("+mHandler.getClass().getName()+")."+tag;
	}
	
	public void doInject() {
		if (mHandler instanceof Context) {
			this.doInject((Context)(mHandler));
		}
	}

	public void doInject(Context context) {
		// TODO Auto-generated method stub
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
               AfExceptionHandler.handler(e,TAG("doInject.")+ field.getName());
            }
		}
	}

}
