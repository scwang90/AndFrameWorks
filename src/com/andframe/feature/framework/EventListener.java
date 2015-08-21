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
import android.widget.CompoundButton;

public class EventListener implements OnClickListener, OnLongClickListener, OnItemClickListener,OnItemLongClickListener, CompoundButton.OnCheckedChangeListener {

	private Object handler;
	
	private Method clickMethod;
	private Method longClickMethod;
	private Method itemClickMethod;
	private Method itemLongClickMehtod;
	private Method checkedChangedMehtod;

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

	public OnItemClickListener itemClick(Method method){
		this.itemClickMethod = method;
		return this;
	}

	public OnItemLongClickListener itemLongClick(Method method){
		this.itemLongClickMehtod = method;
		return this;
	}

	public CompoundButton.OnCheckedChangeListener checkedChange(Method method) {
		this.checkedChangedMehtod = method;
		return this;
	}

	public void onClick(View v) {
		invokeMethod(handler, clickMethod, v);
	}

	public boolean onLongClick(View v) {
		return (boolean)invokeMethod(handler, longClickMethod, v);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		invokeMethod(handler, itemClickMethod, parent, view, parent, id);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		return (boolean)invokeMethod(handler, itemLongClickMehtod, parent, view, position, id);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		invokeMethod(handler, checkedChangedMehtod, buttonView,isChecked);
	}

	private Object invokeMethod(Object handler, Method method, Object... params) {
		if (handler != null && method != null){
			try {
				return method.invoke(handler, params);
			}catch(Throwable e){
				e.printStackTrace();
				AfExceptionHandler.handler(e, "EventListener.invokeMethod");
			}
		}
		return null;
	}


}

