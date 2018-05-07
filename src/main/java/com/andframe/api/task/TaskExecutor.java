package com.andframe.api.task;

import android.support.annotation.MainThread;

import com.andframe.api.task.builder.Builder;
import com.andframe.api.task.handler.WorkingHandler;

/**
 * 任务执行器
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public interface TaskExecutor {

    Builder builder();

    @MainThread
    void execute(WorkingHandler runnable);

    @MainThread
    void execute(Task task);

    <T extends Task> T postTask(T task);

    <T extends Task> T postTask(T task, long delay);

}
