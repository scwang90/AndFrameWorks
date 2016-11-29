package com.andframe.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.api.multistatus.StatusLayouter;
import com.andframe.api.pager.MultiStatusHelper;
import com.andframe.api.pager.MultiStatusPager;
import com.andframe.impl.helper.AfMultiStatusHelper;
import com.andframe.task.AfHandlerTask;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/20.
 */
public abstract class AfMultiStatusFragment<T> extends AfTabFragment implements MultiStatusPager<T> {

    protected MultiStatusHelper<T> mHelper = newHelper();

    protected StatusLayouter mStatusLayouter;
    protected RefreshLayouter mRefreshLayouter;

    @NonNull
    protected MultiStatusHelper<T> newHelper() {
        return new AfMultiStatusHelper<>(this);
    }

    @BindViewCreated@CallSuper
    protected void onViewCreated() throws Exception {
        mHelper.onViewCreated();
    }

    //<editor-fold desc="初始化布局">
    public View findContentView() {
        return mHelper.findContentView();
    }

    public RefreshLayouter initRefreshLayout(View content) {
        return mRefreshLayouter = mHelper.initRefreshLayout(content);
    }

    public StatusLayouter initStatusLayout(View content) {
        return mStatusLayouter = mHelper.initStatusLayout(content);
    }

    public StatusLayouter newStatusLayouter(Context context) {
        return mStatusLayouter = mHelper.newStatusLayouter(context);
    }

    public RefreshLayouter newRefreshLayouter(Context context) {
        return mRefreshLayouter = mHelper.newRefreshLayouter(context);
    }
    //</editor-fold>

    //<editor-fold desc="数据加载">

    public boolean isLoading() {
        return mHelper.isLoading();
    }

    @Override
    public boolean isEmpty(T model) {
        return mHelper.isEmpty(model);
    }

    @Override
    public boolean onRefresh() {
        return mHelper.onRefresh();
    }

    public void onTaskFinish(T data) {
        mHelper.onTaskFinish(data);
    }

    public void onTaskFailed(AfHandlerTask task) {
        mHelper.onTaskFailed(task);
    }

//    /**
//     * 任务加载完成
//     * @param data 加载的数据
//     * @return 数据是否为非空，用于框架自动显示空数据页面
//     */
//    public boolean onTaskLoaded(T data) {
//        return mHelper.onTaskLoaded(data);
//    }

//    /**
//     *
//     * 任务加载（异步线程，由框架自动发出执行）
//     * @return 加载的数据
//     * @throws Exception
//     */
//    public T onTaskLoading() throws Exception {
//        return mHelper.onTaskLoading();
//    }
    //</editor-fold>

    //<editor-fold desc="页面状态">
    public void showEmpty() {
        mHelper.showEmpty();
    }

    public void showContent() {
        mHelper.showContent();
    }

    public void showProgress() {
        mHelper.showProgress();
    }

    public void showError(String error) {
        mHelper.showError(error);
    }

    public void showProgress(String progress) {
        mHelper.showProgress(progress);
    }
    //</editor-fold>

}
