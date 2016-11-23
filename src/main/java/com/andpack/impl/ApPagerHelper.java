package com.andpack.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.andframe.$;
import com.andframe.activity.AfActivity;
import com.andframe.api.view.ViewQuery;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.fragment.AfFragment;
import com.andframe.listener.SafeOnClickListener;
import com.andframe.util.java.AfReflecter;
import com.andpack.R;
import com.andpack.annotation.RegisterEventBus;
import com.andpack.annotation.interpreter.StatusBarInterpreter;
import com.andpack.api.ApPager;
import com.andpack.application.ApApp;

import org.greenrobot.eventbus.EventBus;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApPagerHelper {

    protected ApPager pager;

    protected RegisterEventBus mEventBus;

    //<editor-fold desc="滑动关闭">
    private SwipeBackActivityHelper mSwipeBackHelper;
    protected boolean mIsUsingSwipeBack = false;
    //</editor-fold>

    public ApPagerHelper(ApPager pager) {
        this.pager = pager;
        mEventBus = AfReflecter.getAnnotation(pager.getClass(), getStopClass(), RegisterEventBus.class);
    }

    @NonNull
    protected Class<?> getStopClass() {
        return pager instanceof Activity ? AfActivity.class : AfFragment.class;
    }

    public void setTheme(@StyleRes int resid) {
        mIsUsingSwipeBack = resid == R.style.AppTheme_SwipeBack;
    }

    public void onCreate() {
        if (mEventBus != null) {
            EventBus.getDefault().register(pager);
        }
//        if (mIsUsingSwipeBack) {
//            mSwipeBackHelper = new SwipeBackActivityHelper(pager.getActivity());
//            mSwipeBackHelper.onActivityCreate();
//        }
    }

    public void onDestroy() {
        if (mEventBus != null) {
            EventBus.getDefault().unregister(pager);
        }
        if (ApApp.getApp().isDebug()) {
            ApApp.getApp().getRefWatcher().watch(this);
        }
    }

    public void onDestroyView() {
        if (mEventBus != null) {
            EventBus.getDefault().unregister(pager);
        }
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
}
