package com.andpack.annotation.statusbar;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定任务透明
 * Created by SCWANG on 2016/9/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusBarPadding {
    @IdRes int[] value() default {};
}
