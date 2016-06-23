package com.andrestful.util;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * 表单工具
 * Created by SCWANG on 2016/6/22.
 */
public class FormUtil {

    public static String toForm(Object obj) throws Exception {
        return toForm(obj, new Formater());
    }

    public static String toForm(Object obj,Formater formater) throws Exception {
        if (obj instanceof String || obj instanceof JSONObject) {
            return URLDecoder.decode(obj.toString(), "UTF-8");
        } else if (obj instanceof Map) {
            return toForm((Map) obj,formater);
        } else {
            return toForm(obj, ReflecterUtil.getField(obj.getClass()),formater);
        }
    }

    private static String toForm(Object obj, Field[] fields,Formater formater) throws IllegalAccessException, UnsupportedEncodingException {
        String form = "";
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) &&
                    !Modifier.isTransient(modifiers)) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    form += "&" + field.getName() + "=" + URLEncoder.encode(formater.toString(value), "UTF-8");
                }
            }
        }
        return form;
    }

    private static String toForm(Map obj,Formater formater) throws UnsupportedEncodingException {
        String form = "";
        Set<Map.Entry> set = obj.entrySet();
        for (Map.Entry entry : set) {
            form += "&" + entry.getKey() + "=" + URLEncoder.encode(formater.toString(entry.getValue()), "UTF-8");
        }
        return form;
    }


    public static class Formater {
        public String toString(Object object){
            return String.valueOf(object);
        }
    }
}
