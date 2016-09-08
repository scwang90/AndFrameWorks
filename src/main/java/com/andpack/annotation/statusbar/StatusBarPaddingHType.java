package com.andpack.annotation.statusbar;

import android.view.View;

/**
 * 指定任务透明
 * Created by SCWANG on 2016/9/8.
 */
public @interface StatusBarPaddingHType {
    Class<? extends View>[] value() ;
}
