package com.andframe.api;

import androidx.annotation.StringRes;

/**
 * Toast 气泡提示
 * Created by SCWANG on 2016/9/1.
 */
public interface ToastBuilder {

    void show();
    ToastBuilder longer();
    ToastBuilder shorter();
    ToastBuilder duration(int duration);
    ToastBuilder msg(CharSequence msg);
    ToastBuilder msg(@StringRes int  resId);

}
