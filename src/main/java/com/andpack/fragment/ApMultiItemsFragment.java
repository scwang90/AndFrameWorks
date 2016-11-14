package com.andpack.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.andframe.api.adapter.ListItem;
import com.andframe.api.adapter.ListItemAdapter;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.api.view.ViewQuery;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfMultiItemsFragment;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.api.ApItemsPager;
import com.andpack.impl.ApItemsHelper;

import java.util.List;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApMultiItemsFragment<T> extends AfMultiItemsFragment<T> implements ApItemsPager<T> {

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
    protected void onViewCreated() throws Exception {
        mApHelper.onViewCreated();
        super.onViewCreated();
    }


    @Override
    public ListItemAdapter<T> newAdapter(Context context, List<T> list) {
        ListItemAdapter<T> adapter = mApHelper.newAdapter(context,list);
        if (adapter != null) {
            return mAdapter = adapter;
        }
        return super.newAdapter(context, list);
    }

    @Override
    public RefreshLayouter newRefreshLayouter(Context context) {
        RefreshLayouter layouter = mApHelper.createRefreshLayouter(context);
        if (layouter != null) {
            return layouter;
        }
        return super.newRefreshLayouter(context);
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
    public ListItem<T> newListItem() {
        throw new AfToastException("请重写 newListItem() 方法");
    }

    @Override
    public void onItemBinding(ViewQuery<? extends ViewQuery> $, T model, int index) {

    }
}
