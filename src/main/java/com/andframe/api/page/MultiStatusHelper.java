package com.andframe.api.page;

import android.content.Context;
import android.view.View;

import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.api.multistatus.StatusLayouter;
import com.andframe.task.AfHandlerTask;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/22.
 */

public interface MultiStatusHelper<T> {

    void onViewCreated() throws Exception;

    View findContentView();

    RefreshLayouter initRefreshLayout(View content);
    StatusLayouter initStatusLayout(View content);
    StatusLayouter createStatusLayouter(Context context);
    RefreshLayouter createRefreshLayouter(Context context);

    boolean onRefresh();
    boolean isEmpty(T model);
    void onTaskFinish(T data);
    void onTaskFailed(AfHandlerTask task);

    void showEmpty();
    void showContent();
    void showProgress();
    void showError(String error);
    void showProgress(String progress);

    void setLoadOnViewCreated(boolean loadOrNot);

}
