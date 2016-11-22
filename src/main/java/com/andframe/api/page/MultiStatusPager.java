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
    RefreshLayouter newRefreshLayouter(Context context);
    StatusLayouter newStatusLayouter(Context context);

    void showEmpty();
    void showProgress();
    void showContent();
    void showError(String error);

    void onTaskFinish(T model);
    void onTaskFailed(AfHandlerTask task);

    boolean isEmpty(T model);

    /**
     *
     * 任务加载（异步线程，由框架自动发出执行）
     * @return 加载的数据
     * @throws Exception
     */
    T onTaskLoading() throws Exception;
    /**
     * 任务加载完成
     * @param model 加载的数据
     */
    void onTaskLoaded(T model);

}
