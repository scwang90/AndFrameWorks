package com.andframe.thread;

import android.os.Handler;
import android.os.Looper;

import com.andframe.application.AfExceptionHandler;

/**
 *
 * Created by SCWANG on 2016/8/16.
 */
public class AfDispatcher implements Runnable {

    Runnable runnable;

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
        new Handler(Looper.getMainLooper()).post(new AfDispatcher(runnable));
    }

    public static void dispatch(Runnable runnable, long delay) {
        new Handler(Looper.getMainLooper()).postDelayed(new AfDispatcher(runnable), delay);
    }

}
