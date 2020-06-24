package com.andpack.adapter;

import android.support.annotation.LayoutRes;

import com.andframe.adapter.LayoutItemViewerAdapter;
import com.andframe.api.query.ViewQuery;
import com.andpack.api.ApItemBinder;

import java.util.List;

/**
 * ApListAdapter
 * Created by SCWANG on 2016/11/10.
 */

public class ApItemBinderAdapter<T> extends LayoutItemViewerAdapter<T> {

    protected ApItemBinder<T> binder;

    public ApItemBinderAdapter(List<T> list, ApItemBinder<T> binder, @LayoutRes int layoutId) {
        super(layoutId, list);
        this.binder = binder;
    }

    @Override
    protected void onBinding(T model, int index) {
        binder.onItemBinding($$, model, index);
    }
}
