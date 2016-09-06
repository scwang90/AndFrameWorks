package com.andframe.api;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface ListItem<T> {
    /**
     * 将数据绑定到控件显示
     */
    void onBinding(View view, T model, int index);

    /**
     * 创建视图
     */
    View onCreateView(Context context, ViewGroup parent);
}