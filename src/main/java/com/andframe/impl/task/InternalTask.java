package com.andframe.impl.task;

import com.andframe.task.AfHandlerTask;

/**
 * 内部任务实现
 * Created by SCWANG on 2017/4/28.
 */

@SuppressWarnings("WeakerAccess")
public class InternalTask extends AfHandlerTask {

    protected TaskBuilder builder;

    public InternalTask(TaskBuilder builder) {
        this.builder = builder;
    }

    @Override
    protected boolean onPrepare() {
        if (builder.prepareHandler != null) {
            return builder.prepareHandler.onTaskPrepare();
        } else if (builder.prepareRunnable != null) {
            builder.prepareRunnable.run();
        }
        return super.onPrepare();
    }

    @Override
    protected void onCancel() {
        if (builder.canceledRunnable != null) {
            builder.canceledRunnable.run();
        }
        super.onCancel();
    }

    @Override
    protected void onHandle() {
        if (builder.finallyRunnable != null) {
            builder.finallyRunnable.run();
        }
        if (success()) {
            if (builder.successRunnable != null) {
                builder.successRunnable.run();
            }
        } else if (mException != null) {
            if (builder.exceptionHandler != null) {
                builder.exceptionHandler.onTaskException(mException);
            }
        }
    }

    @Override
    protected void onWorking() throws Exception {
        if (builder.workingHandler != null) {
            builder.workingHandler.onWorking();
        }
    }
}
