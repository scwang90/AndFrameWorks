package com.andframe.api.task.builder;

import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
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

@SuppressWarnings("unused")
public interface Builder {

    Builder prepare(Runnable runnable);
    Builder prepare(PrepareHandler handler);
    Builder working(WorkingHandler handler);
    Builder success(Runnable runnable);
    Builder fina11y(Runnable runnable);
    Builder canceled(Runnable runnable);
    Builder exception(ExceptionHandler handler);

    Runnable prepare();
    Runnable canceled();
    PrepareHandler prepareHandler();
    WorkingHandler working();
    Runnable success();
    ExceptionHandler exception();

    WaitBuilder wait(Pager pager, String master);
    <T> LoadBuilder<T> load(LoadingHandler<T> handler);
    <T> LoadBuilder<T> load(Class<T> clazz);
    <T> LoadBuilder<Set<T>> loadSet(Class<T> clazz);
    <T> LoadBuilder<List<T>> loadList(Class<T> clazz);
    <T> LoadBuilder<Collection<T>> loadCollection(Class<T> clazz);
    <K,V> LoadBuilder<Map<K,V>> loadMap(Class<K> key, Class<V> value);

    Task build();
    Task post();
    Task post(long delay);
}
