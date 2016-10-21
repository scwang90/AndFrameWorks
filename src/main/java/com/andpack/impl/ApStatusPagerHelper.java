package com.andpack.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andpack.api.ApPager;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.Date;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApStatusPagerHelper extends ApPagerHelper {

    public ApStatusPagerHelper(ApPager pager) {
        super(pager);
    }

    public RefreshLayouter createRefreshLayouter(Context context) {
        return new RefreshLayouter() {
            boolean isRefreshing = false;

            PullToRefreshView mRefreshView = new PullToRefreshView(context);

            @Override
            public ViewGroup getLayout() {
                return mRefreshView;
            }

            @Override
            public void setContenView(View content) {
                mRefreshView.addView(content);
            }

            @Override
            public void setRefreshing(boolean refreshing) {
                isRefreshing = refreshing;
                mRefreshView.setRefreshing(refreshing);
            }

            @Override
            public void setOnRefreshListener(OnRefreshListener listener) {
                mRefreshView.setOnRefreshListener(() -> {
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
        };
    }

    public View findContentView() {
        return null;
    }
}
