package com.andframe.annotation.pager.items;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记页面为单页数据（不使用数据分页加载）
 * Created by SCWANG on 2016/3/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemsSinglePage {
    int value() default 0;
}
