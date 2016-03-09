package com.andframe.feature;

import com.andframe.util.java.AfReflecter;
import com.google.gson.internal.UnsafeAllocator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 自制Json转换
 * Created by SCWANG on 2015-08-24.
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class AfJsoner {

    public static String toJson(Object object){
        try {
            return builderJson(object).toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json,Class<T> clazz){
        try {
            return fromJson(new JSONObject(json),clazz);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(JSONObject object,Class<T> clazz){
        T model = allocateInstance(clazz);
        Field[] fields = getJsonField(clazz);
        for (Field field : fields) {
            Object value = object.opt(field.getName());
            if (value instanceof JSONObject){
                value = fromJson((JSONObject) value, field.getType());
            } else if (value instanceof JSONArray) {
                Class<?> type = field.getType();
                Type generic = field.getGenericType();
                if (type.isArray()) {
                    type = type.getComponentType();
                    List<?> list = fromJsons((JSONArray) value, type);
                    value = Array.newInstance(type, list.size());
                    value = list.toArray((Object[]) value);
                } else if (List.class.equals(type)) {
                    ParameterizedType parameterized = (ParameterizedType) generic;
                    type = (Class<?>) parameterized.getActualTypeArguments()[0];
                    value = fromJsons((JSONArray) value, type);
                }
            } else if (value instanceof Long && field.getType().equals(Date.class)){
                value = new Date((Long)value);
            }
            try {
                field.setAccessible(true);
                field.set(model,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    public static <T> List<T> fromJsons(String json,Class<T> clazz){
        try {
            return fromJsons(new JSONArray(json),clazz);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> fromJsons(JSONArray array,Class<T> clazz){
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < array.length() ; i++) {
            Object value = array.opt(i);
            if (value instanceof JSONObject){
                list.add(fromJson((JSONObject) value,clazz));
            } else if (value instanceof JSONArray){
                Class<?> type = clazz;
                if (type.isArray()){
                    type = type.getComponentType();
                    List<?> tlist = fromJsons((JSONArray) value,type);
                    value = Array.newInstance(type, tlist.size());
                    value = tlist.toArray((Object[]) value);
                    list.add((T)value);
                } else if (List.class.equals(type)){
                    type.toString();
//                    ParameterizedType generic = (ParameterizedType) field.getGenericType();
//                    type = (Class<?>)generic.getActualTypeArguments()[0];
//                    value = fromJsons((JSONArray) value,type);
                }
            } else {
                list.add((T)value);
            }
        }
        return list;
    }

    private static <T> T allocateInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Throwable e) {
        }
        try {
            return (T)UnsafeAllocator.create().newInstance(clazz);
        } catch (Throwable e) {
        }
        return null;
    }

    private static Object builderJson(Object value) throws JSONException {
        if (value == null){
            return "null";
        } else if (value instanceof Map){
            return builderMap((Map)value);
        } else if (value instanceof Collection){
            return builderArray(((Collection) value).toArray(new Object[0]));
        } else if(value.getClass().isArray()){
            return builderArray((Object[])value);
        } else {
            final Class<?> type = value.getClass();
            if (Integer.class.equals(type) || Short.class.equals(type) ||
                    Long.class.equals(type) || Float.class.equals(type) ||
                    Double.class.equals(type) || String.class.equals(type)){
                return value;
            } else if (Date.class.equals(type)){
                return Date.class.cast(value).getTime();
            }
            return builderObject(value);
        }
    }

    /**
     *
     * @param obj 不可为空
     * @return
     */
    private static JSONObject builderObject(Object obj) throws JSONException {
        JSONObject object = new JSONObject();
        Field[] fields = getJsonField(obj);
        for (Field field : fields) {
            Object value = getFiledValue(obj,field);
            object.put(field.getName(),builderJson(value));
        }
        return object;
    }

    private static JSONArray builderArray(Object[] objs) throws JSONException {
        JSONArray array = new JSONArray();
        for (Object obj : objs) {
            if (obj == null){
                array.put(obj);
            } else {
                array.put(builderJson(obj));
            }
        }
        return array;
    }

    private static JSONObject builderMap(Map value) throws JSONException {
        JSONObject object = new JSONObject();
        Set<Map.Entry> set = value.entrySet();
        for (Map.Entry entry: set) {
            object.put(entry.getKey().toString(),builderJson(entry.getValue()));
        }
        return object;
    }

    private static Object getFiledValue(Object obj, Field field) {
        return AfReflecter.getMemberNoException(obj, field.getName());
    }

    private static Field[] getJsonField(Object obj) {
        Class<?> clazz = null;
        if (obj instanceof Class){
            clazz = ((Class) obj);
        } else {
            clazz = obj.getClass();
        }
        List<Field> fileds = new ArrayList<Field>();
        for (Field field : clazz.getFields()) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) &&
                    !Modifier.isTransient(modifiers)){
                fileds.add(field);
            }
        }
        return fileds.toArray(new Field[0]);
    }

    /**
     * 使用json克隆model
     * @param model
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T clone(Object model, Class<T> clazz) {
        return AfJsoner.fromJson(AfJsoner.toJson(model),clazz);
    }
}
