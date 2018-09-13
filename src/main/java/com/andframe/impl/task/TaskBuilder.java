package com.andframe.impl.task;

import com.andframe.$;
import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
import com.andframe.api.task.builder.Builder;
import com.andframe.api.task.builder.LoadBuilder;
import com.andframe.api.task.builder.WaitBuilder;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.LoadingHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务构建器
 * Created by SCWANG on 2017/4/28.
 */
@SuppressWarnings("WeakerAccess")
public class TaskBuilder implements Builder {

    public Runnable prepareRunnable;
    public PrepareHandler prepareHandler;
    public WorkingHandler workingHandler;
    public Runnable successRunnable;
    public Runnable finallyRunnable;
    public Runnable canceledRunnable;
    public ExceptionHandler exceptionHandler;

    //<editor-fold desc="设置参数">
    @Override
    public Builder prepare(Runnable prepareRunnable) {
        this.prepareRunnable = prepareRunnable;
        return this;
    }

    @Override
    public Builder prepare(PrepareHandler prepareHandler) {
        this.prepareHandler = prepareHandler;
        return this;
    }

    @Override
    public Builder working(WorkingHandler workingHandler) {
        this.workingHandler = workingHandler;
        return this;
    }

    @Override
    public Builder success(Runnable successRunnable) {
        this.successRunnable = successRunnable;
        return this;
    }

    @Override
    public Builder fina11y(Runnable finallyRunnable) {
        this.finallyRunnable = finallyRunnable;
        return this;
    }

    @Override
    public Builder canceled(Runnable canceledRunnable) {
        this.canceledRunnable = canceledRunnable;
        return this;
    }

    @Override
    public Builder exception(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="获取参数">
    @Override
    public Runnable prepare() {
        return prepareRunnable;
    }

    @Override
    public Runnable canceled() {
        return canceledRunnable;
    }

    @Override
    public PrepareHandler prepareHandler() {
        return prepareHandler;
    }

    @Override
    public WorkingHandler working() {
        return workingHandler;
    }

    @Override
    public Runnable success() {
        return successRunnable;
    }

    @Override
    public ExceptionHandler exception() {
        return exceptionHandler;
    }
    //</editor-fold>

    @Override
    public WaitBuilder wait(Pager pager, String master) {
        return new WaitTaskBuilder(this, pager, master);
    }

    @Override
    public <T> LoadBuilder<T> load(LoadingHandler<T> handler) {
        return new LoadTaskBuilder<>(this, handler);
    }

    @Override
    public <T> LoadBuilder<T> load(Class<T> clazz) {
        return new LoadTaskBuilder<>(this);
    }

    public <T> LoadBuilder<Set<T>> loadSet(Class<T> clazz) {
        return new LoadTaskBuilder<>(this);
    }

    @Override
    public <T> LoadBuilder<List<T>> loadList(Class<T> clazz) {
        return new LoadTaskBuilder<>(this);
    }

    public <T> LoadBuilder<Collection<T>> loadCollection(Class<T> clazz) {
        return new LoadTaskBuilder<>(this);
    }

    public <K,V> LoadBuilder<Map<K,V>> loadMap(Class<K> key, Class<V> value) {
        return new LoadTaskBuilder<>(this);
    }

    @Override
    public Task build() {
        return new InternalTask(this);
    }

    @Override
    public Task post() {
        return $.task().postTask(build());
    }

    @Override
    public Task post(long delay) {
        return $.task().postTask(build(), delay);
    }
}
