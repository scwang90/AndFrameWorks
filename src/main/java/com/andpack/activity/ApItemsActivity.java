package com.andpack.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfItemsActivity;
import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemViewerAdapter;
import com.andframe.api.pager.status.RefreshLayouter;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andpack.api.ApItemsPager;
import com.andpack.impl.ApItemsHelper;

import java.util.List;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApItemsActivity<T> extends AfItemsActivity<T> implements ApItemsPager<T> {

    protected ApItemsHelper<T> mApHelper = new ApItemsHelper<>(this);

    @Override
    public void setTheme(@StyleRes int resid) {
        mApHelper.setTheme(resid);
        super.setTheme(resid);
    }

    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) {
        mApHelper.onCreate();
        super.onCreate(bundle, intent);
    }

    @Override
    protected void onDestroy() {
        mApHelper.onDestroy();
        super.onDestroy();
    }

    @Override
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
    public ItemViewerAdapter<T> newAdapter(@NonNull Context context, @NonNull List<T> list) {
        ItemViewerAdapter<T> adapter = mApHelper.newAdapter(context,list);
        if (adapter != null) {
            return mAdapter = adapter;
        }
        return super.newAdapter(context, list);
    }

    @NonNull
    @Override
    public RefreshLayouter newRefreshLayouter(Context context) {
        RefreshLayouter layouter = mApHelper.createRefreshLayouter(context);
        if (layouter != null) {
            return layouter;
        }
        return super.newRefreshLayouter(context);
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
    public void startFragment(Class<? extends Fragment> clazz, Object... args) {
        ApFragmentActivity.start(clazz, args);
    }

    @Override
    public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
        ApFragmentActivity.startResult(clazz, request, args);
    }

    @Override
    public void postEvent(Object event) {
        mApHelper.postEvent(event);
    }

    @Override
    public void initItemEffect() {
        mApHelper.initItemEffect();
    }
}
