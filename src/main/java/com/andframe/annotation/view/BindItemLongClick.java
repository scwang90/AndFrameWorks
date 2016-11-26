package com.andframe.annotation.view;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解式绑定控件<br>
 *  AdapterView.OnItemClickListener
 *  @ BindItemLongClick(R.id.viewId)
 *  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
 *  }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindItemLongClick {
    @IdRes int[] value() default {};
}

