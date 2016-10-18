package com.andframe.activity;

import android.widget.ScrollView;

import com.andframe.annotation.pager.BindFrameLayout;
import com.andframe.annotation.pager.BindScorllView;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.module.AfFrameSelector;
import com.andframe.module.AfModuleNodata;
import com.andframe.module.AfModuleProgress;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfHandlerDataTask;
import com.andframe.task.AfHandlerTask;
import com.andframe.util.java.AfReflecter;
import com.andframe.widget.AfRefreshScorllView;
import com.andframe.widget.pulltorefresh.AfPullToRefreshBase;

/**
 * 框架 AfFragment
 * @author 树朾
 */
public abstract class AfDetailActivity<T> extends AfActivity implements AfPullToRefreshBase.OnRefreshListener {

    //<editor-fold desc="属性字段">
    protected T mData;

    @BindViewModule
    protected AfModuleNodata mNodata;
    @BindViewModule
    protected AfModuleProgress mProgress;

    protected AfFrameSelector mFrameSelector;
    protected AfRefreshScorllView mRfScorllView;
    protected boolean loadOnAfterViews = true;
    //</editor-fold>

    //<editor-fold desc="初始方法">
    @BindAfterViews
    protected void onAfterViews() throws Exception{
        BindScorllView scorll = AfReflecter.getAnnotation(getClass(), AfDetailActivity.class, BindScorllView.class);
        if (scorll != null) {
            ScrollView scrollView = findViewById(scorll.value(), ScrollView.class);
            if (scrollView != null) {
                mRfScorllView = new AfRefreshScorllView(this, scorll.value());
                mRfScorllView.setOnRefreshListener(this);
            }
        }

        BindFrameLayout frame = AfReflecter.getAnnotation(getClass(), AfDetailActivity.class, BindFrameLayout.class);
        if (frame != null) {
            mFrameSelector = new AfFrameSelector(this, frame.value());
            mNodata.setOnRefreshListener(view -> onRefresh());
        }

        if (loadOnAfterViews && mData == null) {
            loadOnAfterViews = false;
            onRefresh();
        } else if (mData != null) {
            onTaskFinish(mData);
        } else {
            showNoData();
        }
    }
    //</editor-fold>

    //<editor-fold desc="数据加载">
    @Override
    public boolean onMore() {
        return false;
    }

    @Override
    public boolean onRefresh() {
        return postTask(new AfHandlerDataTask<T>() {
            @Override
            protected void onHandle(T data) {
                if (isFinish()) {
                    onTaskFinish(data);
                } else {
                    onTaskFailed(this);
                }
            }
            @Override
            protected T onLoadData() throws Exception {
                AfDispatcher.dispatch(() -> showLoading());
                return mData = onTaskLoading();
            }
        }).setListener(mRfScorllView).prepare();
    }

    protected void onTaskFinish(T data) {
        boolean loaded = onTaskLoaded(data);
        if (loaded) {
            showData();
        } else {
            showNoData();
        }
    }

    protected void onTaskFailed(AfHandlerTask task) {
        showError(task.makeErrorToast("加载失败"));
    }

    /**
     * 任务加载完成
     * @param data 加载的数据
     * @return 数据是否为非空，用于框架自动显示空数据页面
     */
    protected boolean onTaskLoaded(T data) {
        return data != null;
    }

    /**
     *
     * 任务加载（异步线程，由框架自动发出执行）
     * @return 加载的数据
     * @throws Exception
     */
    protected T onTaskLoading() throws Exception {
        Thread.sleep(1000);
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="页面状态">
    protected void showNoData() {
        if (mFrameSelector != null) {
            mFrameSelector.selectFrame(mNodata);
        } else if ((mRfScorllView == null || !mRfScorllView.isRefreshing())) {
            hideProgressDialog();
        }
    }

    protected void showData() {
        if (mFrameSelector != null) {
            mFrameSelector.selectFrame(mRfScorllView);
        } else if ((mRfScorllView == null || !mRfScorllView.isRefreshing())) {
            hideProgressDialog();
        }
    }

    protected void showLoading() {
        if ((mRfScorllView == null || !mRfScorllView.isRefreshing())) {
            if (mFrameSelector != null) {
                mFrameSelector.selectFrame(mProgress);
            } else {
                showProgressDialog("正在加载...");
            }
        }
    }

    protected void showError(String error) {
        hideProgressDialog();
        if (mFrameSelector != null) {
            mNodata.setDescription(error);
            mFrameSelector.selectFrame(mNodata);
        } else {
            makeToastShort(error);
        }
    }
    //</editor-fold>

}
