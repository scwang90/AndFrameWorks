package com.andpack.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.api.pager.status.RefreshLayoutManager;
import com.andframe.api.pager.status.StatusLayoutManager;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.exception.AfException;
import com.andframe.fragment.AfItemsFragment;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.api.ApItemsPager;
import com.andpack.impl.ApItemsHelper;

import java.util.List;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApItemsFragment<T> extends AfItemsFragment<T> implements ApItemsPager<T> {

    protected ApItemsHelper<T> mApHelper;// = new ApItemsHelper<>(this);

    public ApItemsFragment() {
        this.mApHelper = new ApItemsHelper<>(this);
    }

    public ApItemsFragment(ApItemsHelper<T> helper) {
        this.mApHelper = helper;
    }

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
        throw new AfException("请添加 @BindItemLayout 注解 或 者重写 newItemViewer(int viewType) 方法");
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
