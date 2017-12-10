package com.andpack.impl;

import android.support.v7.widget.RecyclerView;

import com.andframe.adapter.AfLayoutItemViewerAdapter;
import com.andframe.api.adapter.ItemViewerAdapter;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.util.java.AfReflecter;
import com.andpack.annotation.BindItemLayout;
import com.andpack.api.ApItemsPager;
import com.twotoasters.jazzylistview.effects.SlideInEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

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
        mItemPager.initItemEffect();
    }

    public void initItemEffect() {
        ViewQuery $ = mItemPager.$(RecyclerView.class);
        if ($.exist()) {
            JazzyRecyclerViewScrollListener listener = new JazzyRecyclerViewScrollListener();
            listener.setTransitionEffect(new SlideInEffect());
            $.foreach(RecyclerView.class, (ViewQuery.ViewEacher<RecyclerView>) view -> view.addOnScrollListener(listener));
        }
    }

    public ItemViewerAdapter<T> newAdapter(List<T> list) {
        if (mItemLayout != null) {
            return new AfLayoutItemViewerAdapter<T>(mItemLayout.value(), list) {
                @Override
                protected void onBinding(ViewQuery<? extends ViewQuery> $, T model, int index) {
                    mItemPager.onItemBinding($, model, index);
                }
            };
        }
        return null;
    }
}
