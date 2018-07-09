package com.andframe.fragment;

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
public abstract class AfStatusFragment<T> extends AfLoadFragment<T> implements StatusPager<T> {

    protected StatusHelper<T> mStatusHelper;// = newStatusHelper();

    protected StatusLayouter mStatusLayouter;

    public AfStatusFragment() {
        super(null);
        this.mStatusHelper = new AfStatusHelper<>(this);
        this.mHelper = mStatusHelper;
    }

    public AfStatusFragment(StatusHelper<T> helper) {
        super(helper);
        this.mStatusHelper = helper;
    }

    //    @NonNull
//    @Override
//    protected StatusHelper<T> newHelper() {
//        return mStatusHelper = newStatusHelper();
//    }
//
//    @NonNull
//    protected StatusHelper<T> newStatusHelper() {
//        if (mHelper instanceof StatusHelper) {
//            return ((StatusHelper<T>) mHelper);
//        }
//        return new AfStatusHelper<>(this);
//    }

    //<editor-fold desc="初始化布局">


    @Override
    public void initRefreshAndStatusLayouter(@NonNull View refreshContent, @NonNull View statusContent) {
        mStatusHelper.initRefreshAndStatusLayouter(refreshContent, statusContent);
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
    @Override
    public void showStatus(StatusLayouter.Status status, String... msg) {
        mStatusHelper.showStatus(status, msg);
    }

    public void showEmpty() {
        mStatusHelper.showEmpty();
    }

    public void showContent() {
        mStatusHelper.showContent();
    }

    public void showProgress() {
        mStatusHelper.showProgress();
    }

    @Override
    public void showInvalidNet() {
        mStatusHelper.showInvalidNet();
    }

    public void showProgress(@NonNull String progress) {
        mStatusHelper.showProgress(progress);
    }
    //</editor-fold>

}
