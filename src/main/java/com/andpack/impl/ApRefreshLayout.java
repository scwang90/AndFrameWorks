package com.andpack.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.multistatus.RefreshLayouter;
import com.baoyz.widget.PullRefreshLayout;

import java.util.Date;
import java.util.Random;

/**
 * 使用第三方刷新控件 PullRefreshLayout
 * Created by SCWANG on 2016/10/21.
 */

public class ApRefreshLayout implements RefreshLayouter {

    private PullRefreshLayout mRefreshLayout;
    private boolean isRefreshing = false;

    public ApRefreshLayout(Context context) {
        this.mRefreshLayout = new PullRefreshLayout(context);
        this.mRefreshLayout.setRefreshStyle(new Random().nextInt(5));
    }

    @Override
    public ViewGroup getLayout() {
        return mRefreshLayout;
    }

    @Override
    public void setContenView(View content) {
        mRefreshLayout.addView(content);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
        mRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshLayout.setOnRefreshListener(() -> {
            if (listener.onRefresh()) {
                isRefreshing = true;
            } else {
                setRefreshing(false);
            }
        });
    }

    @Override
    public void setLastRefreshTime(Date date) {

    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }
}
