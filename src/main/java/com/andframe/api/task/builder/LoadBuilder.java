package com.andframe.api.task.builder;

import com.andframe.api.task.handler.LoadSuccessHandler;
import com.andframe.api.task.handler.LoadingHandler;

/**
 * 任务构建器
 * Created by SCWANG on 2017/4/28.
 */

public interface LoadBuilder<T> extends Builder {
    LoadBuilder<T> loading(LoadingHandler<T> handler);
    LoadBuilder<T> loadSuccess(LoadSuccessHandler<T> handler);
    LoadBuilder<T> loadEmpty(Runnable runnable);
}
