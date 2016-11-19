package com.andframe.api.multistatus;

import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

/**
 * 可进行刷新操作的布局
 * Created by SCWANG on 2016/10/20.
 */

public interface RefreshLayouter {
    ViewGroup getmTwinkling();
    void setContenView(View content);
    void setRefreshComplete();
    void setOnRefreshListener(OnRefreshListener listener);
    void setLastRefreshTime(Date date);

    boolean isRefreshing();
}
