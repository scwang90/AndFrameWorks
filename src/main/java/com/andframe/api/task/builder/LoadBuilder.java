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
 * 数据加载任务构建器
 * Created by SCWANG on 2017/4/28.
 */

@SuppressWarnings("unused")
public interface LoadBuilder<T> extends Builder {

    /**
     * 特有接口
     */
    LoadBuilder<T> isEmpty(EmptyDecider<T> handler);
    LoadBuilder<T> loading(LoadingHandler<T> handler);
    LoadBuilder<T> loadSuccess(LoadSuccessHandler<T> handler);
    LoadBuilder<T> loadEmpty(Runnable runnable);

    LoadingHandler<T> loading();
    LoadSuccessHandler<T> loadSuccess();
    Runnable loadEmpty();

    /**
     * 重写接口
     */
    LoadBuilder<T> prepare(Runnable runnable);
    LoadBuilder<T> prepare(PrepareHandler handler);
    LoadBuilder<T> fina11y(Runnable fina11y);
    LoadBuilder<T> exception(ExceptionHandler handler);
    WaitLoadBuilder<T> wait(Pager pager, String master);

    /**
     * 禁用接口
     */
    @Deprecated
    Builder working(WorkingHandler handler);
    @Deprecated
    Builder success(Runnable runnable);
    @Deprecated
    <TT> LoadBuilder<TT> load(LoadingHandler<TT> handler);
    @Deprecated
    <TT> LoadBuilder<TT> load(Class<TT> clazz);
    @Deprecated
    <TT> LoadBuilder<Set<TT>> loadSet(Class<TT> clazz);
    @Deprecated
    <TT> LoadBuilder<List<TT>> loadList(Class<TT> clazz);
    @Deprecated
    <TT> LoadBuilder<Collection<TT>> loadCollection(Class<TT> clazz);
    @Deprecated
    <K,V> LoadBuilder<Map<K,V>> loadMap(Class<K> key, Class<V> value);
}
