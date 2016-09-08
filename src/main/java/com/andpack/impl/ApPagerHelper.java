package com.andpack.impl;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;

import com.andframe.$;
import com.andframe.activity.AfActivity;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.task.AfHandlerTask;
import com.andframe.util.java.AfReflecter;
import com.andframe.widget.AfRefreshScorllView;
import com.andframe.widget.pulltorefresh.AfPullToRefreshBase;
import com.andpack.R;
import com.andpack.annotation.BindScorllView;
import com.andpack.annotation.BindStatusBarMode;
import com.andpack.annotation.interpreter.StatusBarInterpreter;
import com.andpack.api.ApPager;
import com.flyco.systembar.SystemBarHelper;

import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApPagerHelper implements AfPullToRefreshBase.OnRefreshListener {

    private ApPager pager;

    private AfRefreshScorllView mRfScorllView;

    //<editor-fold desc="滑动关闭">
    private SwipeBackActivityHelper mSwipeBackHelper;
    protected boolean mIsUsingSwipeBack = false;
    //</editor-fold>

    public ApPagerHelper(ApPager pager) {
        this.pager = pager;
    }

    public void setTheme(@StyleRes int resid) {
        mIsUsingSwipeBack = resid == R.style.AppTheme_SwipeBack;
    }

    public void onCreate() {
        try {
            if (mIsUsingSwipeBack) {
                mSwipeBackHelper = new SwipeBackActivityHelper(pager.getActivity());
                mSwipeBackHelper.onActivityCreate();
            }
            if (!StatusBarInterpreter.interpreter(pager)) {
                BindStatusBarMode mode = AfReflecter.getAnnotation(pager.getClass(), AfActivity.class, BindStatusBarMode.class);
                if (mode != null) {
                    switch (mode.value()) {
                        case normal:
                            break;
                        case translucent:
                            SystemBarHelper.immersiveStatusBar(pager.getActivity(), 0);
                            View top = pager.findViewById(R.id.af_titlebar_layout);
                            if (top != null) {
                                SystemBarHelper.setHeightAndPadding(pager.getContext(), top);
                            }
                            //$.with(pager).id(R.id.af_titlebar_layout).view()
                            break;
                        case translucent_white:
                            SystemBarHelper.setStatusBarDarkMode(pager.getActivity());
    //                    SystemBarHelper.tintStatusBar(pager.getActivity(), 0XFFEAEAEA, 0);
                            SystemBarHelper.tintStatusBar(pager.getActivity(), 0XFFFFFFFF, 0);
                            break;
                    }
                }
            }
            if (pager.getClass().isAnnotationPresent(BindScorllView.class)) {
                BindScorllView bind = pager.getClass().getAnnotation(BindScorllView.class);
                ScrollView scrollView = pager.findViewById(bind.value(), ScrollView.class);
                if (scrollView != null) {
                    mRfScorllView = new AfRefreshScorllView(pager, bind.value());
                    mRfScorllView.setOnRefreshListener(this);
                    mRfScorllView.getHeaderLayout().setBackgroundResource(R.color.gray_white);
                }
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, ("ApPagerHelper.onCreate 失败"));
        }
    }

    public void onAfterViews() {
        try {
            Toolbar toolbar = (Toolbar) $.with(pager).$(Toolbar.class).view();
            if (toolbar != null) {
                toolbar.setNavigationOnClickListener(v -> pager.getActivity().finish());
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, ("ApPagerHelper.onAfterViews 失败"));
        }
    }

    public View findViewById(int id) {
        if (mSwipeBackHelper != null) {
            try {
                return mSwipeBackHelper.findViewById(id);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, ("SwipeBackActivityHelper.findViewById 失败"));
            }
        }
        return null;
    }

    public void onPostCreate(Bundle bundle) {
        try {
            if (mSwipeBackHelper != null) {
                mSwipeBackHelper.onPostCreate();
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, ("SwipeBackActivityHelper.onPostCreate 失败"));
        }
    }

    public boolean finish() {
        try {
            if (mSwipeBackHelper != null) {
                mSwipeBackHelper.getSwipeBackLayout().scrollToFinishActivity();
                mSwipeBackHelper = null;
                return true;
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, ("SwipeBackActivityHelper.scrollToFinishActivity 失败"));
        }
        return false;
    }

    //<editor-fold desc="下啦刷新">
    @Override
    public boolean onMore() {
        return pager.onMore();
    }

    @Override
    public boolean onRefresh() {
        return pager.onRefresh() || $.with().postTask(new AfHandlerTask() {
            @Override
            protected void onHandle() {
            }

            @Override
            protected void onWorking() throws Exception {
                Thread.sleep(1000);
            }
        }).setListener(mRfScorllView).prepare();
    }

    //</editor-fold>

}
