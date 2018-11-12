package com.andframe.annotation.pager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定 Fragment 是App主页面
 * Created by SCWANG on 2017/1/1.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindMainPager {
    String value() default "";
}
