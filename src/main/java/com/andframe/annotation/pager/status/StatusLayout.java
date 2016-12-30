package com.andframe.annotation.pager.status;


import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

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
public @interface StatusLayout {
    @LayoutRes int progress();
    @LayoutRes int empty();
    @LayoutRes int error() default 0;
    @LayoutRes int invalidNet() default 0;
    @IdRes int errorTxtId() default 0;
    @IdRes int progressTxtId() default 0;
    @IdRes int emptyTxtId() default 0;
    @IdRes int invalidNetTxtId() default 0;
}
