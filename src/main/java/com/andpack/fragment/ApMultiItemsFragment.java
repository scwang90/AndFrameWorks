package com.andpack.fragment;

import com.andframe.api.page.ItemsPager;
import com.andframe.api.view.ItemsViewer;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfMultiItemsFragment;
import com.andpack.api.ApPager;
import com.andpack.impl.ApListPagerHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApMultiItemsFragment<T> extends AfMultiItemsFragment<T> implements ApPager {

    protected ApListPagerHelper mHelper = new ApListPagerHelper(this);

    @Override
    protected void onCreated(AfBundle bundle, AfView view) throws Exception {
        mHelper.onCreate();
        super.onCreated(bundle, view);
    }

    @Override
    public void onViewCreated() throws Exception {
        mHelper.onViewCreated(null);
        super.onViewCreated();
    }

    @Override
    public ItemsViewer findItemsViewer(ItemsPager<T> pager) {
        return mHelper.findItemsViewer(pager);
    }

    //<editor-fold desc="下拉刷新">
    @Override
    public boolean onMore() {
        return false;
    }

    @Override
    public boolean onRefresh() {
        return false;
    }
    //</editor-fold>
}
