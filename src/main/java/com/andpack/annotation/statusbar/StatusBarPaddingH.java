package com.andpack.annotation.statusbar;

import android.support.annotation.IdRes;

/**
 * 指定任务透明
 * Created by SCWANG on 2016/9/8.
 */
public @interface StatusBarPaddingH {
    @IdRes int[] value() default {} ;
}
