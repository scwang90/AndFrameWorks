package com.andframe.annotation.interpreter;

import android.text.TextUtils;

import com.andframe.annotation.model.IntRange;
import com.andframe.annotation.model.Must;
import com.andframe.api.pager.Pager;
import com.andframe.exception.AfToastException;

import java.lang.reflect.Field;

import static com.andframe.annotation.interpreter.ReflecterCacher.getFieldByHandler;

/**
 * Must 检测器
 * Created by Administrator on 2016/3/2.
 */
public class ModelChecker {

    public static void check(Object obj) throws Exception {
        if (obj != null) {

            Field[] fields = getFieldByHandler(obj);
            for (Field field : fields) {
                if (field.isAnnotationPresent(Must.class)) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value == null || (value instanceof String && TextUtils.isEmpty(value.toString()))) {
                        throw new AfToastException(field.getAnnotation(Must.class).value());
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
            }
        }
    }

    public static boolean check(Pager pager, Object obj) {
        try {
            check(obj);
            return true;
        } catch (Exception e) {
            if (pager != null) {
                pager.makeToastShort("请先完善信息", e);
            }
            return false;
        }
    }
}
