package com.andframe.api.task.builder;

import com.andframe.api.task.Task;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

/**
 * 任务构建器
 * Created by SCWANG on 2017/4/28.
 */

public interface Builder {
    Builder prepare(Runnable runnable);
    Builder prepare(PrepareHandler handler);
    Builder working(WorkingHandler handler);
    Builder success(Runnable runnable);
    Builder exception(ExceptionHandler handler);
    <T> LoadBuilder<T> load(Class<T> clazz);
    void post();
    Task build();
}
