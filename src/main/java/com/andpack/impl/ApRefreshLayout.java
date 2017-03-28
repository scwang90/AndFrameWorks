package com.andpack.impl;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.andframe.$;
import com.andframe.api.pager.status.OnRefreshListener;
import com.andframe.api.pager.status.RefreshLayouter;
import com.andframe.task.AfDispatcher;
import com.andframe.util.android.AfDensity;
import com.andframe.util.java.AfReflecter;
import com.andpack.R;
import com.andpack.impl.bezierlayout.BezierLayout;
import com.lcodecore.tkrefreshlayout.Footer.BottomProgressView;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.Collections;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 使用第三方刷新控件 PullRefreshLayout
 * Created by SCWANG on 2016/10/21.
 */

public class ApRefreshLayout implements RefreshLayouter<TwinklingRefreshLayout>/*, MoreLayouter*/ {

    private final TwinklingRefreshLayoutEx mTwinkling;

    private boolean isRefreshing = false;
    private OnRefreshListener mOnRefreshListener;
    private int mRealContentId = 0;
    //    private OnMoreListener mOnMoreListener;

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
        mTwinkling = new TwinklingRefreshLayoutEx(context);
        mTwinkling.setWaveHeight(AfDensity.dp2px(180));
        mTwinkling.setHeaderHeight(AfDensity.dp2px(100));
        mTwinkling.setHeaderView(header);
        mTwinkling.setBottomView(bottom);
        mTwinkling.setEnableLoadmore(false);
        mTwinkling.setOnRefreshListener(twinklingListener);
    }

    public ApRefreshLayout setRealContentId(@IdRes int Id) {
        mRealContentId = Id;
        return this;
    }

    public TwinklingRefreshLayout getLayout() {
        return mTwinkling;
    }

    @Override
    public void setContenView(View content) {
        View contentView = null;
        if (mRealContentId > 0) {
            contentView = content.findViewById(mRealContentId);
        }
        if (contentView == null && content instanceof AbsListView) {
            contentView = content;
            FrameLayout layout = new FrameLayout(content.getContext());
            layout.addView(content, MATCH_PARENT, MATCH_PARENT);
            layout.setBackgroundDrawable(content.getBackground());
            content = layout;
        }
        mTwinkling.addView(content, MATCH_PARENT, MATCH_PARENT);
        mTwinkling.setBackgroundDrawable(content.getBackground());
        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(content));
        while (!views.isEmpty() && contentView == null){
            View view = views.poll();
            if (view != null) {
                if (view instanceof AbsListView
                        || view instanceof RecyclerView
                        || view instanceof ScrollView
                        || view instanceof WebView) {
                    contentView = view;
                } else if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        }
        if (content instanceof CoordinatorLayout) {
            AppBarLayout layout = $.query(content).$(AppBarLayout.class).view();
            if (layout != null) {
//                setRealContentView(new AppBarLayoutWrapper(layout, contentView));
                layout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                    if (verticalOffset >= 0) {
                        mTwinkling.setTwinklingEnabled(true);
                    } else {
                        mTwinkling.setTwinklingEnabled(false);
                    }
                });
                return;
            }
        }
        if (contentView != null && contentView != content && !mTwinkling.hasRealContentView()) {
            setRealContentView(contentView);
        }
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
    TwinklingRefreshLayout.OnRefreshListener twinklingListener = new com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh(com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout refreshLayout) {
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

    public ApRefreshLayout setRealContentView(View view) {
        mTwinkling.setRealContentView(view);
        return this;
    }

    public class TwinklingRefreshLayoutEx extends TwinklingRefreshLayout {


        private ChildViewWrapper wrapper = null;
        private boolean mTwinklingEnabled = true;

        public TwinklingRefreshLayoutEx(Context context) {
            super(context);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (wrapper != null) {
                wrapperChildView();
            }
        }

        private void wrapperChildView() {
            try {
                View view = AfReflecter.getPreciseMemberByType(this, View.class, TwinklingRefreshLayout.class);
                if (view != null && wrapper != view) {
                    wrapper.setOrginView(view);
                    AfReflecter.setPreciseMemberByType(this, wrapper, View.class, TwinklingRefreshLayout.class);
                }
            } catch (IllegalAccessException ignored) {
            }
        }

        public void setRealContentView(View realView) {
            wrapper = new ChildViewWrapper(realView, realView);
            wrapperChildView();
        }

        public boolean hasRealContentView() {
            return wrapper != null;
        }

        public void setTwinklingEnabled(boolean enabled) {
            mTwinklingEnabled = enabled;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return mTwinklingEnabled && super.onInterceptTouchEvent(ev);
        }
    }

    private class ChildViewWrapper extends View {

        private View view;
        private View realView;

        public ChildViewWrapper(View view,View realView) {
            super(view.getContext());
            this.view = view;
            this.realView = realView;
        }

        @Override
        public float getTranslationY() {
            return view.getTranslationY();
        }

        @Override
        public void setTranslationY(float translationY) {
            view.setTranslationY(translationY);
        }

        @Override
        public ViewPropertyAnimator animate() {
            return view.animate();
        }

        @Override
        public void setOnTouchListener(OnTouchListener l) {
            view.setOnTouchListener(l);
        }

        @Override
        public boolean canScrollVertically(int direction) {
            return realView.canScrollVertically(direction);
        }

        public void setOrginView(View view) {
            this.view = view;
        }
    }

}
