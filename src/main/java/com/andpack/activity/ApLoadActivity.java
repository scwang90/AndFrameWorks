package com.andpack.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfLoadActivity;
import com.andframe.api.pager.status.RefreshLayoutManager;
import com.andpack.api.ApPager;
import com.andpack.impl.ApLoadHelper;

/**
 * 加载页面支持
 * Created by SCWANG on 2017/5/5.
 */

public abstract class ApLoadActivity<T> extends AfLoadActivity<T> implements ApPager {

    protected ApLoadHelper mApHelper = new ApLoadHelper(this);

    @Override
    public void setTheme(@StyleRes int resId) {
        mApHelper.setTheme(resId);
        super.setTheme(resId);
    }

    @Override
    protected void onCreated(Bundle bundle) {
        mApHelper.onCreate();
        super.onCreated(bundle);
    }

    @Override
    @CallSuper
    public void onViewCreated()  {
        mApHelper.onViewCreated();
        super.onViewCreated();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null)
            return mApHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onPostCreate(Bundle bundle) {
        mApHelper.onPostCreate(bundle);
        super.onPostCreate(bundle);
    }

    @Override
    public void finish() {
        if (mApHelper.finish()) {
            return;
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public View findContentView() {
        View view = mApHelper.findContentView();
        if (view != null) {
            return view;
        }
        return super.findContentView();
    }

    @NonNull
    @Override
    public RefreshLayoutManager newRefreshLayoutManager(Context context) {
        RefreshLayoutManager layoutManager = mApHelper.newRefreshManager(context);
        if (layoutManager != null) {
            return layoutManager;
        }
        return super.newRefreshLayoutManager(context);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean startPager(Class clazz, Object... args) {
        if (Fragment.class.isAssignableFrom(clazz)) {
            ApFragmentActivity.start(this, clazz, args);
        } else {
            return super.startPager(clazz, args);
        }
        return true;
    }

    @Override
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        ApFragmentActivity.start(this, clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        ApFragmentActivity.startResult(this, clazz, request, args);
    }

    @Override
    public void postEvent(Object event) {
        mApHelper.postEvent(event);
    }
}
