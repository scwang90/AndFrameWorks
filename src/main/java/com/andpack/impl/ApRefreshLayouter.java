package com.andpack.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.pager.status.OnRefreshListener;
import com.andframe.api.pager.status.RefreshLayouter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;

import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

/**
 * 使用第三方刷新控件 PullRefreshLayout
 * Created by SCWANG on 2016/10/21.
 */

public class ApRefreshLayouter implements RefreshLayouter<SmartRefreshLayout> {

    protected final SmartRefreshLayout mRefreshLayout;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> new BezierRadarHeader(context));
    }

    public ApRefreshLayouter(Context context) {
        mRefreshLayout = new SmartRefreshLayout(context);
        mRefreshLayout.setRefreshHeader(newHeader(context));
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableOverScrollBounce(false);
    }

    public ApRefreshLayouter(Context context, int primaryId, int frontId) {
        mRefreshLayout = new SmartRefreshLayout(context);
        mRefreshLayout.setRefreshHeader(newHeader(context));
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setPrimaryColorsId(primaryId, frontId);
        mRefreshLayout.setEnableOverScrollBounce(false);
    }

    public ApRefreshLayouter(SmartRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        mRefreshLayout.setRefreshHeader(newHeader(refreshLayout.getContext()));
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableOverScrollBounce(false);
    }

    protected RefreshHeader newHeader(Context context) {
        return new BezierRadarHeader(context);
    }

    @NonNull
    @Override
    public SmartRefreshLayout getLayout() {
        return mRefreshLayout;
    }

    @Override
    public void setContenView(View content) {
        ViewGroup.LayoutParams params = content.getLayoutParams();
        int height = params == null ? MATCH_PARENT : params.height;
        if (params != null && params.height == WRAP_CONTENT) {
            params.height = MATCH_PARENT;
        }
        mRefreshLayout.addView(content, MATCH_PARENT, height == 0 ? MATCH_PARENT : height);
    }

    @Override
    public void setRefreshComplete() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void setRefreshFailed() {
        mRefreshLayout.finishRefresh(false);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            if (!listener.onRefresh()) {
                mRefreshLayout.finishRefresh(0, false);
            }
        });
    }

    @Override
    public void setLastRefreshTime(Date date) {

    }

    @Override
    public boolean isRefreshing() {
        return mRefreshLayout.isRefreshing();
    }
}
