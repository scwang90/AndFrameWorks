package com.andframe.annotation.view;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解式绑定长按事件<br>
 *  @ BindLongClick(R.id.viewId)
 *  public boolean onLongClick(View v) {
 *  }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindLongClick {
    @IdRes int[] value();
}

