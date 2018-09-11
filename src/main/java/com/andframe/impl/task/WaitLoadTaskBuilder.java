package com.andframe.impl.task;

import com.andframe.api.EmptyDecider;
import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
import com.andframe.api.task.builder.WaitLoadBuilder;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.LoadSuccessHandler;
import com.andframe.api.task.handler.LoadingHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集成等待对话框的数据加载任务 Builder
 * Created by SCWANG on 2017/5/3.
 */
@SuppressWarnings("WeakerAccess")
public class WaitLoadTaskBuilder<T> extends WaitTaskBuilder implements WaitLoadBuilder<T> {

    public Runnable emptyRunnable;
    public LoadingHandler<T> loadingHandler;
    public LoadSuccessHandler<T> loadSuccessHandler;
    public EmptyDecider<T> isEmptyHandler;
    public boolean feedbackOnEmpty = true;

    public WaitLoadTaskBuilder(WaitTaskBuilder builder) {
        this(builder, null);
    }

    public WaitLoadTaskBuilder(WaitTaskBuilder builder, LoadingHandler<T> loadingHandler) {
        super(builder, builder.pager.get(), builder.master);
        this.pager = builder.pager;
        this.master = builder.master;
        this.loadingHandler = loadingHandler;
        this.feedbackOnSuccess = builder.feedbackOnSuccess;
        this.feedbackOnException = builder.feedbackOnException;
    }

    public WaitLoadTaskBuilder(LoadTaskBuilder<T> builder, Pager pager, String master) {
        super(builder, pager, master);
        this.loadingHandler = builder.loadingHandler;
        this.loadSuccessHandler = builder.loadSuccessHandler;
        this.emptyRunnable = builder.emptyRunnable;
    }

    @Override
    public Task build() {
        return new InternalWaitLoadTask<>(this);
    }

    //<editor-fold desc="特有接口">

    //<editor-fold desc="设置参数">

    @Override
    public WaitLoadBuilder<T> isEmpty(EmptyDecider<T> handler) {
        isEmptyHandler = handler;
        return this;
    }

    @Override
    public WaitLoadBuilder<T> loading(LoadingHandler<T> handler) {
        loadingHandler = handler;
        return this;
    }

    @Override
    public WaitLoadTaskBuilder<T> success(boolean feedback, Runnable success) {
        super.success(success);
        feedbackOnSuccess = feedback;
        return this;
    }

    @Override
    public WaitLoadTaskBuilder<T> exception(boolean feedback, ExceptionHandler handler) {
        super.exception(feedback, handler);
        feedbackOnException = feedback;
        return this;
    }

    @Override
    public WaitLoadTaskBuilder<T> loadEmpty(boolean feedback, Runnable runnable) {
        emptyRunnable = runnable;
        feedbackOnEmpty = feedback;
        return this;
    }

    @Override
    public WaitLoadTaskBuilder<T> loadSuccess(boolean feedback, LoadSuccessHandler<T> handler) {
        loadSuccessHandler = handler;
        feedbackOnSuccess = feedback;
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

    //<editor-fold desc="重写接口">

    //<editor-fold desc="WaitTaskBuilder">
    public WaitLoadTaskBuilder<T> prepare(Runnable runnable) {
        super.prepare(runnable);
        return this;
    }
    public WaitLoadTaskBuilder<T> prepare(PrepareHandler handler) {
        super.prepare(handler);
        return this;
    }
    public WaitLoadTaskBuilder<T> working(WorkingHandler handler) {
        super.working(handler);
        return this;
    }
    public WaitLoadTaskBuilder<T> success(Runnable success) {
        super.success(success);
        return this;
    }
    public WaitLoadTaskBuilder<T> exception(ExceptionHandler handler) {
        return exception(true, handler);
    }
    //</editor-fold>

    //<editor-fold desc="LoadTaskBuilder">

    @Override
    public WaitLoadTaskBuilder<T> loadSuccess(LoadSuccessHandler<T> loadSuccessHandler) {
        return loadSuccess(false, loadSuccessHandler);
    }

    @Override
    public WaitLoadTaskBuilder<T> loadEmpty(Runnable emptyRunnable) {
        return loadEmpty(true, emptyRunnable);
    }

    //</editor-fold>

    public WaitLoadTaskBuilder<T> fina11y(Runnable finallyRunnable) {
        super.fina11y(finallyRunnable);
        return this;
    }

    //</editor-fold>

    //<editor-fold desc="禁用接口">

    @Override
    public <TT> WaitLoadBuilder<TT> load(LoadingHandler<TT> handler) {
        return null;
    }

    @Override
    public <TT> WaitLoadBuilder<TT> load(Class<TT> clazz) {
        return null;
    }
    @Override
    public <TT> WaitLoadBuilder<Set<TT>> loadSet(Class<TT> clazz) {
        return null;
    }
    @Override
    public <TT> WaitLoadBuilder<List<TT>> loadList(Class<TT> clazz) {
        return null;
    }
    @Override
    public <TT> WaitLoadBuilder<Collection<TT>> loadCollection(Class<TT> clazz) {
        return null;
    }
    @Override
    public <K,V> WaitLoadBuilder<Map<K,V>> loadMap(Class<K> key, Class<V> value) {
        return null;
    }
    @Override
    public WaitLoadBuilder<T> wait(Pager pager, String master) {
        return null;
    }
    //</editor-fold>
}
