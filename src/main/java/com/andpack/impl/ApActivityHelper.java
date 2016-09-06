package com.andpack.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;

import com.andpack.R;
import com.andpack.annotation.BindStatusBarMode;
import com.andframe.activity.AfActivity;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfReflecter;
import com.flyco.systembar.SystemBarHelper;

import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApActivityHelper {

    private Activity activity;

    //<editor-fold desc="滑动关闭">
    private SwipeBackActivityHelper mSwipeBackHelper;
    protected boolean mIsUsingSwipeBack = false;
    //</editor-fold>


    public ApActivityHelper(Activity activity) {
        this.activity = activity;
    }

    public void setTheme(@StyleRes int resid) {
        mIsUsingSwipeBack = resid == R.style.AppTheme_SwipeBack;
    }

    public void onCreate(Bundle bundle, AfIntent intent) {
        if (mIsUsingSwipeBack) {
            mSwipeBackHelper = new SwipeBackActivityHelper(activity);
            mSwipeBackHelper.onActivityCreate();
        }
        BindStatusBarMode mode = AfReflecter.getAnnotation(activity.getClass(), AfActivity.class, BindStatusBarMode.class);
        if (mode != null) {
            switch (mode.value()) {
                case normal:
                    break;
                case translucent:
                    SystemBarHelper.immersiveStatusBar(activity, 0);
                    View top = activity.findViewById(R.id.af_titlebar_layout);
                    if (top != null) {
                        SystemBarHelper.setHeightAndPadding(activity, top);
                    }
                    //$.with(activity).id(R.id.af_titlebar_layout).view()
                    break;
                case translucent_white:
                    SystemBarHelper.setStatusBarDarkMode(activity);
                    SystemBarHelper.tintStatusBar(activity, 0XFFEAEAEA, 0);
                    break;
            }
        }
    }

    public View findViewById(int id) {
        if (mSwipeBackHelper != null) {
            try {
                return mSwipeBackHelper.findViewById(id);
            } catch (Exception e) {
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
        } catch (Exception e) {
            AfExceptionHandler.handle(e, ("SwipeBackActivityHelper.scrollToFinishActivity 失败"));
        }
        return false;
    }
}
