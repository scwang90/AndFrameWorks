package com.andframe.api.viewer;

import android.view.ViewGroup;

import com.andframe.api.pager.status.OnRefreshListener;

import java.util.Date;

/**
 * 可刷新的 多项视图控件
 * Created by SCWANG on 2016/10/18.
 */

public interface ItemsRefreshableViewer<T extends ViewGroup> extends ItemsViewer<T> {
    void finishRefresh();
    void finishRefreshFail();
    void finishLoadMore();
    void removeMoreView();
    void addMoreView();
    void setLastUpdateTime(Date date);
    void setOnRefreshListener(OnRefreshListener refreshListener);
    ViewGroup getRefreshableLayout();
}
