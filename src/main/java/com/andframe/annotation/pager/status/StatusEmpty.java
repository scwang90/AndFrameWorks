package com.andframe.annotation.pager.status;


import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定多状态页面的各个布局ID
 * Created by SCWANG on 2016/10/20.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusEmpty {
    @LayoutRes int value() default 0;
    @IdRes int txtId() default 0;
    @IdRes int btnId() default 0;
    @StringRes int  messageId() default 0;
    String message() default "";
}
