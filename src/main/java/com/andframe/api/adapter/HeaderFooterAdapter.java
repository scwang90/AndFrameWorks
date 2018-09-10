package com.andframe.api.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 带有头部和尾部的适配器
 * Created by SCWANG on 2016/12/31.
 */

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface HeaderFooterAdapter<T> extends ItemsViewerAdapter<T> {

    boolean addHeader(@NonNull ItemViewer<T> item);
    boolean addHeaderLayout(@LayoutRes int layoutId);
    boolean addHeaderView(@NonNull View view);
    boolean addFooter(@NonNull ItemViewer<T> item);
    boolean addFooterLayout(@LayoutRes int layoutId);
    boolean addFooterView(@NonNull View view);

    boolean removeHeader(ItemViewer<T> item);
    boolean removeFooter(ItemViewer<T> item) ;
    boolean removeHeaderView(@NonNull View view);
    boolean removeFooterView(@NonNull View view) ;

    void clearHeader();
    void clearFooter();

    boolean hasHeaderView(View view);
    boolean hasFooterView(View view);
}
