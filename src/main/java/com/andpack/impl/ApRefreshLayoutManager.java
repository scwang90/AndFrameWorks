package com.andpack.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.andframe.api.pager.items.MoreLayoutManager;
import com.andframe.api.pager.items.OnMoreListener;
import com.andframe.api.pager.status.OnRefreshListener;
import com.andframe.api.pager.status.RefreshLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

/**
 * 使用第三方刷新控件 PullRefreshLayout
 * Created by SCWANG on 2016/10/21.
 */

@SuppressWarnings("unused")
public class ApRefreshLayoutManager implements RefreshLayoutManager<SmartRefreshLayout>, MoreLayoutManager {

    protected final SmartRefreshLayout mRefreshLayout;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new BezierRadarHeader(context));
    }

    public ApRefreshLayoutManager(Context context) {
        mRefreshLayout = new SmartRefreshLayout(context);
        initRefreshLayout(mRefreshLayout);
    }

    public ApRefreshLayoutManager(Context context, int primaryId, int frontId) {
        mRefreshLayout = new SmartRefreshLayout(context);
        mRefreshLayout.setPrimaryColorsId(primaryId, frontId);
        initRefreshLayout(mRefreshLayout);
    }

    public ApRefreshLayoutManager(SmartRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        initRefreshLayout(refreshLayout);
    }

    protected void initRefreshLayout(SmartRefreshLayout refreshLayout) {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableOverScrollBounce(false);
        refreshLayout.setRefreshHeader(newHeader(refreshLayout.getContext()));
        refreshLayout.setRefreshFooter(newFooter(refreshLayout.getContext()));
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setEnableFooterFollowWhenNoMoreData(true);
    }

    protected RefreshHeader newHeader(Context context) {
        return new BezierRadarHeader(context);
    }

    protected RefreshFooter newFooter(Context context) {
        return new ClassicsFooter(context);
    }

    @NonNull
    @Override
    public SmartRefreshLayout getLayout() {
        return mRefreshLayout;
    }

    @Override
    public void setContentView(View content) {
        ViewGroup.LayoutParams params = content.getLayoutParams();
        int height = params == null ? MATCH_PARENT : params.height;
        if (params != null && params.height == WRAP_CONTENT) {
            params.height = MATCH_PARENT;
        }
//        mRefreshLayout.addView(content, MATCH_PARENT, height == 0 ? MATCH_PARENT : height);
        mRefreshLayout.setRefreshContent(content, MATCH_PARENT, height == 0 ? MATCH_PARENT : height);
    }

    @Override
    public void wrapper(View content) {
        ViewParent parent = content.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            ViewGroup.LayoutParams params = content.getLayoutParams();
            int index = group.indexOfChild(content);
            group.removeViewAt(index);
            setContentView(content);
            group.addView(getLayout(),index,params);
        }
    }

    @Override
    public void finishRefresh(boolean success) {
        mRefreshLayout.finishRefresh(success);
    }

    @Override
    public void autoRefresh() {
        mRefreshLayout.autoRefresh();
    }

    //    @Override
//    public void setRefreshComplete() {
//        mRefreshLayout.finishRefresh();
//    }
//
//    @Override
//    public void setRefreshFailed() {
//        mRefreshLayout.finishRefresh(false);
//    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            if (!listener.onRefresh()) {
                mRefreshLayout.finishRefresh(0, false, null);
            }
        });
    }

    @Override
    public void setLastRefreshTime(Date date) {
        final RefreshHeader header = mRefreshLayout.getRefreshHeader();
        if (header instanceof ClassicsHeader) {
            ((ClassicsHeader) header).setLastUpdateTime(date);
        }
    }

    @Override
    public boolean isRefreshing() {
        return mRefreshLayout.getState() == RefreshState.Refreshing;
    }

    @Override
    public void setOnMoreListener(OnMoreListener listener) {
        mRefreshLayout.setOnLoadMoreListener((refreshLayout)-> listener.onMore());
    }

    @Override
    public void setLoadMoreEnabled(boolean enable) {
        mRefreshLayout.setEnableLoadMore(enable);
    }

    @Override
    public void setNoMoreData(boolean noMoreData) {
        mRefreshLayout.setNoMoreData(noMoreData);
    }

    @Override
    public void finishLoadMore() {
        mRefreshLayout.finishLoadMore();
    }
}
