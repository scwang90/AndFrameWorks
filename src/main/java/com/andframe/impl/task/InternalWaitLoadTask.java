package com.andframe.impl.task;


import com.andframe.R;
import com.andframe.api.pager.Pager;

import java.util.Collection;

/**
 * 集成等待对话框的数据加载任务
 * Created by SCWANG on 2017/5/3.
 */
@SuppressWarnings("WeakerAccess")
public class InternalWaitLoadTask<T> extends InternalWaitTask {

    protected T data;
    protected WaitLoadTaskBuilder<T> builder;

    public InternalWaitLoadTask(WaitLoadTaskBuilder<T> builder) {
        super(builder);
        this.builder = builder;
    }
    @Override
    protected void onWorking() throws Exception {
        super.onWorking();
        if (builder.loadingHandler != null) {
            data = builder.loadingHandler.onLoading();
        }
    }

    @Override
    protected void onHandle() {
        super.onHandle();
        if (success()) {
            if (isEmpty(data)) {
                if (builder.emptyRunnable != null) {
                    builder.emptyRunnable.run();
                }
            } else {
                if (builder.loadSuccessHandler != null) {
                    builder.loadSuccessHandler.onSuccess(data);
                }
            }
        }
    }

    private boolean isEmpty(T data) {
        if (builder.isEmptyHandler != null) {
            return builder.isEmptyHandler.isEmpty(data);
        }
        if (builder.emptyRunnable != null) {
            if (data instanceof Collection) {
                return ((Collection) data).isEmpty();
            }
            return data == null;
        }
        return false;
    }

    @Override
    protected void makeToastSuccess() {
        if (isEmpty(data)) {
            makeToastEmpty();
        } else {
            super.makeToastSuccess();
        }
    }

    private void makeToastEmpty() {
        Pager pager = builder.pager.get();
        if (pager != null && builder.feedbackOnEmpty) {
            pager.makeToastShort(String.format(pager.getContext().getString(R.string.task_format_success),builder.master));
        }
    }
}
