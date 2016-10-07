package com.andpack.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 必须登录
 * Created by SCWANG on 2016/10/7.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MustLogined {
    Class<?> value();//需要启动登录页面的 Activity 或者 Fragment 类
}
