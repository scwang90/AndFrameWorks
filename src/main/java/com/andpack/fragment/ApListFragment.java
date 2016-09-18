package com.andpack.fragment;

import com.andframe.annotation.view.BindViewModule;
import com.andframe.api.page.ListPager;
import com.andframe.api.view.ItemsViewer;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfView;
import com.andframe.fragment.AfListFragment;
import com.andframe.module.AfModuleTitlebar;
import com.andpack.api.ApPager;
import com.andpack.impl.ApListPagerHelper;

/**
 *
 * Created by SCWANG on 2016/9/7.
 */
public abstract class ApListFragment<T> extends AfListFragment<T> implements ApPager {

    @BindViewModule
    protected AfModuleTitlebar mTitlebar;

    protected ApListPagerHelper mHelper = new ApListPagerHelper(this);

    @Override
    protected void onCreated(AfBundle bundle, AfView view) throws Exception {
        mHelper.onCreate();
        super.onCreated(bundle, view);
    }

    @Override
    public void onAfterViews() throws Exception {
        mHelper.onAfterViews(mTitlebar);
        super.onAfterViews();
    }

    @Override
    public ItemsViewer findItemsViewer(ListPager<T> pager) {
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
