package com.andframe.task;

import android.os.Handler;
import android.os.Looper;

import com.andframe.exception.AfExceptionHandler;

/**
 *
 * Created by SCWANG on 2016/8/16.
 */
public class AfDispatcher implements Runnable {

    protected Runnable runnable;
    protected static Handler handler = new Handler(Looper.getMainLooper());

    private AfDispatcher(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public final void run() {
        try {
            runnable.run();
        } catch (Throwable e) {
            String remark = "AfDispatcher("+runnable.getClass().getName()+").run";
            AfExceptionHandler.handle(e, remark);
        }
    }

    public static void dispatch(Runnable runnable) {
        handler.post(new AfDispatcher(runnable));
    }

    public static void dispatch(Runnable runnable, long delay) {
        handler.postDelayed(new AfDispatcher(runnable), delay);
    }

}
