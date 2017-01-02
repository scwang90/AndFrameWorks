package com.andframe.annotation.pager;

import com.andframe.model.constants.LaunchMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给Fragment 绑定 LaunchMode
 * Created by SCWANG on 2017/1/1.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindLaunchMode {
    LaunchMode value() default LaunchMode.standard;
}
