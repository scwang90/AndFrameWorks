package com.andframe.api.page;

import android.content.Context;
import android.view.View;

import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.api.multistatus.StatusLayouter;
import com.andframe.task.AfHandlerTask;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/22.
 */

public interface MultiStatusPager<T> extends Pager, OnRefreshListener {
    View findContentView();

    RefreshLayouter initRefreshLayout(View content);

    StatusLayouter initStatusLayout(View layout);

    void onTaskFinish(T model);

    void showEmpty();

    RefreshLayouter createRefreshLayouter(Context context);

    StatusLayouter createStatusLayouter(Context context);

    void onTaskFailed(AfHandlerTask task);

    void showProgress();

    boolean onTaskLoaded(T model);

    void showContent();

    void showError(String error);

    T onTaskLoading() throws Exception ;
}
