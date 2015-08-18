/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andframe.feature.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.andframe.application.AfExceptionHandler;
import com.andframe.exception.AfException;
import com.andframe.util.java.AfReflecter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class EventListener implements OnClickListener, OnLongClickListener, OnItemClickListener, OnItemSelectedListener,OnItemLongClickListener {

	private Object handler;
	
	private Method clickMethod;
	private Method longClickMethod;
	private Method itemClickMethod;
	private Method itemSelectMethod;
	private Method nothingSelectedMethod;
	private Method itemLongClickMehtod;
	
	public EventListener(Object handler) {
		this.handler = handler;
	}

	public OnClickListener click(Method method) {
		clickMethod = method;
		return this;
	}

	public OnLongClickListener longClick(Method method){
		this.longClickMethod = method;
		return this;
	}

	public OnItemLongClickListener itemLongClick(Method method){
		this.itemLongClickMehtod = method;
		return this;
	}

	public OnItemClickListener itemClick(Method method){
		this.itemClickMethod = method;
		return this;
	}

	public EventListener select(Method method){
		this.itemSelectMethod = method;
		return this;
	}

	public EventListener noSelect(Method method){
		this.nothingSelectedMethod = method;
		return this;
	}

	public OnClickListener click(String method){
		this.clickMethod = AfReflecter.getMethod(handler.getClass(), method, new Class[]{View.class});
		return this;
	}
	
	public OnLongClickListener longClick(String method){
		this.longClickMethod = AfReflecter.getMethod(handler.getClass(), method, new Class[]{View.class});
		return this;
	}
	
	public OnItemLongClickListener itemLongClick(String method){
		this.itemLongClickMehtod = AfReflecter.getMethod(handler.getClass(), method, new Class[]{AdapterView.class,View.class,int.class,long.class});
		return this;
	}
	
	public OnItemClickListener itemClick(String method){
		this.itemClickMethod = AfReflecter.getMethod(handler.getClass(), method, new Class[]{AdapterView.class,View.class,int.class,long.class});
		return this;
	}
	
	public EventListener select(String method){
		this.itemSelectMethod = AfReflecter.getMethod(handler.getClass(), method, new Class[]{AdapterView.class,View.class,int.class,long.class});
		return this;
	}
	
	public EventListener noSelect(String method){
		this.nothingSelectedMethod = AfReflecter.getMethod(handler.getClass(), method, new Class[]{AdapterView.class});
		return this;
	}
	
	public boolean onLongClick(View v) {
		return (boolean)invokeMethod(handler, longClickMethod, v);
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		return (boolean)invokeMethod(handler, itemLongClickMehtod, arg0, arg1, arg2, arg3);
	}
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		invokeMethod(handler, itemSelectMethod, arg0, arg1, arg2, arg3);
	}
	
	public void onNothingSelected(AdapterView<?> arg0) {
		invokeMethod(handler, nothingSelectedMethod, arg0);
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		invokeMethod(handler, itemClickMethod, arg0, arg1, arg2, arg3);
	}
	
	public void onClick(View v) {
		invokeMethod(handler, clickMethod, v);
	}

	private Object invokeMethod(Object handler, Method method, Object... params) {
		if (handler != null && method != null){
			try {
				return method.invoke(handler, params);
			}catch(Exception e){
				e.printStackTrace();
				AfExceptionHandler.handler(e, "EventListener.invokeMethod");
			}
		}
		return null;
	}

}

