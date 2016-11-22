package com.andframe.impl.multistatus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.multistatus.RefreshLayouter;

import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 可进行刷新操作的布局
 * Created by SCWANG on 2016/10/20.
 */

public class DefaultRefreshLayouter implements RefreshLayouter<SwipeRefreshLayout>, SwipeRefreshLayout.OnRefreshListener {

    private final SwipeRefreshLayout mRefreshLayout;
    private OnRefreshListener mOnRefreshListener;
    private View mContentView;

    public DefaultRefreshLayouter(Context context) {
        this(new SwipeRefreshLayout(context));
    }

    public DefaultRefreshLayouter(SwipeRefreshLayout layout) {
        mRefreshLayout = layout;
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light);
    }

    public SwipeRefreshLayout getLayout() {
        return mRefreshLayout;
    }

    @Override
    public void setContenView(View content) {
        if (mContentView != null) {
            mRefreshLayout.removeView(mContentView);
        }
        mRefreshLayout.addView(mContentView = content, MATCH_PARENT, MATCH_PARENT);
        Drawable background = content.getBackground();
        if (background != null) {
            mRefreshLayout.setBackgroundDrawable(content.getBackground());
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

    public void setRefreshComplete() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (mOnRefreshListener == null || !mOnRefreshListener.onRefresh()) {
            mRefreshLayout.setRefreshing(false);
        }
    }
}
