package com.andframe.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.andframe.$;
import com.andframe.annotation.multistatus.MultiContentViewId;
import com.andframe.annotation.multistatus.MultiStatusLayout;
import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.multistatus.OnRefreshListener;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.api.multistatus.StatusLayouter;
import com.andframe.application.AfApp;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfHandlerDataTask;
import com.andframe.task.AfHandlerTask;
import com.andframe.util.java.AfReflecter;

import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/20.
 */

public class AfMultiStatusFragment<T> extends AfTabFragment implements OnRefreshListener {

    protected StatusLayouter mStatusLayouter;
    protected RefreshLayouter mRefreshLayouter;

    protected T mModel;
    protected boolean mLoadOnViewCreated = true;

    @BindViewCreated@CallSuper
    protected void onViewCreated() throws Exception {
        View content = findContentView();
        if (content != null) {
            mRefreshLayouter = initRefreshLayout(content);
            if (mRefreshLayouter != null) {
                mRefreshLayouter.setOnRefreshListener(this);
                mStatusLayouter = initStatusLayout(mRefreshLayouter.getLayout());
            } else {
                mStatusLayouter = initStatusLayout(content);
            }
            if (mStatusLayouter != null) {
                mStatusLayouter.setOnRefreshListener(this);
            }
        }

        if (mLoadOnViewCreated && mModel == null) {
            mLoadOnViewCreated = false;
            onRefresh();
        } else if (mModel != null) {
            onTaskFinish(mModel);
        } else {
            showEmpty();
        }
    }

    //<editor-fold desc="初始化布局">
    protected View findContentView() {
        MultiContentViewId id = AfReflecter.getAnnotation(getClass(), AfMultiStatusFragment.class, MultiContentViewId.class);
        if (id != null) {
            return findViewById(id.value());
        }

        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mRootView));
        do {
            View view = views.poll();
            if (view != null) {
                if (view instanceof ListView
                        || view instanceof GridView
                        || view instanceof RecyclerView
                        || view instanceof ScrollView) {
                    return view;
                } else if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        } while (!views.isEmpty());

        return null;
    }

    protected RefreshLayouter initRefreshLayout(View content) {
        RefreshLayouter layouter = createRefreshLayouter(content.getContext());
        $.query(content).replace(layouter.getLayout());
        layouter.setContenView(content);
        return layouter;
    }

    protected StatusLayouter initStatusLayout(View content) {
        StatusLayouter layouter = createStatusLayouter(content.getContext());
        $.query(content).replace(layouter.getLayout());
        layouter.setContenView(content);

        MultiStatusLayout status = AfReflecter.getAnnotation(getClass(), AfMultiStatusFragment.class, MultiStatusLayout.class);
        if (status != null) {
            layouter.setProgressLayoutId(status.progress());
            layouter.setEmptyLayoutId(status.empty());
            if (status.error() > 0) {
                layouter.setErrorLayoutId(status.error());
            }
            if (status.invalidnet() > 0) {
                layouter.setInvalidnetLayoutId(status.invalidnet());
            }
        }
        layouter.autoCompletedLayout();
        return layouter;
    }

    protected StatusLayouter createStatusLayouter(Context context) {
        return AfApp.get().newStatusLayouter(context);
    }

    protected RefreshLayouter createRefreshLayouter(Context context) {
        return AfApp.get().newRefreshLayouter(context);
    }
    //</editor-fold>

    //<editor-fold desc="数据加载">

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
                AfDispatcher.dispatch(() -> showProgress());
                return mModel = onTaskLoading();
            }
        })/*.setListener(task -> mRefreshLayouter.setRefreshing(false))*/.prepare();
    }

    protected void onTaskFinish(T data) {
        boolean loaded = onTaskLoaded(data);
        if (loaded) {
            showContent();
        } else {
            showEmpty();
        }
    }

    protected void onTaskFailed(AfHandlerTask task) {
        if (mModel != null) {
            showContent();
            makeToastShort(task.makeErrorToast("加载失败"));
        } else {
            showError(task.makeErrorToast("加载失败"));
        }
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
    public void showEmpty() {
        if (mStatusLayouter != null) {
            mStatusLayouter.showEmpty();
        } else if ((mRefreshLayouter == null || !mRefreshLayouter.isRefreshing())) {
            hideProgressDialog();
        }
    }

    public void showContent() {
        if (mStatusLayouter != null && !mStatusLayouter.isContent()) {
            mStatusLayouter.showContent();
        } else if (mRefreshLayouter != null && mRefreshLayouter.isRefreshing()) {
            mRefreshLayouter.setRefreshing(false);
        } else {
            hideProgressDialog();
        }
    }

    public void showProgress() {
        if ((mRefreshLayouter == null || !mRefreshLayouter.isRefreshing())) {
            if (mStatusLayouter != null) {
                if (!mStatusLayouter.isProgress())
                    mStatusLayouter.showProgress();
            } else {
                showProgressDialog("正在加载...");
            }
        }
    }

    public void showError(String error) {
        if (mRefreshLayouter != null && mRefreshLayouter.isRefreshing()) {
            mRefreshLayouter.setRefreshing(false);
        } else if (mStatusLayouter == null || !mStatusLayouter.isProgress()) {
            hideProgressDialog();
        }
        if (mStatusLayouter != null) {
            mStatusLayouter.showError(error);
        } else {
            makeToastShort(error);
        }
    }
    //</editor-fold>

}
