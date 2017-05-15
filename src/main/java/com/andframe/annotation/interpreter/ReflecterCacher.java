package com.andframe.annotation.interpreter;

import android.support.annotation.NonNull;

import com.andframe.activity.AfActivity;
import com.andframe.fragment.AfFragment;
import com.andframe.impl.wrapper.ViewWrapper;
import com.andframe.util.java.AfReflecter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static Field[] getFields(Object handler) {
        return getFields(handler.getClass());
    }

    @NonNull
    public static Field[] getFields(Class clazz) {
        Field[] fields = fieldCache.get(clazz);
        if (fields == null) {
            fields = AfReflecter.getField(clazz, getStopType(clazz));
            fieldCache.put(clazz, fields);
        }
        return fields;
    }

    @NonNull
    public static Method[] getMethods(Object handler) {
        return getMethods(handler.getClass());
    }

    @NonNull
    public static Method[] getMethods(Class clazz) {
        Method[] methods = methodCache.get(clazz);
        if (methods == null) {
            methods = AfReflecter.getMethod(clazz, getStopType(clazz));
            methodCache.put(clazz, methods);
        }
        return methods;
    }

    public static Method[] getMethodAnnotation(Object handler, Class<? extends Annotation> annotation) {
        return getMethodAnnotation(handler.getClass(), annotation);
    }

    public static Method[] getMethodAnnotation(Class clazz, Class<? extends Annotation> annotation) {
        List<Method> methodList = new ArrayList<>();
        Method[] methods = getMethods(clazz);
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                methodList.add(method);
            }
        }
        return methodList.toArray(new Method[methodList.size()]);
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
