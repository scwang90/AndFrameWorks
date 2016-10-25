package com.andpack.annotation.framework;

import com.andframe.annotation.Must;
import com.andframe.exception.AfToastException;
import com.andframe.util.java.AfReflecter;

import java.lang.reflect.Field;

/**
 * Must 检测器
 * Created by Administrator on 2016/3/2.
 */
public class MustChecker {

    public static void checkMust(Object obj) throws Exception {
        for (Field field: AfReflecter.getFieldAnnotation(obj.getClass(), Must.class)) {
            field.setAccessible(true);
            if (field.get(obj) == null) {
                throw new AfToastException(field.getAnnotation(Must.class).value());
            }
        }
    }

}
