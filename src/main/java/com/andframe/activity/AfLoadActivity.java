package com.andframe.activity;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.andframe.api.pager.load.LoadHelper;
import com.andframe.api.pager.load.LoadPager;
import com.andframe.api.pager.status.RefreshLayoutManager;
import com.andframe.api.task.Task;
import com.andframe.impl.helper.AfLoadHelper;

import java.util.Date;

/**
 * 加载页面支持
 * Created by SCWANG on 2017/5/5.
 */

public abstract class AfLoadActivity<T> extends AfActivity implements LoadPager<T> {

    protected LoadHelper<T> mHelper = newHelper();

    protected RefreshLayoutManager mRefreshLayoutManager;

    @NonNull
    protected LoadHelper<T> newHelper() {
        return new AfLoadHelper<>(this);
    }

    @CallSuper
    public void onViewCreated()  {
        super.onViewCreated();
        mHelper.onViewCreated();
    }

    @Override
    public void onDestroyView() {
        mHelper.onDestroyView();
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

    public RefreshLayoutManager initRefreshLayoutManager(View content) {
        return mRefreshLayoutManager = mHelper.initRefreshLayoutManager(content);
    }

    @NonNull
    public RefreshLayoutManager newRefreshLayoutManager(Context context) {
        return mRefreshLayoutManager = mHelper.newRefreshLayoutManager(context);
    }
    //</editor-fold>

    //<editor-fold desc="数据加载">

    public boolean isLoading() {
        return mHelper.isLoading();
    }

    @Override
    public boolean onRefresh() {
        return mHelper.onRefresh();
    }

    @Override
    public void onTaskFinish(@NonNull Task task, T model) {
        mHelper.onTaskFinish(task, model);
    }

    public void onTaskSucceed(T model) {
        mHelper.onTaskSucceed(model);
    }

    public void onTaskFailed(@NonNull Task task) {
        mHelper.onTaskFailed(task);
    }
    //</editor-fold>

    //<editor-fold desc="页面状态">

    public void showError(@NonNull String error) {
        mHelper.showError(error);
    }

    //</editor-fold>

}
