package com.andframe.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.api.pager.status.LayoutManager;
import com.andframe.api.pager.status.StatusHelper;
import com.andframe.api.pager.status.StatusLayoutManager;
import com.andframe.api.pager.status.StatusPager;
import com.andframe.impl.helper.AfStatusHelper;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/20.
 */
public abstract class AfStatusFragment<T> extends AfLoadFragment<T> implements StatusPager<T> {

    protected StatusHelper<T> mStatusHelper;// = newStatusHelper();

    protected StatusLayoutManager mStatusLayoutManager;

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
    public void initRefreshAndStatusManager(@NonNull View refreshContent, @NonNull View statusContent) {
        mStatusHelper.initRefreshAndStatusManager(refreshContent, statusContent);
    }

    @Override
    public void initRefreshAndStatusManagerOrder(LayoutManager refresh, LayoutManager status, View content, ViewGroup parent, int index, ViewGroup.LayoutParams lp) {
        mStatusHelper.initRefreshAndStatusManagerOrder(refresh, status, content, parent, index, lp);
    }

    public StatusLayoutManager initStatusLayoutManager(View content) {
        return mStatusLayoutManager = mStatusHelper.initStatusLayoutManager(content);
    }

    @NonNull
    public StatusLayoutManager newStatusLayoutManager(Context context) {
        return mStatusLayoutManager = mStatusHelper.newStatusLayoutManager(context);
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
    public void showStatus(StatusLayoutManager.Status status, String... msg) {
        mStatusHelper.showStatus(status, msg);
    }

    @Override
    public void showEmpty() {
        mStatusHelper.showEmpty();
    }

    @Override
    public void showEmpty(@NonNull String message) {
        mStatusHelper.showEmpty(message);
    }

    @Override
    public void showContent() {
        mStatusHelper.showContent();
    }

    @Override
    public void showContent(@NonNull T model) {
        mStatusHelper.showContent(model);
    }

    @Override
    public void showProgress() {
        mStatusHelper.showProgress();
    }

    @Override
    public void showInvalidNet() {
        mStatusHelper.showInvalidNet();
    }

    @Override
    public void showProgress(@NonNull String progress) {
        mStatusHelper.showProgress(progress);
    }
    //</editor-fold>

}
