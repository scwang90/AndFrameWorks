package com.andframe.thread;

import android.os.Message;

/**
 * AbDataTask
 * Created by SCWANG on 2016/3/11.
 */
public class AfData2Task<T, TT> extends AfHandlerTask {

    public interface OnData2TaskHandlerListener<T, TT> {
        void onTaskBackground(T t, TT tt) throws Exception;
        boolean onTaskHandle(T t, TT tt, AfData2Task task);
    }

    T t;
    TT tt;
    AbData2TaskHandler<T, TT> handler;
    OnData2TaskHandlerListener<T, TT> listener;

    public AfData2Task(T t, TT tt, OnData2TaskHandlerListener<T, TT> listener) {
        this.t = t;
        this.tt = tt;
        this.listener = listener;
        if (listener instanceof AbData2TaskHandler) {
            handler = (AbData2TaskHandler<T, TT>) listener;
            handler.setTask(this);
        }
    }

    @Override
    public boolean onPrepare() {
        if (handler != null && handler.onPrepare(t, tt)) {
            return true;
        }
        return super.onPrepare();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (handler != null) {
            handler.onCancel(t, tt);
        }
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        if (handler != null) {
            handler.onException(t, tt, e);
        }
    }

    @Override
    protected boolean onHandle(Message msg) {
        if (handler != null) {
            return handler.onHandle(t, tt);
        }
        return listener.onTaskHandle(t, tt, this);
    }

    @Override
    protected void onWorking(Message msg) throws Exception {
        if (handler != null) {
            handler.onWorking(t, tt);
            return;
        }
        listener.onTaskBackground(t, tt);
    }

    public T getData1() {
        return t;
    }

    public TT getData2() {
        return tt;
    }

    public static abstract class AbData2TaskHandler<T, TT> implements OnData2TaskHandlerListener<T, TT> {

        AfData2Task<T, TT> task;

        private void setTask(AfData2Task<T, TT> task) {
            this.task = task;
        }

        public AfData2Task<T, TT> getTask() {
            return task;
        }

        public boolean onPrepare(T t, TT tt) {
            return false;
        }

        public void onCancel(T t, TT tt) {
        }

        public String makeErrorToast(String tip) {
            return task.makeErrorToast(tip);
        }

        public final boolean onHandle(T t, TT tt) {
            return onTaskHandle(t, tt, task);
        }

        public boolean isFail() {
            return task.isFail();
        }

        public final void onWorking(T t, TT tt) throws Exception {
            this.onTaskBackground(t, tt);
        }

        public boolean isFinish() {
            return !isFail();
        }

        public void onException(T t, TT tt, Throwable e) {

        }
    }
}
