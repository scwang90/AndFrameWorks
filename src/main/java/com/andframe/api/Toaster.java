package com.andframe.api;

import androidx.annotation.StringRes;

/**
 * Toast 气泡提示
 * Created by SCWANG on 2016/9/1.
 */
public interface Toaster {

    void toast(@StringRes int resId);

    void toast(CharSequence msg);

    void error(CharSequence remark, Throwable throwable);

    ToastBuilder builder();
}
