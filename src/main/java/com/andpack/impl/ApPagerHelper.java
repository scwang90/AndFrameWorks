package com.andpack.impl;

import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.andframe.$;
import com.andframe.api.view.ViewQuery;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.listener.SafeOnClickListener;
import com.andframe.task.AfHandlerTask;
import com.andframe.util.java.AfReflecter;
import com.andframe.widget.AfRefreshScorllView;
import com.andframe.widget.pulltorefresh.AfPullToRefreshBase;
import com.andpack.R;
import com.andpack.annotation.interpreter.StatusBarInterpreter;
import com.andpack.api.ApPager;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApPagerHelper implements AfPullToRefreshBase.OnRefreshListener {

    protected ApPager pager;

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
//        if (mIsUsingSwipeBack) {
//            mSwipeBackHelper = new SwipeBackActivityHelper(pager.getActivity());
//            mSwipeBackHelper.onActivityCreate();
//        }
    }

    public void onViewCreated() {
        try {
            StatusBarInterpreter.interpreter(pager);
            if (pager.getView() != null) {
                $.query(pager.getView())
                        .$(Toolbar.class)
                        .foreach(Toolbar.class, (ViewQuery.ViewEacher<Toolbar>)
                                view -> view.setNavigationOnClickListener(new SafeOnClickListener(
                                        v -> pager.getActivity().finish())));
            }
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, ("ApPagerHelper.onViewCreated 失败"));
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
            if (mIsUsingSwipeBack) {
                mSwipeBackHelper = new SwipeBackActivityHelper(pager.getActivity());
                mSwipeBackHelper.onActivityCreate();
            }
            if (mSwipeBackHelper != null) {
                mSwipeBackHelper.onPostCreate();
                SwipeBackLayout layout = mSwipeBackHelper.getSwipeBackLayout();
                ViewGroup root = (ViewGroup)pager.getActivity().getWindow().getDecorView();
                FrameLayout frame = new FrameLayout(pager.getContext());
                for (int i = 0; i < root.getChildCount(); i++) {
                    View child = root.getChildAt(i);
                    if (child != layout) {
                        root.removeViewAt(i--);
                        frame.addView(child);
                    }
                }
                if (frame.getChildCount() > 0) {
                    View view = layout.getChildAt(0);
                    layout.removeViewAt(0);
                    layout.addView(frame);
                    frame.addView(view, 0);
                    AfReflecter.setMemberByType(layout, frame, View.class);
                }
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
