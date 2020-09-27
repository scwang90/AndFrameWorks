package com.andframe.annotation.view;

import androidx.annotation.IdRes;
import androidx.annotation.Keep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解式绑定控件<br>
 *  @ BindClick(R.id.viewId)
 *  public void onClick(View v) {
 *  }
 */
@Keep
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindClick {
    @IdRes int[] value();
    int intervalTime() default 500;
}

