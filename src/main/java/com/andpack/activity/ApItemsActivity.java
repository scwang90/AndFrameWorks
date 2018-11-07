package com.andpack.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfItemsActivity;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.api.pager.status.RefreshLayoutManager;
import com.andframe.api.pager.status.StatusLayoutManager;
import com.andframe.api.query.ViewQuery;
import com.andframe.exception.AfToastException;
import com.andpack.api.ApItemsPager;
import com.andpack.impl.ApItemsHelper;

import java.util.List;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApItemsActivity<T> extends AfItemsActivity<T> implements ApItemsPager<T> {

    protected ApItemsHelper<T> mApHelper;// = new ApItemsHelper<>(this);

    public ApItemsActivity() {
        this.mApHelper = new ApItemsHelper<>(this);
    }

    public ApItemsActivity(ApItemsHelper<T> helper) {
        this.mApHelper = helper;
    }

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
    protected void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
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
        super.onPostCreate(bundle);
        mApHelper.onPostCreate(bundle);
    }

    @Override
    public void finish() {
        if (mApHelper.finish()) {
            return;
        }
        super.finish();
    }

    @NonNull
    @Override
    public ItemsViewerAdapter<T> newAdapter(@NonNull Context context, @NonNull List<T> list) {
        ItemsViewerAdapter<T> adapter = mApHelper.newAdapter(list);
        if (adapter != null) {
            return mAdapter = adapter;
        }
        return super.newAdapter(context, list);
    }

    @NonNull
    @Override
    public RefreshLayoutManager newRefreshLayoutManager(Context context) {
        RefreshLayoutManager layoutManager = mApHelper.newRefreshManager(context);
        if (layoutManager != null) {
            mRefreshLayoutManager = layoutManager;
            return layoutManager;
        }
        return super.newRefreshLayoutManager(context);
    }

    @NonNull
    @Override
    public StatusLayoutManager newStatusLayoutManager(Context context) {
        StatusLayoutManager layoutManager = mApHelper.newStatusManager(context);
        if (layoutManager != null) {
            return layoutManager;
        }
        return super.newStatusLayoutManager(context);
    }

    @NonNull
    @Override
    public ItemViewer<T> newItemViewer(int viewType) {
        throw new AfToastException("请重写 newItemViewer(int viewType) 方法");
    }

    @Override
    public void onItemBinding(ViewQuery<? extends ViewQuery> $, T model, int index) {

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

//    @Override
//    public void initItemEffect() {
//        mApHelper.initItemEffect();
//    }
}
