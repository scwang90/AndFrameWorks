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
            if (null == json || json.trim().length() == 0 || "null".equals(json)) {
                return null;
            }
            if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                return (T) Integer.valueOf(json);
            } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
                return (T) Short.valueOf(json);
            } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
                return (T) Byte.valueOf(json);
            } else if (Character.class.equals(clazz) || char.class.equals(clazz)) {
                return (T) Character.valueOf(json.charAt(0));
            } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                return (T) Long.valueOf(json);
            } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
                return (T) Float.valueOf(json);
            } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
                return (T) Double.valueOf(json);
            } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
                return (T) Boolean.valueOf(json);
            } else if (String.class.equals(clazz)) {
                return (T)json;
            }
            return fromJson(new JSONObject(json), clazz);
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
                if (/*!field.getType().isPrimitive() || */value != null) {
                    field.setAccessible(true);
                    field.set(model, safeValue(value,field.getType()));
                }
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
            }
        }
        return model;
    }

    protected static Object safeValue(Object value, Class<?> clazz) {
        if (value != null) {
            if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                return Double.valueOf(value.toString()).intValue();
            } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
                return Double.valueOf(value.toString()).shortValue();
            } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
                return Byte.valueOf(value.toString());
            } else if (Character.class.equals(clazz) || char.class.equals(clazz)) {
                return (value.toString() + " ").charAt(0);
            } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                return Double.valueOf(value.toString()).longValue();
            } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
                return Double.valueOf(value.toString()).floatValue();
            } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
                return Double.valueOf(value.toString());
            } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
                return Boolean.valueOf(value.toString());
            }
        }
        return value;
    }

    public static <T> List<T> fromJsons(String json,Class<T> clazz){
        try {
            return fromJsons(new JSONArray(json),clazz);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> fromJsons(JSONArray array,Class<T> clazz){
        List<T> list = new ArrayList<>();
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
//                } else if (List.class.equals(type)){
//                    type.toString();
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
            if (clazz.equals(Class.class)) {
                return null;
            }
            return clazz.newInstance();
        } catch (Throwable ignored) {
        }
        try {
            return UnsafeAllocator.create().newInstance(clazz);
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static Object builderJson(Object value) throws JSONException {
        if (value == null){
            return "null";
        } else if (value instanceof Map){
            return builderMap((Map)value);
        } else if (value instanceof Collection){
            return builderArray(((Collection) value).toArray(new Object[((Collection) value).size()]));
        } else if(value.getClass().isArray()){
            return builderArray((Object[])value);
        } else {
            final Class<?> type = value.getClass();
            if (type.isPrimitive() || Integer.class.equals(type) || Short.class.equals(type) ||
                    Character.class.equals(type) || Byte.class.equals(type) || Long.class.equals(type) || Float.class.equals(type) ||
                    Double.class.equals(type) || String.class.equals(type) || Boolean.class.equals(type)){
                return value;
            } else if (Date.class.isAssignableFrom(type)){
                return Date.class.cast(value).getTime();
            }
            return builderObject(value);
        }
    }

    /**
     *
     * @param obj 不可为空
     */
    private static JSONObject builderObject(Object obj) throws JSONException {
        JSONObject object = new JSONObject();
        Field[] fields = getJsonField(obj);
        for (Field field : fields) {
            Object value = getFiledValue(obj,field);
            if (value != null) {
                object.put(field.getName(),builderJson(value));
            }
        }
        return object;
    }

    private static JSONArray builderArray(Object[] objs) throws JSONException {
        JSONArray array = new JSONArray();
        for (Object obj : objs) {
            if (obj == null){
                array.put(null);
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
        Class<?> clazz;
        if (obj instanceof Class){
            clazz = ((Class) obj);
        } else {
            clazz = obj.getClass();
        }
        List<Field> fileds = new ArrayList<>();
        for (Field field : AfReflecter.getField(clazz)) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) &&
                    !Modifier.isTransient(modifiers)){
                fileds.add(field);
            }
        }
        return fileds.toArray(new Field[fileds.size()]);
    }

    /**
     * 使用json克隆model
     */
    public static <T> T clone(Object model, Class<T> clazz) {
        return AfJsoner.fromJson(AfJsoner.toJson(model), clazz);
    }

    /**
     * 使用json克隆list
     */
    public static <T> List<T> cloneList(List<?> list, Class<T> clazz) {
        return AfJsoner.fromJsons(AfJsoner.toJson(list), clazz);
    }
}
