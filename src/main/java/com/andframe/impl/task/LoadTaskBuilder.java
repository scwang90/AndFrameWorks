package com.andframe.impl.task;

import com.andframe.api.task.Task;
import com.andframe.api.task.builder.LoadBuilder;
import com.andframe.api.task.handler.LoadSuccessHandler;
import com.andframe.api.task.handler.LoadingHandler;

/**
 * 任务构建器
 * Created by SCWANG on 2017/4/28.
 */

public class LoadTaskBuilder<T> extends TaskBuilder implements LoadBuilder<T> {

    LoadingHandler<T> loadingHandler;
    LoadSuccessHandler<T> loadSuccessHandler;
    Runnable emptyRunnable;

    public LoadTaskBuilder(TaskBuilder builder, Class<T> clazz) {
        this.prepareRunnable = builder.prepareRunnable;
        this.prepareHandler = builder.prepareHandler;
        this.workingHandler = builder.workingHandler;
        this.successRunnable = builder.successRunnable;
        this.exceptionHandler = builder.exceptionHandler;
    }

    @Override
    public LoadBuilder<T> loading(LoadingHandler<T> loadingHandler) {
        this.loadingHandler = loadingHandler;
        return this;
    }

    @Override
    public LoadBuilder<T> loadSuccess(LoadSuccessHandler<T> loadSuccessHandler) {
        this.loadSuccessHandler = loadSuccessHandler;
        return this;
    }

    @Override
    public LoadBuilder<T> loadEmpty(Runnable emptyRunnable) {
        this.emptyRunnable = emptyRunnable;
        return this;
    }

    @Override
    public Task build() {
        return new InternalLoadTask<>(this);
    }
}
