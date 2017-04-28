package com.andframe.impl.task;

import java.util.Collection;

/**
 * 内部加载任务实现
 * Created by SCWANG on 2017/4/28.
 */

class InternalLoadTask<T> extends InternalTask {

    private T data;
    private LoadTaskBuilder<T> builder;

    InternalLoadTask(LoadTaskBuilder<T> builder) {
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
        if (isFinish()) {
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
        if (data instanceof Collection) {
            return ((Collection) data).isEmpty();
        }
        return data != null;
    }
}
