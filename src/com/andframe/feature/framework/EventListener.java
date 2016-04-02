/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andframe.feature.framework;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;

import com.andframe.application.AfExceptionHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventListener implements OnClickListener, OnLongClickListener, OnItemClickListener, OnItemLongClickListener, CompoundButton.OnCheckedChangeListener {

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

    public OnLongClickListener longClick(Method method) {
        this.longClickMethod = method;
        return this;
    }

    public OnItemClickListener itemClick(Method method) {
        this.itemClickMethod = method;
        return this;
    }

    public OnItemLongClickListener itemLongClick(Method method) {
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
        return (Boolean) invokeMethod(handler, longClickMethod, v);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        invokeMethod(handler, itemClickMethod, parent, view, position, id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return (Boolean) invokeMethod(handler, itemLongClickMehtod, parent, view, position, id);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        invokeMethod(handler, checkedChangedMehtod, buttonView, isChecked);
    }

    private Object invokeMethod(Object handler, Method method, Object... params) {
        if (handler != null && method != null) {
            try {
                method.setAccessible(true);
                return method.invoke(handler, paramAllot(method, params));
            } catch (Throwable e) {
                e.printStackTrace();
                AfExceptionHandler.handler(e, "EventListener.invokeMethod");
            }
        }
        return null;
    }

    private Object[] paramAllot(Method method, Object... params) {
        Set<Integer> set = new HashSet<>();
        List<Object> list = new ArrayList<>();
        Class<?>[] types = method.getParameterTypes();
        if (types.length > 0) {
            for (int i = 0; i < types.length; i++) {
                Object obj = null;
                if (params.length > i && params[i] != null && isInstance(types[i], params[i])) {
                    set.add(i);
                    obj = params[i];
                } else {
                    for (int j = 0; j < params.length; j++) {
                        if (params[j] != null && !set.contains(j) && isInstance(types[i], params[j])) {
                            set.add(j);
                            obj = params[j];
                        }
                    }
                }
                list.add(obj);
            }
        }
        return list.toArray(new Object[list.size()]);
    }

    private boolean isInstance(Class<?> t1, Object object) {
        if (t1.isPrimitive()) {
            if (t1.equals(int.class)) {
                t1 = Integer.class;
            } else if (t1.equals(short.class)) {
                t1 = Short.class;
            } else if (t1.equals(long.class)) {
                t1 = Long.class;
            } else if (t1.equals(float.class)) {
                t1 = Float.class;
            } else if (t1.equals(double.class)) {
                t1 = Double.class;
            } else if (t1.equals(char.class)) {
                t1 = Character.class;
            } else if (t1.equals(byte.class)) {
                t1 = Byte.class;
            } else if (t1.equals(boolean.class)) {
                t1 = Boolean.class;
            }
        }
        return t1.isInstance(object);
    }

}

