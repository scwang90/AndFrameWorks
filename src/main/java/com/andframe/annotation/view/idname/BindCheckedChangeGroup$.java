package com.andframe.annotation.view.idname;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解式绑定控件<br>
 *  RadioGroup.OnCheckedChangeListener
 *  @ BindCheckedChangeGroup$("viewId")
 *  public void onCheckedChanged(RadioGroup group, int checkedId) {
 *  }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindCheckedChangeGroup$ {
    String[] value();
}

