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
        if (mFrameSelector != null) {
            mNodata.setDescription(task.makeErrorToast("加载失败"));
            mFrameSelector.selectFrame(mNodata);
        } else {
            makeToastShort(task.makeErrorToast("加载失败"));
        }
    }

    protected void onTaskLoaded(T data) {
        if (mFrameSelector != null) {
            mFrameSelector.selectFrame(mRfScorllView);
        }
    }

    protected T onTaskLoading() throws Exception {
        Thread.sleep(1000);
        return null;
    }
}
