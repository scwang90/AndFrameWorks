package com.andframe.impl.multistatus;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.multistatus.RefreshLayouter;

/**
 * 可进行刷新操作的布局
 * Created by SCWANG on 2016/10/20.
 */

public class DefaultRefreshLayouter implements RefreshLayouter, SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout mRefreshLayout;
    private OnRefreshListener mOnRefreshListener;

    public DefaultRefreshLayouter(Context context) {
        mRefreshLayout = new SwipeRefreshLayout(context);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public ViewGroup getLayout() {
        return mRefreshLayout;
    }

    @Override
    public void setContenView(View content) {
        if (mRefreshLayout.getChildCount() > 0) {
            mRefreshLayout.removeAllViews();
        }
        mRefreshLayout.addView(content);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    @Override
    public boolean isRefreshing() {
        return mRefreshLayout.isRefreshing();
    }

    public void setRefreshing(boolean refreshing) {
        mRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onRefresh() {
        if (mOnRefreshListener == null || !mOnRefreshListener.onRefresh()) {
            mRefreshLayout.setRefreshing(false);
        }
    }
}
