package com.andframe.impl.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;

import com.andframe.activity.AfActivity;
import com.andframe.activity.AfFragmentActivity;
import com.andframe.api.pager.PagerManager;
import com.andframe.application.AfApp;
import com.andframe.exception.AfException;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfFragment;

import java.util.Stack;

/**
 * 页面堆栈管理器
 * Created by SCWANG on 2016/11/29.
 */

public class AfPagerManager implements PagerManager {

    //<editor-fold desc="功能实现">
    // 当前主页面
    private Stack<AfActivity> mStackActivity = new Stack<>();

    public AfPagerManager() {
    }

    @Override@CallSuper
    public void onActivityCreated(AfActivity activity) {
        if (!mStackActivity.contains(activity)) {
            mStackActivity.push(activity);
        }
    }

    @Override@CallSuper
    public void onActivityDestroy(AfActivity activity) {
        if (mStackActivity.contains(activity)) {
            mStackActivity.remove(activity);
        }
    }

    @Override
    public void onActivityResume(AfActivity activity) {

    }

    @Override@CallSuper
    public void onActivityPause(AfActivity activity) {
        if (activity.isFinishing()) {
            if (mStackActivity.contains(activity)) {
                mStackActivity.remove(activity);
            }
        }
    }

    @Override
    public void onActivityStart(AfActivity activity) {

    }

    @Override
    public void onActivityRestart(AfActivity activity) {

    }

    @Override
    public void onActivityStop(AfActivity activity) {
    }

    @Override
    public void onFragmentCreate(AfFragment fragment) {

    }

    @Override
    public void onFragmentAttach(AfFragment fragment, Context context) {

    }

    @Override
    public void onFragmentDetach(AfFragment fragment) {

    }

    @Override
    public void onFragmentResume(AfFragment fragment) {

    }

    @Override
    public void onFragmentPause(AfFragment fragment) {

    }

    @Override
    public void onFragmentStart(AfFragment fragment) {

    }

    @Override
    public void onFragmentStop(AfFragment fragment) {

    }

    @Override
    public boolean hasActivityRunning() {
        return !mStackActivity.isEmpty();
    }

    @Override
    public boolean hasActivity(Class<? extends AfActivity> clazz) {
        for (Activity activity : mStackActivity) {
            if (activity.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AfActivity currentActivity() {
        System.out.println(this + " currentActivity - size = " + mStackActivity.size());
        if (mStackActivity.isEmpty()) {
            return null;
        }
        return mStackActivity.peek();
    }

    @Override
    public AfActivity getActivity(Class<? extends AfActivity> clazz) {
        for (AfActivity activity : mStackActivity) {
            if (clazz.isAssignableFrom(activity.getClass())) {
                return activity;
            }
        }
        return null;
    }

    @Override
    public AfFragment getFragment(Class<? extends AfFragment> clazz) {
        for (AfActivity activity : mStackActivity) {
            if (activity instanceof AfFragmentActivity) {
                if (((AfFragmentActivity) activity).getFragmentClazz().isAssignableFrom(activity.getClass())) {
                    Fragment fragment = ((AfFragmentActivity) activity).getFragment();
                    if (fragment instanceof AfFragment) {
                        return (AfFragment) fragment;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void finishCurrentActivity() {
        AfActivity activity = currentActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void finishActivity(AfActivity activity) {
        if (activity != null && mStackActivity.contains(activity)) {
            activity.finish();
        }
    }

    @Override
    public void finishAllActivity() {
        for (int i = 0, size = mStackActivity.size(); i < size; i++) {
            if (null != mStackActivity.get(i)) {
                mStackActivity.get(i).finish();
            }
        }
    }

    @Override
    public void startForeground() {
        throw new AfException("如要使用startForeground功能，请自行继承AfPagerManager并实现startForeground");
    }

    @Override
    public void startForeground(Class<? extends AfActivity> clazz) {
        AfActivity lastActivity = null;
        while (mStackActivity.size() > 0) {
            lastActivity = mStackActivity.peek();
            if (clazz.isAssignableFrom(lastActivity.getClass())) {
                return;
            }
            mStackActivity.pop().finish();
        }
        if (lastActivity != null) {
            lastActivity.startActivity(clazz);
        } else {
            startActivity(clazz);
        }
    }

    @Override
    public void startActivity(Class<? extends Activity> clazz, Object... args) {
        AfActivity activity = currentActivity();
        if (activity != null && activity.isRecycled()) {
            activity.startActivity(clazz, args);
        } else {
            AfApp app = AfApp.get();
            AfIntent intent = new AfIntent(app, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putKeyVaules(args);
            app.startActivity(intent);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean startPager(Class clazz, Object... args) {
        if (Fragment.class.isAssignableFrom(clazz)) {
            startFragment(clazz,args);
        } else if (Activity.class.isAssignableFrom(clazz)) {
            startActivity(clazz, args);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        AfActivity activity = currentActivity();
        if (activity != null && activity.isRecycled()) {
            activity.startFragment(clazz, args);
        } else {
            AfFragmentActivity.start(null, clazz, args);
        }
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args) {
        AfActivity activity = currentActivity();
        if (activity instanceof AfFragmentActivity) {
            ((AfFragmentActivity) activity).getFragment().startActivityForResult(new AfIntent(activity,clazz).putKeyVaules(args), request);
        } else if (activity != null && activity.isRecycled()) {
            activity.startActivityForResult(clazz, request, args);
        }
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        AfActivity activity = currentActivity();
        if (activity != null && activity.isRecycled()) {
            activity.startFragment(clazz, request, args);
        }
    }

    //</editor-fold>
}
