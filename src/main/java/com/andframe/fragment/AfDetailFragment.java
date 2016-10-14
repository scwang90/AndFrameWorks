package com.andframe.fragment;

import android.widget.ScrollView;

import com.andframe.annotation.pager.BindFrameLayout;
import com.andframe.annotation.pager.BindScorllView;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.annotation.view.BindViewModule;
import com.andframe.module.AfFrameSelector;
import com.andframe.module.AfModuleNodata;
import com.andframe.module.AfModuleProgress;
import com.andframe.task.AfHandlerTask;
import com.andframe.util.java.AfReflecter;
import com.andframe.widget.AfRefreshScorllView;
import com.andframe.widget.pulltorefresh.AfPullToRefreshBase;

/**
 * 框架 AfFragment
 * @author 树朾
 */
public abstract class AfDetailFragment<T> extends AfTabFragment implements AfPullToRefreshBase.OnRefreshListener {

    @BindViewModule
    protected AfModuleNodata mNodata;
    @BindViewModule
    protected AfModuleProgress mProgress;

    protected AfFrameSelector mFrameSelector;
    protected AfRefreshScorllView mRfScorllView;
    protected boolean loadOnAfterViews = true;

    @BindAfterViews
    protected void onAfterViews() throws Exception{
        BindScorllView scorll = AfReflecter.getAnnotation(getClass(), AfDetailFragment.class, BindScorllView.class);
        if (scorll != null) {
            ScrollView scrollView = findViewById(scorll.value(), ScrollView.class);
            if (scrollView != null) {
                mRfScorllView = new AfRefreshScorllView(this, scorll.value());
                mRfScorllView.setOnRefreshListener(this);
            }
        }

        BindFrameLayout frame = AfReflecter.getAnnotation(getClass(), AfDetailFragment.class, BindFrameLayout.class);
        if (frame != null) {
            mFrameSelector = new AfFrameSelector(this, frame.value());
            mNodata.setOnRefreshListener(view -> onRefresh());
        }

        if (loadOnAfterViews) {
            onRefresh();
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
            protected boolean onPrepare() {
                if (mFrameSelector != null) {
                    mFrameSelector.selectFrame(mProgress);
                }
                return super.onPrepare();
            }

            @Override
            protected void onHandle() {
                if (isFinish()) {
                    boolean loaded = onTaskLoaded(data);
                    if (mFrameSelector != null) {
                        if (loaded) {
                            mFrameSelector.selectFrame(mRfScorllView);
                        } else {
                            mFrameSelector.selectFrame(mNodata);
                        }
                    }
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
        if (mFrameSelector != null) {
            mNodata.setDescription(task.makeErrorToast("加载失败"));
            mFrameSelector.selectFrame(mNodata);
        } else {
            makeToastShort(task.makeErrorToast("加载失败"));
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
}
