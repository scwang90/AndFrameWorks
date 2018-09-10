package com.andpack.impl;

import com.andframe.adapter.AfLayoutItemViewerAdapter;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.util.java.AfReflecter;
import com.andpack.annotation.BindItemLayout;
import com.andpack.api.ApItemsPager;

import java.util.List;

//import com.twotoasters.jazzylistview.effects.FadeEffect;
//import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
public class ApItemsHelper<T> extends ApStatusHelper {

    private BindItemLayout mItemLayout;
    protected ApItemsPager<T> mItemPager;

    public ApItemsHelper(ApItemsPager<T> pager) {
        super(pager);
        mItemPager = pager;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mItemLayout = AfReflecter.getAnnotation(pager.getClass(), getStopClass(), BindItemLayout.class);
//        mItemPager.initItemEffect();
    }

//    public void initItemEffect() {
//        ViewQuery $ = mItemPager.$(RecyclerView.class);
//        if ($.exist()) {
//            JazzyRecyclerViewScrollListener listener = new JazzyRecyclerViewScrollListener();
//            listener.setTransitionEffect(new FadeEffect());
//            $.foreach(RecyclerView.class, (ViewQuery.ViewIterator<RecyclerView>) view -> view.addOnScrollListener(listener));
//        }
//    }

    public ItemsViewerAdapter<T> newAdapter(List<T> list) {
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
