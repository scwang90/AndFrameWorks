package com.andframe.util.internal;

/**
 * 日志 TAG 工具类
 * Created by SCWANG on 2016/11/21.
 */

public class TAG {

    public static String TAG(Object object, String tag) {
        return tag+ "(" + object.getClass().getName() + ")";
    }
    public static String TAG(Object object, String tag, String method) {
        return tag+ "(" + object.getClass().getName() + ")." + method;
    }

}
