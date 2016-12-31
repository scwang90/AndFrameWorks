package com.andpack.impl;

import android.content.Context;

import com.andframe.adapter.AfLayoutItemViewerAdapter;
import com.andframe.api.adapter.ItemViewerAdapter;
import com.andframe.api.view.ViewQuery;
import com.andframe.util.java.AfReflecter;
import com.andpack.annotation.BindItemLayout;
import com.andpack.api.ApItemsPager;

import java.util.List;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApItemsHelper<T> extends ApStatusHelper {

    private BindItemLayout mItemLayout;
    private ApItemsPager<T> mItemPager;

    public ApItemsHelper(ApItemsPager<T> pager) {
        super(pager);
        mItemPager = pager;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mItemLayout = AfReflecter.getAnnotation(pager.getClass(), getStopClass(), BindItemLayout.class);
    }


    public ItemViewerAdapter<T> newAdapter(Context context, List<T> list) {
        if (mItemLayout != null) {
            return new AfLayoutItemViewerAdapter<T>(mItemLayout.value(), context, list) {
                @Override
                protected void onBinding(ViewQuery<? extends ViewQuery> $, T model, int index) {
                    mItemPager.onItemBinding($, model, index);
                }
            };
        }
        return null;
    }
}
