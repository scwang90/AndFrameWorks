package com.andframe.task;

import android.os.Handler;
import android.os.Looper;

import com.andframe.$;
import com.andframe.exception.AfExceptionHandler;

/**
 *
 * Created by SCWANG on 2016/8/16.
 */
public class Dispatcher implements Runnable {

    protected Runnable runnable;
    protected static Handler handler = new Handler(Looper.getMainLooper());

    private Dispatcher(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public final void run() {
        try {
            runnable.run();
        } catch (Throwable e) {
            String remark = "AfDispatcher("+runnable.getClass().getName()+").run";
            $.error().handle(e, remark);
        }
    }

    public static void dispatch(Runnable runnable) {
        handler.post(new Dispatcher(runnable));
    }

    public static void dispatch(Runnable runnable, long delay) {
        handler.postDelayed(new Dispatcher(runnable), delay);
    }

}
