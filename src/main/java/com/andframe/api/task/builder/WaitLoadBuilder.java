package com.andframe.api.task.builder;

import com.andframe.api.EmptyDecider;
import com.andframe.api.pager.Pager;
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

@SuppressWarnings("SameParameterValue")
public interface WaitLoadBuilder<T> extends WaitBuilder, LoadBuilder<T> {

    /**
     * 特有接口
     */
    WaitLoadBuilder<T> loadEmpty(boolean feedback, Runnable runnable);
    WaitLoadBuilder<T> loadSuccess(boolean feedback, LoadSuccessHandler<T> handler);

    /**
     * 重写接口
     */
    @Override
    WaitLoadBuilder<T> isEmpty(EmptyDecider<T> handler);
    @Override
    WaitLoadBuilder<T> prepare(Runnable runnable);
    @Override
    WaitLoadBuilder<T> prepare(PrepareHandler handler);
    @Override
    WaitLoadBuilder<T> loading(LoadingHandler<T> handler);
    @Override
    WaitLoadBuilder<T> loadEmpty(Runnable runnable);
    @Override
    WaitLoadBuilder<T> loadSuccess(LoadSuccessHandler<T> handler);
    @Override
    WaitLoadBuilder<T> exception(ExceptionHandler handler);
    @Override
    WaitLoadBuilder<T> exception(boolean feedback, ExceptionHandler handler);
    @Override
    WaitLoadBuilder<T> fina11y(Runnable fina11y);
    /**
     * 禁用接口
     */
    @Deprecated
    WaitLoadBuilder<T> success(Runnable success);
    @Deprecated
    WaitLoadBuilder<T> success(boolean feedback, Runnable success);
    @Deprecated
    WaitLoadBuilder<T> working(WorkingHandler handler);
    @Deprecated
    WaitLoadBuilder<T> wait(Pager pager, String master);
    @Deprecated
    <TT> WaitLoadBuilder<TT> load(LoadingHandler<TT> handler);
    @Deprecated
    <TT> WaitLoadBuilder<TT> load(Class<TT> clazz);
    @Deprecated
    <TT> WaitLoadBuilder<Set<TT>> loadSet(Class<TT> clazz);
    @Deprecated
    <TT> WaitLoadBuilder<List<TT>> loadList(Class<TT> clazz);
    @Deprecated
    <TT> WaitLoadBuilder<Collection<TT>> loadCollection(Class<TT> clazz);
    @Deprecated
    <K,V> WaitLoadBuilder<Map<K,V>> loadMap(Class<K> key, Class<V> value);
}
