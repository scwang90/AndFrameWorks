package com.andpack.annotation;

import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定状态栏透明(Fragment 必须手动指定)
 * Created by SCWANG on 2016/9/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BackgroundTranslucent {
    @FloatRange(from = 0.0, to = 1.0) float value() default 0f;//alpha
    @ColorRes int color() default android.R.color.black ;
}
