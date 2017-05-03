package com.andframe.annotation.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 整数范围
 * Created by Administrator on 2016/3/2.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntRange {
    String value();
    int from();
    int to();
}
