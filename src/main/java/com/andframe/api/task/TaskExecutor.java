package com.andframe.api.task;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.andframe.api.task.builder.Builder;
import com.andframe.api.task.handler.WorkingHandler;

/**
 * 任务执行器
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public interface TaskExecutor {

    Builder builder();
    Builder builder(boolean autoPost);
    Builder builder(@NonNull Object master);

    @MainThread
    void execute(@NonNull WorkingHandler runnable);

    @MainThread
    void execute(@NonNull Task task);

    <T extends Task> T postTask(@NonNull T task);

    <T extends Task> T postTask(@NonNull T task, long delay);

}
