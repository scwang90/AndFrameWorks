package com.andpack.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.api.pager.status.RefreshLayoutManager;
import com.andframe.fragment.AfLoadFragment;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.api.ApPager;
import com.andpack.impl.ApLoadHelper;

/**
 * 加载页面支持
 * Created by SCWANG on 2017/5/5.
 */

public abstract class ApLoadFragment<T> extends AfLoadFragment<T> implements ApPager {

    protected ApLoadHelper mApHelper = new ApLoadHelper(this);

    @Override
    protected void onCreated() {
        mApHelper.onCreate();
        super.onCreated();
    }

    @Override
    public void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        mApHelper.onDestroyView();
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onViewCreated()  {
        mApHelper.onViewCreated();
        super.onViewCreated();
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
    public T onTaskLoading() throws Exception {
        return null;
    }

    @Override
    public void onTaskLoaded(@NonNull T model) {

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
