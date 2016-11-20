package com.andpack.impl;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.task.AfDispatcher;
import com.andframe.util.android.AfDensity;
import com.andpack.R;
import com.andpack.impl.bezierlayout.BezierLayout;
import com.lcodecore.tkrefreshlayout.Footer.BottomProgressView;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 使用第三方刷新控件 PullRefreshLayout
 * Created by SCWANG on 2016/10/21.
 */

public class ApRefreshLayout implements RefreshLayouter/*, MoreLayouter*/ {

    private final TwinklingRefreshLayout mTwinkling;

    private boolean isRefreshing = false;
    //    private OnMoreListener mOnMoreListener;
    private OnRefreshListener mOnRefreshListener;

    public ApRefreshLayout(Context context) {
        this(context, R.color.colorPrimary);
    }

    public ApRefreshLayout(Context context, int primaryColorId) {
        this(context, primaryColorId, R.color.white);
    }

    public ApRefreshLayout(Context context, int primaryColorId, int frontColorId) {
        Resources resources = context.getResources();
        BezierLayout header = new BezierLayout(context);
        header.setFrontColor(resources.getColor(frontColorId));
        header.setBackColor(resources.getColor(primaryColorId));
        BottomProgressView bottom = new BottomProgressView(context);
        bottom.setIndicatorColor(resources.getColor(primaryColorId));
        mTwinkling = new TwinklingRefreshLayout(context);
        mTwinkling.setWaveHeight(AfDensity.dp2px(180));
        mTwinkling.setHeaderHeight(AfDensity.dp2px(100));
        mTwinkling.setHeaderView(header);
        mTwinkling.setBottomView(bottom);
        mTwinkling.setEnableLoadmore(false);
        mTwinkling.setOnRefreshListener(twinklingListener);
    }

    public ViewGroup getLayout() {
        return mTwinkling;
    }

    @Override
    public void setContenView(View content) {
        mTwinkling.addView(content, MATCH_PARENT, MATCH_PARENT);
        mTwinkling.setBackgroundDrawable(content.getBackground());
    }

    @Override
    public void setRefreshComplete() {
        isRefreshing = false;
        AfDispatcher.dispatch(mTwinkling::finishRefreshing, 1000);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    @Override
    public void setLastRefreshTime(Date date) {

    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    //    @Override
//    public void setOnMoreListener(OnMoreListener listener) {
//        mOnMoreListener = listener;
//    }
//
//    @Override
//    public void setLoadMoreEnabled(boolean enable) {
//        mTwinkling.setEnableLoadmore(enable);
//    }
//
//    @Override
//    public void finishLoadMore() {
//        mTwinkling.finishLoadmore();
//    }
//
    TwinklingRefreshLayout.OnRefreshListener twinklingListener = new TwinklingRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh(TwinklingRefreshLayout refreshLayout) {
            if (mOnRefreshListener != null) {
                if (mOnRefreshListener.onRefresh()) {
                    isRefreshing = true;
                } else {
                    setRefreshComplete();
                }
            } else {
                AfDispatcher.dispatch(() -> setRefreshComplete(), 2000);
            }
        }

//        @Override
//        public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//            if (mOnMoreListener != null) {
//                if (!mOnMoreListener.onMore()) {
//                    mTwinkling.finishRefreshing();
//                }
//            } else {
//                AfDispatcher.dispatch(() -> mTwinkling.finishRefreshing(), 2000);
//            }
//        }
    };
}
