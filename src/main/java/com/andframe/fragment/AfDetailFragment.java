package com.andframe.fragment;

import android.widget.ScrollView;

import com.andframe.annotation.pager.BindScorllView;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.task.AfHandlerTask;
import com.andframe.widget.AfRefreshScorllView;
import com.andframe.widget.pulltorefresh.AfPullToRefreshBase;

/**
 * 框架 AfFragment
 * @author 树朾
 */
public abstract class AfDetailFragment<T> extends AfFragment implements AfPullToRefreshBase.OnRefreshListener {

    protected AfRefreshScorllView mRfScorllView;

    @BindAfterViews
    protected void onAfterViews() {
        if (this.getClass().isAnnotationPresent(BindScorllView.class)) {
            BindScorllView bind = this.getClass().getAnnotation(BindScorllView.class);
            ScrollView scrollView = findViewById(bind.value(), ScrollView.class);
            if (scrollView != null) {
                mRfScorllView = new AfRefreshScorllView(this,bind.value());
                mRfScorllView.setOnRefreshListener(this);
            }
        }
    }

    @Override
    public boolean onMore() {
        return false;
    }

    @Override
    public boolean onRefresh() {
        return postTask(new AfHandlerTask() {
            T data;
            @Override
            protected void onHandle() {
                if (isFinish()) {
                    onTaskLoaded(data);
                } else {
                    onTaskFailed(this);
                }
            }

            @Override
            protected void onWorking() throws Exception {
                data = onTaskLoading();
            }
        }).setListener(mRfScorllView).prepare();
    }

    protected void onTaskFailed(AfHandlerTask task) {
        makeToastShort(task.makeErrorToast("加载失败"));
    }

    protected void onTaskLoaded(T data) {

    }

    protected T onTaskLoading() throws Exception {
        Thread.sleep(1000);
        return null;
    }
}
