package com.andframe.api.task.builder;

import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

import java.util.List;

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

    Runnable prepare();
    PrepareHandler prepareHandler();
    WorkingHandler working();
    Runnable success();
    ExceptionHandler exception();

    WaitBuilder wait(Pager pager, String master);
    <T> LoadBuilder<T> load(Class<T> clazz);
    <T> LoadBuilder<List<T>> loadList(Class<T> clazz);

    Task post();
    Task build();

}
