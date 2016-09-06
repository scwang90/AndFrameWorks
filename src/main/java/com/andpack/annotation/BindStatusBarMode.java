package com.andpack.annotation;

import com.andpack.constant.StatusBarMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定状态栏模式
 * Created by SCWANG on 2016/9/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindStatusBarMode {
    StatusBarMode value() default StatusBarMode.normal;
}
