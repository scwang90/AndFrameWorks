package com.andframe.annotation.interpreter;

import android.text.TextUtils;

import com.andframe.annotation.model.IntRange;
import com.andframe.annotation.model.MinFloat;
import com.andframe.annotation.model.MinInt;
import com.andframe.annotation.model.Must;
import com.andframe.annotation.model.Regex;
import com.andframe.api.pager.Pager;
import com.andframe.exception.AfToastException;

import java.lang.reflect.Field;

import static com.andframe.annotation.interpreter.ReflecterCacher.getFields;

/**
 * Must 检测器
 * Created by Administrator on 2016/3/2.
 */
public class ModelChecker {

    public static void check(Object obj) throws Exception {
        if (obj != null) {

            Field[] fields = getFields(obj);
            for (Field field : fields) {
                if (field.isAnnotationPresent(Regex.class)) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null) {
                        Regex annotation = field.getAnnotation(Regex.class);
                        for (String regex : annotation.value()) {
                            if (value.toString().matches(regex)) {
                                value = null;
                                break;
                            }
                        }
                        if (value != null) {
                            throw new AfToastException(annotation.message());
                        }
                    }
                }
                if (field.isAnnotationPresent(IntRange.class)) {
                    IntRange range = field.getAnnotation(IntRange.class);
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        if (intValue < range.from() || intValue > range.to()) {
                            throw new AfToastException(range.value());
                        }
                    }
                }
                if (field.isAnnotationPresent(MinFloat.class)) {
                    MinFloat minFloat = field.getAnnotation(MinFloat.class);
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value instanceof Float) {
                        float floatValue = (Float) value;
                        if (floatValue < minFloat.min()) {
                            throw new AfToastException(minFloat.value());
                        }
                    }
                } else if (field.isAnnotationPresent(MinInt.class)) {
                    MinInt minInt = field.getAnnotation(MinInt.class);
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value instanceof Integer) {
                        float intValue = (Integer) value;
                        if (intValue < minInt.min()) {
                            throw new AfToastException(minInt.value());
                        }
                    }
                }
            }
            for (Field field : fields) {
                if (field.isAnnotationPresent(Must.class)) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value == null || (value instanceof String && TextUtils.isEmpty(value.toString()))) {
                        throw new AfToastException(field.getAnnotation(Must.class).value());
                    }
                }
            }
        }
    }

    public static boolean check(Pager pager, Object obj) {
        try {
            check(obj);
            return true;
        } catch (Throwable e) {
            if (pager != null) {
                pager.makeToastShort("请先完善信息", e);
            }
            return false;
        }
    }
}
