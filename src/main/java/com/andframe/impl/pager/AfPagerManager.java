package com.andframe.impl.pager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.andframe.api.pager.PagerManager;
import com.andframe.application.AfApp;

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

    public static void activityCreated(Activity activity) {
        getInstance().onActivityCreated(activity);
    }

    public static void activityDestroy(Activity activity) {
        getInstance().onActivityDestroy(activity);
    }

    public static void activityResume(Activity activity) {
        getInstance().onActivityResume(activity);
    }

    public static void activityPause(Activity activity) {
        getInstance().onActivityPause(activity);
    }

    public static void fragmentAttach(Fragment fragment, Context context) {
        getInstance().onFragmentAttach(fragment, context);
    }

    public static void fragmentDetach(Fragment fragment) {
        getInstance().onFragmentDetach(fragment);
    }

    public static void fragmentResume(Fragment fragment) {
        getInstance().onFragmentResume(fragment);
    }

    public static void fragmentPause(Fragment fragment) {
        getInstance().onFragmentPause(fragment);
    }


    //<editor-fold desc="功能实现">
    // 当前主页面
    private Stack<Activity> mStackActivity = new Stack<>();

    @Override
    public void onActivityCreated(Activity activity) {
        if (!mStackActivity.contains(activity)) {
            mStackActivity.push(activity);
        }
    }

    @Override
    public void onActivityDestroy(Activity activity) {
        if (mStackActivity.contains(activity)) {
            mStackActivity.remove(activity);
        }
    }

    @Override
    public void onActivityResume(Activity activity) {

    }

    @Override
    public void onActivityPause(Activity activity) {

    }

    @Override
    public void onFragmentAttach(Fragment fragment, Context context) {

    }

    @Override
    public void onFragmentDetach(Fragment fragment) {

    }

    @Override
    public void onFragmentResume(Fragment fragment) {

    }

    @Override
    public void onFragmentPause(Fragment fragment) {

    }

    @Override
    public boolean hasActivityRuning() {
        return !mStackActivity.isEmpty();
    }

    @Override
    public boolean hasActivity(Class<? extends Activity> clazz) {
        for (Activity activity : mStackActivity) {
            if (activity.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Activity currentActivity() {
        if (mStackActivity.isEmpty()) {
            return null;
        }
        return mStackActivity.peek();
    }

    @Override
    public Activity getActivity(Class<? extends Activity> clazz) {
        for (Activity activity : mStackActivity) {
            if (activity.getClass().equals(clazz)) {
                return activity;
            }
        }
        return null;
    }

    @Override
    public void finishCurrentActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void finishActivity(Activity activity) {
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
    //</editor-fold>
}
