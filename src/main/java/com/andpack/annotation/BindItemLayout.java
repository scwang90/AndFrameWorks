package com.andpack.annotation;

import androidx.annotation.LayoutRes;

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
public @interface BindItemLayout {
    @LayoutRes int value();
}
