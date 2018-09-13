package com.andframe.impl.task;

import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
import com.andframe.api.task.builder.WaitBuilder;
import com.andframe.api.task.builder.WaitLoadBuilder;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.LoadingHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集成等待对话框的任务 Builder
 * Created by SCWANG on 2017/5/3.
 */
@SuppressWarnings("WeakerAccess")
public class WaitTaskBuilder extends TaskBuilder implements WaitBuilder {

    public String master;
    public WeakReference<Pager> pager;
    public boolean feedbackOnSuccess = true;
    public boolean feedbackOnException = true;

    public WaitTaskBuilder(TaskBuilder builder, Pager pager, String master) {
        this.master = master;
        this.pager = new WeakReference<>(pager);
        this.canceledRunnable = builder.canceledRunnable;
        this.prepareRunnable = builder.prepareRunnable;
        this.prepareHandler = builder.prepareHandler;
        this.workingHandler = builder.workingHandler;
        this.finallyRunnable = builder.finallyRunnable;
        this.successRunnable = builder.successRunnable;
        this.exceptionHandler = builder.exceptionHandler;
    }

    //<editor-fold desc="特有接口">

    @Override
    public String master() {
        return master;
    }

    @Override
    public WaitBuilder success(boolean feedback, Runnable success) {
        super.success(success);
        feedbackOnSuccess = feedback;
        return this;
    }

    @Override
    public WaitBuilder exception(boolean feedback, ExceptionHandler handler) {
        super.exception(handler);
        feedbackOnException = feedback;
        return this;
    }

    @Override
    public boolean feedbackSuccess() {
        return feedbackOnSuccess;
    }

    @Override
    public boolean feedbackException() {
        return feedbackOnException;
    }

    //</editor-fold>

    @Override
    public Task build() {
        return new InternalWaitTask(this);
    }

    //<editor-fold desc="重写接口">
    public WaitBuilder prepare(Runnable runnable) {
        super.prepare(runnable);
        return this;
    }
    public WaitBuilder prepare(PrepareHandler handler) {
        super.prepare(handler);
        return this;
    }
    public WaitBuilder working(WorkingHandler handler) {
        super.working(handler);
        return this;
    }
    public WaitBuilder success(Runnable success) {
        this.success(true, success);
        return this;
    }
    public WaitBuilder exception(ExceptionHandler handler) {
        return exception(true, handler);
    }

    public WaitBuilder fina11y(Runnable finallyRunnable) {
        super.fina11y(finallyRunnable);
        return this;
    }

    @Override
    public <T> WaitLoadBuilder<T> load(LoadingHandler<T> handler) {
        return new WaitLoadTaskBuilder<>(this, handler);
    }

    public <T> WaitLoadBuilder<T> load(Class<T> clazz) {
        return new WaitLoadTaskBuilder<>(this);
    }
    @Override
    public <T> WaitLoadBuilder<Set<T>> loadSet(Class<T> clazz) {
        return new WaitLoadTaskBuilder<>(this);
    }
    @Override
    public <TT> WaitLoadBuilder<List<TT>> loadList(Class<TT> clazz) {
        return new WaitLoadTaskBuilder<>(this);
    }
    @Override
    public <T> WaitLoadBuilder<Collection<T>> loadCollection(Class<T> clazz) {
        return new WaitLoadTaskBuilder<>(this);
    }
    @Override
    public <K,V> WaitLoadBuilder<Map<K,V>> loadMap(Class<K> key, Class<V> value) {
        return new WaitLoadTaskBuilder<>(this);
    }
    //</editor-fold>
}
