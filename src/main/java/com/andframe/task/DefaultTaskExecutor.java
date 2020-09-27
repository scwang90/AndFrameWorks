package com.andframe.task;

import android.os.Looper;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.andframe.$;
import com.andframe.api.task.Task;
import com.andframe.api.task.TaskExecutor;
import com.andframe.api.task.builder.Builder;
import com.andframe.api.task.handler.WorkingHandler;
import com.andframe.application.AfApp;
import com.andframe.impl.task.TaskBuilder;
import com.andframe.util.internal.TAG;
import com.andframe.util.java.AfDateGuid;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务执行器
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public class DefaultTaskExecutor implements TaskExecutor {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(@NonNull Runnable r) {
            if (r instanceof AbstractTask) {
                AbstractTask task = (AbstractTask) r;
                String name = AfDateGuid.NewID();
                if (AfApp.get().isDebug()) {
                    name = task.getClass().getSimpleName();
                }
                return new Thread(r, "DefaultTaskExecutor-" + name + " #" + mCount.getAndIncrement());
            }
            return new Thread(r, "DefaultTaskExecutor #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(128);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    private static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    private static volatile Executor sDefaultExecutor = THREAD_POOL_EXECUTOR;
    public static void setDefaultExecutor(Executor exec) {
        sDefaultExecutor = exec;
    }

    /**
     * Convenience version of for use with
     * a simple Runnable object. See for more
     * information on the order of execution.
     */
    @MainThread
    public void execute(@NonNull WorkingHandler runnable) {
        sDefaultExecutor.execute(() -> {
            try {
                runnable.onWorking();
            } catch (Throwable e) {
                $.error().handle(e, TAG.TAG(runnable, "DefaultTaskExecutor.execute"));
            }
        });
    }

    @MainThread
    public void execute(@NonNull Task task) {
        if (task.status() != Task.Status.canceled && task.prepare()) {
            sDefaultExecutor.execute(task);
        }
    }

    public <T extends Task> T postTask(@NonNull T task) {
        task.reset();
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            execute(task);
        } else {
            Dispatcher.dispatch(() -> execute(task));
        }
        return task;
    }

    public <T extends Task> T postTask(@NonNull T task, long delay) {
        task.reset();
        Dispatcher.dispatch(() -> execute(task), delay);
        return task;
    }

    @Override
    public Builder builder() {
        return new TaskBuilder(true);
    }

    @Override
    public Builder builder(boolean autoPost) {
        return new TaskBuilder(autoPost);
    }

    @Override
    public Builder builder(@NonNull Object master) {
        return new TaskBuilder(master);
    }

}
