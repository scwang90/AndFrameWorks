package com.andframe.annotation.pager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定可刷新的竖直绑定布局
 * Created by SCWANG on 2016/3/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindFrameLayout {
    int value();
}
