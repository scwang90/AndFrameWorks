package com.andframe.annotation.view;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解式绑定控件<br>
 *  CompoundButton.OnCheckedChangeListener
 *  @ BindCheckedChange(R.id.viewId)
 *  public void onCheckedChanged(CompoundButton button, boolean isChecked) {
 *  }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindCheckedChange {
    @IdRes int[] value();
}

