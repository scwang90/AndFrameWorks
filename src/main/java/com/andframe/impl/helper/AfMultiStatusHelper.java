package com.andframe.impl.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.andframe.$;
import com.andframe.activity.AfMultiStatusActivity;
import com.andframe.annotation.multistatus.MultiContentViewId;
import com.andframe.annotation.multistatus.MultiContentViewType;
import com.andframe.annotation.multistatus.MultiStatusEmpty;
import com.andframe.annotation.multistatus.MultiStatusError;
import com.andframe.annotation.multistatus.MultiStatusInvalidNet;
import com.andframe.annotation.multistatus.MultiStatusLayout;
import com.andframe.annotation.multistatus.MultiStatusProgress;
import com.andframe.api.multistatus.RefreshLayouter;
import com.andframe.api.multistatus.StatusLayouter;
import com.andframe.api.page.MultiStatusHelper;
import com.andframe.api.page.MultiStatusPager;
import com.andframe.application.AfApp;
import com.andframe.fragment.AfMultiStatusFragment;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfHandlerDataTask;
import com.andframe.task.AfHandlerTask;
import com.andframe.util.java.AfReflecter;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 多状态页面支持
 * Created by SCWANG on 2016/10/22.
 */

public class AfMultiStatusHelper<T> implements MultiStatusHelper<T> {

    protected MultiStatusPager<T> mPager;

    protected StatusLayouter mStatusLayouter;
    protected RefreshLayouter mRefreshLayouter;

    protected T mModel;
    protected boolean mLoadOnViewCreated = true;


    public AfMultiStatusHelper(MultiStatusPager<T> pager) {
        this.mPager = pager;
    }

    @Override
    public void setLoadOnViewCreated(boolean loadOrNot) {
        mLoadOnViewCreated = loadOrNot;
    }


    @CallSuper
    public void onViewCreated() throws Exception {
        View content = mPager.findContentView();
        if (content != null) {
            mRefreshLayouter = mPager.initRefreshLayout(content);
            if (mRefreshLayouter != null) {
                mRefreshLayouter.setOnRefreshListener(mPager);
                mStatusLayouter = mPager.initStatusLayout(mRefreshLayouter.getLayout());
            } else {
                mStatusLayouter = mPager.initStatusLayout(content);
            }
            if (mStatusLayouter != null) {
                mStatusLayouter.setOnRefreshListener(mPager);
            }
        }

        if (mLoadOnViewCreated && mModel == null) {
            mLoadOnViewCreated = false;
            mPager.onRefresh();
        } else if (mModel != null) {
            mPager.onTaskFinish(mModel);
        } else {
            mPager.showEmpty();
        }
    }

    //<editor-fold desc="初始化布局">
    public View findContentView() {
        Class<?> stop = mPager instanceof Activity ? AfMultiStatusActivity.class : AfMultiStatusFragment.class;
        MultiContentViewId id = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiContentViewId.class);
        if (id != null) {
            return mPager.findViewById(id.value());
        }
        MultiContentViewType type = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiContentViewType.class);
        if (type != null) {
            return AfApp.get().newViewQuery(mPager).$(type.value()).view();
        }

        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(mPager.getView()));
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

    public RefreshLayouter initRefreshLayout(View content) {
        RefreshLayouter layouter = mPager.createRefreshLayouter(content.getContext());
        $.query(content).replace(layouter.getLayout());
        layouter.setContenView(content);
        return layouter;
    }

    public StatusLayouter initStatusLayout(View content) {
        StatusLayouter layouter = mPager.createStatusLayouter(content.getContext());
        $.query(content).replace(layouter.getLayout());
        layouter.setContenView(content);

        Class<?> stop = mPager instanceof Activity ? AfMultiStatusActivity.class : AfMultiStatusFragment.class;
        MultiStatusLayout status = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusLayout.class);
        if (status != null) {
            layouter.setEmptyLayoutId(status.empty(), status.emptyTxtId());
            layouter.setProgressLayoutId(status.progress(), status.progressTxtId());
            if (status.error() > 0) {
                layouter.setErrorLayoutId(status.error(), status.errorTxtId());
            }
            if (status.invalidNet() > 0) {
                layouter.setInvalidnetLayoutId(status.invalidNet(), status.invalidNetTxtId());
            }
        } else {
            MultiStatusEmpty empty = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusEmpty.class);
            MultiStatusError error = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusError.class);
            MultiStatusProgress progress = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusProgress.class);
            MultiStatusInvalidNet invalidNet = AfReflecter.getAnnotation(mPager.getClass(), stop, MultiStatusInvalidNet.class);
            if (empty != null) {
                layouter.setEmptyLayoutId(empty.value(), empty.txtId(), empty.btnId());
            }
            if (error != null) {
                layouter.setErrorLayoutId(error.value(), error.txtId(), error.btnId());
            }
            if (invalidNet != null) {
                layouter.setInvalidnetLayoutId(invalidNet.value(), invalidNet.txtId(), invalidNet.btnId());
            }
            if (progress != null) {
                layouter.setProgressLayoutId(progress.value(), progress.txtId());
            }
        }
        layouter.autoCompletedLayout();
        return layouter;
    }

    public StatusLayouter createStatusLayouter(Context context) {
        return AfApp.get().newStatusLayouter(context);
    }

    public RefreshLayouter createRefreshLayouter(Context context) {
        return AfApp.get().newRefreshLayouter(context);
    }
    //</editor-fold>

    //<editor-fold desc="数据加载">

    @Override
    public boolean onRefresh() {
        return mPager.postTask(new AfHandlerDataTask<T>() {
            @Override
            protected void onHandle(T data) {
                if (isFinish()) {
                    mPager.onTaskFinish(data);
                } else {
                    mPager.onTaskFailed(this);
                }
            }
            @Override
            protected T onLoadData() throws Exception {
                AfDispatcher.dispatch(() -> mPager.showProgress());
                return mModel = mPager.onTaskLoading();
            }
        })/*.setListener(task -> mRefreshLayouter.setRefreshing(false))*/.prepare();
    }

    @Override
    public boolean isEmpty(T model) {
        if (model instanceof Collection) {
            return ((Collection) model).isEmpty();
        }
        return model == null;
    }

    public void onTaskFinish(T data) {
        if (mPager.isEmpty(data)) {
            mPager.showEmpty();
        } else {
            mPager.showContent();
            mPager.onTaskLoaded(data);
        }
    }

    public void onTaskFailed(AfHandlerTask task) {
        if (mModel != null) {
            mPager.showContent();
            mPager.makeToastShort(task.makeErrorToast("加载失败"));
        } else {
            mPager.showError(task.makeErrorToast("加载失败"));
        }
    }

    /**
     * 任务加载完成
     * @param data 加载的数据
     * @return 数据是否为非空，用于框架自动显示空数据页面
     */
    public boolean onTaskLoaded(T data) {
        return data != null;
    }

//    /**
//     *
//     * 任务加载（异步线程，由框架自动发出执行）
//     * @return 加载的数据
//     * @throws Exception
//     */
//    public T onTaskLoading() throws Exception {
//        Thread.sleep(1000);
//        return null;
//    }
    //</editor-fold>

    //<editor-fold desc="页面状态">
    public void showEmpty() {
        if (mStatusLayouter != null) {
            mStatusLayouter.showEmpty();
        } else if ((mRefreshLayouter == null || !mRefreshLayouter.isRefreshing())) {
            mPager.hideProgressDialog();
        }
    }

    public void showContent() {
        if (mStatusLayouter != null && !mStatusLayouter.isContent()) {
            mStatusLayouter.showContent();
        } else if (mRefreshLayouter != null && mRefreshLayouter.isRefreshing()) {
            mRefreshLayouter.setRefreshing(false);
        } else {
            mPager.hideProgressDialog();
        }
    }

    public void showProgress() {
        if ((mRefreshLayouter == null || !mRefreshLayouter.isRefreshing())) {
            if (mStatusLayouter != null) {
                if (!mStatusLayouter.isProgress())
                    mStatusLayouter.showProgress();
            } else {
                mPager.showProgressDialog("正在加载...");
            }
        }
    }

    public void showError(String error) {
        if (mRefreshLayouter != null && mRefreshLayouter.isRefreshing()) {
            mRefreshLayouter.setRefreshing(false);
        } else if (mStatusLayouter == null || !mStatusLayouter.isProgress()) {
            mPager.hideProgressDialog();
        }
        if (mStatusLayouter != null) {
            mStatusLayouter.showError(error);
        } else {
            mPager.makeToastShort(error);
        }
    }

    @Override
    public void showProgress(String progress) {
        if (mStatusLayouter != null) {
            mStatusLayouter.showProgress(progress);
        } else {
            if (!mPager.isProgressDialogShowing()) {
                mPager.showProgressDialog(progress);
            } else {
                mPager.setProgressDialogText(progress);
            }
        }
    }
    //</editor-fold>

}
