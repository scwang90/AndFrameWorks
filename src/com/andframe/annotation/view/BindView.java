package com.andframe.annotation.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解式绑定控件<br>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
    public int value();
    public boolean click() default false;
    public String onClick() default "";
	public String longClick() default "";
	public String itemClick() default "";
	public String itemLongClick() default "";
	public Select select() default @Select(selected="") ;
}

