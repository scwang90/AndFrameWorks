package com.andframe.annotation.pager.status;


import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

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
public @interface StatusError {
    @LayoutRes int value();
    @IdRes int txtId();
    @IdRes int btnId() default 0;
}
