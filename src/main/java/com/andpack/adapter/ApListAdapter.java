package com.andpack.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.andframe.adapter.AfListLayoutItemAdapter;
import com.andframe.api.view.ViewQuery;
import com.andpack.api.ApItemBinder;

import java.util.List;

/**
 * ApListAdapter
 * Created by SCWANG on 2016/11/10.
 */

public class ApListAdapter<T> extends AfListLayoutItemAdapter<T> {

    protected ApItemBinder<T> binder;

    public ApListAdapter(Context context, List<T> ltdata, ApItemBinder<T> binder, @LayoutRes int layoutId) {
        super(layoutId, context, ltdata);
        this.binder = binder;
    }

    @Override
    protected void onBinding(ViewQuery<? extends ViewQuery> $, T model, int index) {
        binder.onItemBinding($, model, index);
    }
}
