package com.andpack.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.andframe.api.adapter.ItemViewer;
import com.andframe.api.adapter.ItemViewerAdapter;
import com.andframe.api.pager.status.RefreshLayouter;
import com.andframe.api.pager.status.StatusLayouter;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfBundle;
import com.andframe.impl.viewer.AfView;
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

    protected ApItemsHelper<T> mApHelper = new ApItemsHelper<>(this);

    @Override
    protected void onCreate(AfBundle bundle, AfView view) throws Exception {
        mApHelper.onCreate();
        super.onCreate(bundle, view);
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
    public void onViewCreated()  {
        mApHelper.onViewCreated();
        super.onViewCreated();
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
        RefreshLayouter layouter = mApHelper.newRefreshLayouter(context);
        if (layouter != null) {
            return layouter;
        }
        return super.newRefreshLayouter(context);
    }

    @NonNull
    @Override
    public StatusLayouter newStatusLayouter(Context context) {
        StatusLayouter layouter = mApHelper.newStatusLayouter(context);
        if (layouter != null) {
            return layouter;
        }
        return super.newStatusLayouter(context);
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

    @Override
    public void initItemEffect() {
        mApHelper.initItemEffect();
    }
}
