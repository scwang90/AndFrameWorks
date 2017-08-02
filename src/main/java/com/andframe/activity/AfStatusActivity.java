package com.andframe.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.pager.status.Layouter;
import com.andframe.api.pager.status.StatusHelper;
import com.andframe.api.pager.status.StatusLayouter;
import com.andframe.api.pager.status.StatusPager;
import com.andframe.impl.helper.AfStatusHelper;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/20.
 */

public abstract class AfStatusActivity<T> extends AfLoadActivity<T> implements StatusPager<T> {

    protected StatusHelper<T> mStatusHelper = newStatusHelper();

    protected StatusLayouter mStatusLayouter;

    @NonNull
    @Override
    protected StatusHelper<T> newHelper() {
        return mStatusHelper = newStatusHelper();
    }

    @NonNull
    protected StatusHelper<T> newStatusHelper() {
        if (mHelper instanceof StatusHelper) {
            return ((StatusHelper<T>) mHelper);
        }
        return new AfStatusHelper<>(this);
    }

    //<editor-fold desc="初始化布局">

    @Override
    public void initRefreshAndStatusLayouter(View content) {
        mStatusHelper.initRefreshAndStatusLayouter(content);
    }

    @Override
    public void initRefreshAndStatusLayouterOrder(Layouter refresh, Layouter status, View content, ViewGroup parent, int index, ViewGroup.LayoutParams lp) {
        mStatusHelper.initRefreshAndStatusLayouterOrder(refresh, status, content, parent, index, lp);
    }

    public StatusLayouter initStatusLayout(View content) {
        return mStatusLayouter = mStatusHelper.initStatusLayout(content);
    }

    @NonNull
    public StatusLayouter newStatusLayouter(Context context) {
        return mStatusLayouter = mStatusHelper.newStatusLayouter(context);
    }

    //</editor-fold>

    //<editor-fold desc="数据加载">

    @Override
    public boolean isEmpty(T model) {
        return mStatusHelper.isEmpty(model);
    }


    //</editor-fold>

    //<editor-fold desc="页面状态">
    public void showEmpty() {
        mStatusHelper.showEmpty();
    }

    public void showContent() {
        mStatusHelper.showContent();
    }

    public void showProgress() {
        mStatusHelper.showProgress();
    }

    public void showProgress(@NonNull String progress) {
        mStatusHelper.showProgress(progress);
    }
    //</editor-fold>

}
