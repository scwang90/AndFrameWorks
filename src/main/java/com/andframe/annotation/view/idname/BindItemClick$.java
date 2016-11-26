package com.andframe.annotation.view.idname;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解式绑定控件<br>
 *  AdapterView.OnItemClickListener
 *  @ BindItemClick$("viewId")
 *  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
 *  }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindItemClick$ {
    String[] value();
    int intervalTime() default 1000;
}

