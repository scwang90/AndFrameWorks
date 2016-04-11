package com.andframe.thread;

import android.os.Message;

/**
 * 带有效数据的任务
 * Created by SCWANG on 2016/3/30.
 */
public class AfDataTask <T> extends AfHandlerTask {

    public interface OnTaskHandlerListener<T> {
        void onTaskBackground(T t) throws Exception;
        boolean onTaskHandle(T t, AfDataTask task);
    }

    T t;
    AbDataTaskHandler<T> handler;
    OnTaskHandlerListener<T> listener;

    public AfDataTask(T t, OnTaskHandlerListener<T> listener) {
        this.t = t;
        this.listener = listener;
        if (listener instanceof AbDataTaskHandler) {
            handler = (AbDataTaskHandler<T>) listener;
            handler.setTask(this);
        }
    }

    @Override
    public boolean onPrepare() {
        if (handler != null) {
            return handler.onPrepare(t);
        }
        return super.onPrepare();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (handler != null) {
            handler.onCancel(t);
        }
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        if (handler != null) {
            handler.onException(t,e);
        }
    }

    @Override
    protected boolean onHandle(Message msg) {
        if (handler != null) {
            return handler.onHandle(t);
        }
        return listener.onTaskHandle(t, this);
    }

    @Override
    protected void onWorking(Message msg) throws Exception {
        if (handler != null) {
            handler.onWorking(t);
            return;
        }
        listener.onTaskBackground(t);
    }

    public T getData() {
        return t;
    }

    public static abstract class AbDataTaskHandler<T> implements OnTaskHandlerListener<T> {

        AfDataTask<T> task;

        private void setTask(AfDataTask<T> task) {
            this.task = task;
        }

        public AfDataTask<T> getTask() {
            return task;
        }

        public boolean onPrepare(T t) {
            return true;
        }

        public void onCancel(T t) {
        }

        public String makeErrorToast(String tip) {
            return task.makeErrorToast(tip);
        }

        public final boolean onHandle(T t) {
            return onTaskHandle(t, task);
        }

        public boolean isFail() {
            return task.isFail();
        }

        public final void onWorking(T t) throws Exception {
            this.onTaskBackground(t);
        }

        public boolean isFinish() {
            return !isFail();
        }

        public void onException(T t, Throwable e) {

        }
    }
}
