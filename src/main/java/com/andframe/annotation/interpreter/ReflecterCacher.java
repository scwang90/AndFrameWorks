package com.andframe.annotation.interpreter;

import android.support.annotation.NonNull;

import com.andframe.activity.AfActivity;
import com.andframe.fragment.AfFragment;
import com.andframe.impl.wrapper.ViewWrapper;
import com.andframe.util.java.AfReflecter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射缓存
 * Created by SCWANG on 2017/5/3.
 */
@SuppressWarnings("WeakerAccess")
public class ReflecterCacher {

    private static Map<Class, Field[]> fieldCache = new HashMap<>();
    private static Map<Class, Method[]> methodCache = new HashMap<>();

    @NonNull
    public static Field[] getFieldByHandler(Object handler) {
        return getFieldByClass(handler.getClass());

    }

    @NonNull
    public static Field[] getFieldByClass(Class clazz) {
        Field[] fields = fieldCache.get(clazz);
        if (fields == null) {
            fields = AfReflecter.getField(clazz, getStopType(clazz));
            fieldCache.put(clazz, fields);
        }
        return fields;
    }

    @NonNull
    public static Method[] getMethodByHandler(Object handler) {
        return getMethodByClass(handler.getClass());
    }

    @NonNull
    public static Method[] getMethodByClass(Class clazz) {
        Method[] methods = methodCache.get(clazz);
        if (methods == null) {
            methods = AfReflecter.getMethod(clazz, getStopType(clazz));
            methodCache.put(clazz, methods);
        }
        return methods;
    }

    public static Class<?> getStopType(Class clazz) {
        if (ViewWrapper.class.isAssignableFrom(clazz)) {
            return ViewWrapper.class;
        } else if (AfActivity.class.isAssignableFrom(clazz)) {
            return AfActivity.class;
        } else if (AfFragment.class.isAssignableFrom(clazz)) {
            return AfFragment.class;
        }
        return Object.class;
    }
}
