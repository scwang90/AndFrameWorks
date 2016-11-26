package com.andframe.annotation.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 控件注入之后执行<br>
 *  @ BindViewCreated()
 *  public void onInit() {
 *  }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindViewCreated {
    /**
     * 和其BindViewCreated标记的方法的执行序号
     */
    int value() default 0;
    /**
     * 是否允许抛出异常
     * 默认true允许异常
     * @return false 不允许异常
     */
    boolean exception() default true;
}

