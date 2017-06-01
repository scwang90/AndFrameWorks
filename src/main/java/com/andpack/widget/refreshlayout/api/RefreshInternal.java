package com.andpack.widget.refreshlayout.api;

import android.support.annotation.NonNull;
import android.view.View;

import com.andpack.widget.refreshlayout.constant.SpinnerStyle;
import com.andpack.widget.refreshlayout.listener.OnStateChangedListener;


/**
 * 刷新内部组件
 * Created by SCWANG on 2017/5/26.
 */

public interface RefreshInternal extends OnStateChangedListener {
    void onFinish();
    void setPrimaryColor(int... colors);
    @NonNull View getView();
    SpinnerStyle getSpinnerStyle();
}
