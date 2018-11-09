package com.andframe.annotation.pager;

import com.andframe.model.constants.SoftInputAdjust;
import com.andframe.model.constants.SoftInputMode;
import com.andframe.model.constants.SoftInputState;

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
public @interface BindSoftInputMode {
    SoftInputMode value() default SoftInputMode.None;
    SoftInputState state() default SoftInputState.None;
    SoftInputAdjust adjust() default SoftInputAdjust.None;
}
