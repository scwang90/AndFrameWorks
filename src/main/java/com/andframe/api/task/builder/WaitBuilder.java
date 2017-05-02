package com.andframe.api.task.builder;

import com.andframe.api.pager.Pager;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

/**
 * 集成等待对话框的任务 Builder
 * Created by SCWANG on 2017/5/3.
 */

public interface WaitBuilder extends Builder {

    /**
     * 特有接口
     */
    WaitBuilder success(boolean feedback, Runnable success);
    WaitBuilder exception(boolean feedback, ExceptionHandler handler);

    /**
     * 重写接口
     */
    WaitBuilder prepare(Runnable runnable);
    WaitBuilder prepare(PrepareHandler handler);
    WaitBuilder working(WorkingHandler handler);
    WaitBuilder success(Runnable success);
    WaitBuilder exception(ExceptionHandler handler);
    <T> WaitLoadBuilder<T> load(Class<T> clazz);

    /**
     * 禁用接口
     */
    @Deprecated
    WaitBuilder wait(Pager pager, String master);
}
