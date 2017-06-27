package com.andframe.annotation.interpreter;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 智能方法调用
 * Created by SCWANG on 2016/10/14.
 */

public class SmartInvoke {

    public static Object invokeMethod(Object handler, Method method, Object... params) throws Exception {
        if (handler != null && method != null) {
            method.setAccessible(true);
            return method.invoke(handler, paramAllot(method, params));
        }
        return null;
    }
    /**
     * 智能参数分配
     */
    public static Object[] paramAllot(Method method, Object... params) {
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
        return paramAllot(method, list.toArray(new Object[list.size()]), params);
    }
    /**
     * 特定参数分配
     */
    public static Object[] paramAllot(Method method, Object[] args, Object... params) {
        if (params.length == 0) {
            return args;
        }
        Class<?>[] types = null;
        //View 智能获取tag中的值
        if (params[0] instanceof View) {
            Object tag = null;
            for (int i = 0; i < args.length && i < params.length; i++) {
                if (args[i] == null) {
                    if (tag == null) {
                        tag = ((View) params[0]).getTag();
                        if (tag == null) {
                            break;
                        }
                        types = method.getParameterTypes();
                    }
                    if (types[i].isInstance(tag)) {
                        args[i] = tag;
                    }
                }
            }
        }
        //ListView 智能获取列表 中的元素
        if (params[0] instanceof AdapterView && params[2] instanceof Integer) {
            Adapter adapter = ((AdapterView) params[0]).getAdapter();
            if (adapter != null && adapter.getCount() > 0) {
                int index = ((Integer) params[2]);
                if (params[0] instanceof ListView) {
                    int count = ((ListView) params[0]).getHeaderViewsCount();
                    index = index >= count ? (index - count) : index;
                }
                Object value = null;
                for (int i = 0; i < args.length && i < params.length; i++) {
                    if (args[i] == null) {
                        if (value == null) {
                            value = adapter.getItem(index);
                            if (value == null) {
                                break;
                            }
                            types = method.getParameterTypes();
                        }
                        if (types[i].isInstance(value)) {
                            args[i] = value;
                        }
                    }
                }
            }
        }
        return args;
    }

    public static boolean isInstance(Class<?> t1, Object object) {
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
