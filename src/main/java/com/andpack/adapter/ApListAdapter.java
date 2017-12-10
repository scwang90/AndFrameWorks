package com.andpack.adapter;

import android.support.annotation.LayoutRes;

import com.andframe.adapter.AfLayoutItemViewerAdapter;
import com.andframe.api.viewer.ViewQuery;
import com.andpack.api.ApItemBinder;

import java.util.List;

/**
 * ApListAdapter
 * Created by SCWANG on 2016/11/10.
 */

public class ApListAdapter<T> extends AfLayoutItemViewerAdapter<T> {

    protected ApItemBinder<T> binder;

    public ApListAdapter(List<T> ltdata, ApItemBinder<T> binder, @LayoutRes int layoutId) {
        super(layoutId, ltdata);
        this.binder = binder;
    }

    @Override
    protected void onBinding(ViewQuery<? extends ViewQuery> $, T model, int index) {
        binder.onItemBinding($, model, index);
    }
}
