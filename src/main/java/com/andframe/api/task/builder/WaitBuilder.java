package com.andframe.api.task.builder;

import com.andframe.api.pager.Pager;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.LoadingHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集成等待对话框的任务 Builder
 * Created by SCWANG on 2017/5/3.
 */

@SuppressWarnings("unused")
public interface WaitBuilder extends Builder {

    /**
     * 特有接口
     */
    String master();
    WaitBuilder success(boolean feedback, Runnable success);
    WaitBuilder exception(boolean feedback, ExceptionHandler handler);

    boolean feedbackSuccess();
    boolean feedbackException();

    /**
     * 重写接口
     */
    WaitBuilder prepare(Runnable runnable);
    WaitBuilder prepare(PrepareHandler handler);
    WaitBuilder working(WorkingHandler handler);
    WaitBuilder success(Runnable success);
    WaitBuilder fina11y(Runnable fina11y);
    WaitBuilder exception(ExceptionHandler handler);
    <T> WaitLoadBuilder<T> load(LoadingHandler<T> handler);
    <T> WaitLoadBuilder<T> load(Class<T> clazz);
    <T> WaitLoadBuilder<Set<T>> loadSet(Class<T> clazz);
    <T> WaitLoadBuilder<List<T>> loadList(Class<T> clazz);
    <T> WaitLoadBuilder<Collection<T>> loadCollection(Class<T> clazz);
    <K,V> WaitLoadBuilder<Map<K,V>> loadMap(Class<K> key, Class<V> value);

    /**
     * 禁用接口
     */
    @Deprecated
    WaitBuilder wait(Pager pager, String master);

}
