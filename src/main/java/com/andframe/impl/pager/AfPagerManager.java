package com.andframe.impl.pager;

import android.app.Activity;
import android.content.Context;

import com.andframe.activity.AfActivity;
import com.andframe.api.pager.PagerManager;
import com.andframe.application.AfApp;
import com.andframe.exception.AfException;
import com.andframe.fragment.AfFragment;

import java.util.Stack;

/**
 * 页面堆栈管理器
 * Created by SCWANG on 2016/11/29.
 */

public class AfPagerManager implements PagerManager {

    protected static PagerManager instance = null;

    public static PagerManager getInstance() {
        if (instance == null) {
            instance = AfApp.get().newPagerManager();
        }
        return instance;
    }

    public static void activityCreated(AfActivity activity) {
        getInstance().onActivityCreated(activity);
    }

    public static void activityDestroy(AfActivity activity) {
        getInstance().onActivityDestroy(activity);
    }

    public static void activityResume(AfActivity activity) {
        getInstance().onActivityResume(activity);
    }

    public static void activityPause(AfActivity activity) {
        getInstance().onActivityPause(activity);
    }

    public static void fragmentAttach(AfFragment fragment, Context context) {
        getInstance().onFragmentAttach(fragment, context);
    }

    public static void fragmentDetach(AfFragment fragment) {
        getInstance().onFragmentDetach(fragment);
    }

    public static void fragmentResume(AfFragment fragment) {
        getInstance().onFragmentResume(fragment);
    }

    public static void fragmentPause(AfFragment fragment) {
        getInstance().onFragmentPause(fragment);
    }


    //<editor-fold desc="功能实现">
    // 当前主页面
    private Stack<AfActivity> mStackActivity = new Stack<>();

    @Override
    public void onActivityCreated(AfActivity activity) {
        if (!mStackActivity.contains(activity)) {
            mStackActivity.push(activity);
        }
    }

    @Override
    public void onActivityDestroy(AfActivity activity) {
        if (mStackActivity.contains(activity)) {
            mStackActivity.remove(activity);
        }
    }

    @Override
    public void onActivityResume(AfActivity activity) {

    }

    @Override
    public void onActivityPause(AfActivity activity) {

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
    public boolean hasActivityRuning() {
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
        if (mStackActivity.isEmpty()) {
            return null;
        }
        return mStackActivity.peek();
    }

    @Override
    public AfActivity getActivity(Class<? extends AfActivity> clazz) {
        for (AfActivity activity : mStackActivity) {
            if (activity.getClass().equals(clazz)) {
                return activity;
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

    //</editor-fold>
}
