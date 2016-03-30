package com.andframe.thread;

import android.os.Message;

/**
 * AbDataTask
 * Created by SCWANG on 2016/3/11.
 */
public class AfData3Task<T, TT, TTT> extends AfHandlerTask {

    public interface OnData3TaskHandlerListener<T, TT, TTT> {
        void onTaskBackground(T t, TT tt, TTT ttt) throws Exception;
        boolean onTaskHandle(T t, TT tt, TTT ttt, AfData3Task task);
    }

    T t;
    TT tt;
    TTT ttt;
    AbData3TaskHandler<T, TT, TTT> handler;
    OnData3TaskHandlerListener<T, TT, TTT> listener;

    public AfData3Task(T t, TT tt, TTT ttt, OnData3TaskHandlerListener<T, TT, TTT> listener) {
        this.t = t;
        this.tt = tt;
        this.ttt = ttt;
        this.listener = listener;
        if (listener instanceof AbData3TaskHandler) {
            handler = (AbData3TaskHandler<T, TT, TTT>) listener;
            handler.setTask(this);
        }
    }

    @Override
    public boolean onPrepare() {
        if (handler != null && handler.onPrepare(t, tt, ttt)) {
            return true;
        }
        return super.onPrepare();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (handler != null) {
            handler.onCancel(t, tt, ttt);
        }
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        if (handler != null) {
            handler.onException(t, tt, ttt, e);
        }
    }

    @Override
    protected boolean onHandle(Message msg) {
        if (handler != null) {
            return handler.onHandle(t, tt, ttt);
        }
        return listener.onTaskHandle(t, tt, ttt, this);
    }

    @Override
    protected void onWorking(Message msg) throws Exception {
        if (handler != null) {
            handler.onWorking(t, tt, ttt);
            return;
        }
        listener.onTaskBackground(t, tt, ttt);
    }

    public static abstract class AbData3TaskHandler<T, TT, TTT> implements OnData3TaskHandlerListener<T, TT, TTT> {

        AfData3Task<T, TT, TTT> task;

        private void setTask(AfData3Task<T, TT, TTT> task) {
            this.task = task;
        }

        public boolean onPrepare(T t, TT tt, TTT ttt) {
            return false;
        }

        public void onCancel(T t, TT tt, TTT ttt) {
        }

        public String makeErrorToast(String tip) {
            return task.makeErrorToast(tip);
        }

        public final boolean onHandle(T t, TT tt, TTT ttt) {
            return onTaskHandle(t, tt, ttt, task);
        }

        public boolean isFail() {
            return task.isFail();
        }

        public final void onWorking(T t, TT tt, TTT ttt) throws Exception {
            this.onTaskBackground(t, tt, ttt);
        }

        public boolean isFinish() {
            return !isFail();
        }

        public void onException(T t, TT tt,TTT ttt, Throwable e) {

        }
    }
}
