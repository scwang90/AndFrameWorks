package com.andpack.annotation;

import androidx.annotation.StringRes;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 页面绑定标题
 * Created by SCWANG on 2016/3/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindTitle {
    @StringRes
    int value() default View.NO_ID;
    String title() default "";
}
