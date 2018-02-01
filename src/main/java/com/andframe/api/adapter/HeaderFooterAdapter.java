package com.andframe.api.adapter;

import android.view.View;

/**
 * 带有头部和尾部的适配器
 * Created by SCWANG on 2016/12/31.
 */

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface HeaderFooterAdapter<T> extends ItemsViewerAdapter<T> {

    boolean addHeader(ItemViewer<T> item);
    boolean addHeaderLayout(int layoutId);
    boolean addHeaderView(View view);
    boolean addFooter(ItemViewer<T> item);
    boolean addFooterLayout(int layoutId);
    boolean addFooterView(View view);

    boolean removeHeader(ItemViewer<T> item);
    boolean removeFooter(ItemViewer<T> item) ;
    boolean removeHeaderView(View view);
    boolean removeFooterView(View view) ;

    void clearHeader();
    void clearFooter();

    boolean hasHeaderView(View view);
    boolean hasFooterView(View view);
}
