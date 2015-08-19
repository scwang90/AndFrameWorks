package com.andframe.annotation.inject.interpreter;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.annotation.inject.Inject;
import com.andframe.annotation.inject.InjectExtra;
import com.andframe.annotation.view.BindView;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfIntent;
import com.andframe.feature.framework.AfExtrater;
import com.andframe.fragment.AfFragment;
import com.andframe.util.java.AfReflecter;
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
		for (Field field : AfReflecter.getFieldAnnotation(mHandler.getClass(),InjectExtra.class)) {
			try {
				if (mHandler instanceof AfPageable){
					AfExtrater intent = new AfIntent();
					if (mHandler instanceof AfActivity){
						intent = new AfIntent(((AfActivity) mHandler).getIntent());
					} else if (mHandler instanceof AfFragment){
						intent = new AfBundle(((AfFragment) mHandler).getArguments());
					}
					InjectExtra inject = field.getAnnotation(InjectExtra.class);
					Class<?> clazz = field.getType();
					Object value = intent.get(inject.value(), clazz);
					field.setAccessible(true);
					field.set(mHandler, value);
				}
			} catch (Exception e) {
				AfExceptionHandler.handler(e,TAG("doInject.InjectExtra.")+ field.getName());
			}
		}
	}

}
