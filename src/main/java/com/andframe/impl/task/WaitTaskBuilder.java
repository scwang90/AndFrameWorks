package com.andframe.impl.task;

import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
import com.andframe.api.task.builder.WaitBuilder;
import com.andframe.api.task.builder.WaitLoadBuilder;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

import java.lang.ref.WeakReference;

/**
 * 集成等待对话框的任务 Builder
 * Created by SCWANG on 2017/5/3.
 */
public class WaitTaskBuilder extends TaskBuilder implements WaitBuilder {

    String master;
    WeakReference<Pager> pager;
    boolean feedbackOnSuccess = true;
    boolean feedbackOnException = true;

    public WaitTaskBuilder(TaskBuilder builder, Pager pager, String master) {
        this.master = master;
        this.pager = new WeakReference<>(pager);
        this.prepareRunnable = builder.prepareRunnable;
        this.prepareHandler = builder.prepareHandler;
        this.workingHandler = builder.workingHandler;
        this.successRunnable = builder.successRunnable;
        this.exceptionHandler = builder.exceptionHandler;
    }

    //<editor-fold desc="特有接口">

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
        return success(true, success);
    }
    public WaitBuilder exception(ExceptionHandler handler) {
        return exception(true, handler);
    }
    public <T> WaitLoadBuilder<T> load(Class<T> clazz) {
        return new WaitLoadTaskBuilder<>(this, clazz);
    }
    //</editor-fold>
}
