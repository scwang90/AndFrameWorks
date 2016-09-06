package com.andframe.api;

import android.support.annotation.MainThread;

import com.andframe.task.AfData2Task;
import com.andframe.task.AfData2Task.OnData2TaskHandlerListener;
import com.andframe.task.AfData3Task;
import com.andframe.task.AfData3Task.OnData3TaskHandlerListener;
import com.andframe.task.AfDataTask;
import com.andframe.task.AfDataTask.OnTaskHandlerListener;
import com.andframe.task.AfTask;

/**
 * 任务执行器
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public interface TaskExecutor {

    @MainThread
    void execute(Runnable runnable);

    @MainThread
    void execute(AfTask task);

    <T extends AfTask> T postTask(T task);

    <T> AfDataTask postDataTask(T t, OnTaskHandlerListener<T> task);

    <T, TT> AfData2Task postDataTask(T t, TT tt, OnData2TaskHandlerListener<T, TT> task);

    <T, TT, TTT> AfData3Task postDataTask(T t, TT tt, TTT ttt, OnData3TaskHandlerListener<T, TT, TTT> task);
}
