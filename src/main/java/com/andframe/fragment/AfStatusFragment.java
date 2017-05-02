package com.andframe.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.pager.status.RefreshLayouter;
import com.andframe.api.pager.status.StatusHelper;
import com.andframe.api.pager.status.StatusLayouter;
import com.andframe.api.pager.status.StatusPager;
import com.andframe.api.task.Task;
import com.andframe.impl.helper.AfStatusHelper;

import java.util.Date;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/20.
 */
public abstract class AfStatusFragment<T> extends AfTabFragment implements StatusPager<T> {

    protected StatusHelper<T> mHelper = newHelper();

    protected StatusLayouter mStatusLayouter;
    protected RefreshLayouter mRefreshLayouter;

    @NonNull
    protected StatusHelper<T> newHelper() {
        return new AfStatusHelper<>(this);
    }

    @BindViewCreated@CallSuper
    public void onViewCreated()  {
        mHelper.onViewCreated();
    }

    @Override
    public void setModel(@NonNull T model) {
        mHelper.setModel(model);
    }

    @Override
    public void setLoadTaskOnViewCreated(boolean loadOrNot) {
        mHelper.setLoadTaskOnViewCreated(loadOrNot);
    }

    @Override
    public void setLastRefreshTime(@NonNull Date time) {
        mHelper.setLastRefreshTime(time);
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

    @NonNull
    public StatusLayouter newStatusLayouter(Context context) {
        return mStatusLayouter = mHelper.newStatusLayouter(context);
    }

    @NonNull
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

    public void onTaskFailed(@NonNull Task task) {
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

    public void showError(@NonNull String error) {
        mHelper.showError(error);
    }

    public void showProgress(@NonNull String progress) {
        mHelper.showProgress(progress);
    }
    //</editor-fold>

}
