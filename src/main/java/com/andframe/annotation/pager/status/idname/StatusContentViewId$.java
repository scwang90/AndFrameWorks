package com.andframe.annotation.pager.status.idname;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定多状态页面的内容视图ID
 * Created by SCWANG on 2016/10/20.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusContentViewId$ {
    String value();
}
