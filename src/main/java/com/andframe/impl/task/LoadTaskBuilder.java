package com.andframe.impl.task;

import com.andframe.api.EmptyDecider;
import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
import com.andframe.api.task.builder.LoadBuilder;
import com.andframe.api.task.builder.WaitLoadBuilder;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.LoadSuccessHandler;
import com.andframe.api.task.handler.LoadingHandler;
import com.andframe.api.task.handler.PrepareHandler;

/**
 * 任务构建器
 * Created by SCWANG on 2017/4/28.
 */
@SuppressWarnings("WeakerAccess")
public class LoadTaskBuilder<T> extends TaskBuilder implements LoadBuilder<T> {

    public LoadingHandler<T> loadingHandler;
    public LoadSuccessHandler<T> loadSuccessHandler;
    public Runnable emptyRunnable;
    public EmptyDecider<T> isEmptyHandler;


    public LoadTaskBuilder(TaskBuilder builder) {
        this(builder, null);
    }

    public LoadTaskBuilder(TaskBuilder builder, LoadingHandler<T> loadingHandler) {
        this.loadingHandler = loadingHandler;
        this.canceledRunnable = builder.canceledRunnable;
        this.prepareRunnable = builder.prepareRunnable;
        this.prepareHandler = builder.prepareHandler;
        this.workingHandler = builder.workingHandler;
        this.finallyRunnable = builder.finallyRunnable;
        this.successRunnable = builder.successRunnable;
        this.exceptionHandler = builder.exceptionHandler;
    }

    //<editor-fold desc="特有接口">

    //<editor-fold desc="设置参数">

    @Override
    public LoadBuilder<T> isEmpty(EmptyDecider<T> isEmptyHandler) {
        this.isEmptyHandler = isEmptyHandler;
        return this;
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
    //</editor-fold>

    //<editor-fold desc="获取参数">

    @Override
    public LoadingHandler<T> loading() {
        return loadingHandler;
    }

    @Override
    public LoadSuccessHandler<T> loadSuccess() {
        return loadSuccessHandler;
    }

    @Override
    public Runnable loadEmpty() {
        return emptyRunnable;
    }
    //</editor-fold>

    //</editor-fold>

    @Override
    public Task build() {
        return new InternalLoadTask<>(this);
    }


    //<editor-fold desc="重写接口">
    public LoadBuilder<T> prepare(Runnable runnable) {
        super.prepare(runnable);
        return this;
    }
    public LoadBuilder<T> prepare(PrepareHandler handler) {
        super.prepare(handler);
        return this;

    }
    public LoadBuilder<T> exception(ExceptionHandler handler) {
        super.exception(handler);
        return this;
    }

    @Override
    public LoadBuilder<T> fina11y(Runnable finallyRunnable) {
        super.fina11y(finallyRunnable);
        return this;
    }

    public WaitLoadBuilder<T> wait(Pager pager, String master) {
        return new WaitLoadTaskBuilder<>(this, pager, master);
    }
    //</editor-fold>T
}
