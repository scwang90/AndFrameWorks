package com.andframe.api.pager.status;

import android.view.ViewGroup;

import java.util.Date;

/**
 * 可进行刷新操作的布局
 * Created by SCWANG on 2016/10/20.
 */

public interface RefreshLayoutManager<T extends ViewGroup> extends LayoutManager<T> {
//    void setRefreshComplete();
//    void setRefreshFailed();
    void finishRefresh(boolean success);
    void setOnRefreshListener(OnRefreshListener listener);
    void setLastRefreshTime(Date date);

    boolean isRefreshing();
}
