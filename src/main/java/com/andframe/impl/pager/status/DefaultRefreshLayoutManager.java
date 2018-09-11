package com.andframe.impl.pager.status;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.andframe.api.pager.status.OnRefreshListener;
import com.andframe.api.pager.status.RefreshLayoutManager;

import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 可进行刷新操作的布局
 * Created by SCWANG on 2016/10/20.
 */

public class DefaultRefreshLayoutManager implements RefreshLayoutManager<SwipeRefreshLayout>, SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout mRefreshLayout;
    private OnRefreshListener mOnRefreshListener;
    private View mContentView;

    public DefaultRefreshLayoutManager(Context context) {
        this(new SwipeRefreshLayout(context));
    }

    public DefaultRefreshLayoutManager(SwipeRefreshLayout layout) {
        mRefreshLayout = layout;
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light);
    }

    @NonNull
    public SwipeRefreshLayout getLayout() {
        return mRefreshLayout;
    }

    @Override
    public void setContentView(View content) {
        if (mContentView != null) {
            mRefreshLayout.removeView(mContentView);
        }
        ViewGroup.LayoutParams params = content.getLayoutParams();
        int height = params == null ? MATCH_PARENT : params.height;
        mRefreshLayout.addView(mContentView = content, MATCH_PARENT, height == 0 ? MATCH_PARENT : height);
        Drawable background = content.getBackground();
        if (background != null) {
            mRefreshLayout.setBackgroundDrawable(content.getBackground());
        }
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

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    @Override
    public void setLastRefreshTime(Date date) {

    }

    @Override
    public boolean isRefreshing() {
        return mRefreshLayout.isRefreshing();
    }

//    @Override
//    public void setRefreshComplete() {
//        mRefreshLayout.setRefreshing(false);
//    }
//
//    @Override
//    public void setRefreshFailed() {
//        mRefreshLayout.setRefreshing(false);
//    }

    @Override
    public void finishRefresh(boolean success) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (mOnRefreshListener == null || !mOnRefreshListener.onRefresh()) {
            mRefreshLayout.setRefreshing(false);
        }
    }
}
