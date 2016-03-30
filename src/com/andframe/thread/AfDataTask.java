package com.andframe.thread;

import android.os.Message;

/**
 * 带有效数据的任务
 * Created by SCWANG on 2016/3/30.
 */
public class AfDataTask <T> extends AfHandlerTask {

    public interface OnTaskHandlerListener<T> {
        void onTaskBackground(T data) throws Exception;
        boolean onTaskHandle(T data, AfDataTask task);
    }

    T data;
    AbDataTaskHandler<T> handler;
    OnTaskHandlerListener<T> listener;

    public AfDataTask(T data, OnTaskHandlerListener<T> listener) {
        this.data = data;
        this.listener = listener;
        if (listener instanceof AbDataTaskHandler) {
            handler = (AbDataTaskHandler<T>) listener;
            handler.setTask(this);
        }
    }

    @Override
    public boolean onPrepare() {
        if (handler != null && handler.onPrepare(data)) {
            return true;
        }
        return super.onPrepare();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        if (handler != null) {
            handler.onCancel(data);
        }
    }

    @Override
    protected void onException(Throwable e) {
        super.onException(e);
        if (handler != null) {
            handler.onException(data,e);
        }
    }

    @Override
    protected boolean onHandle(Message msg) {
        if (handler != null) {
            return handler.onHandle(data);
        }
        return listener.onTaskHandle(data, this);
    }

    @Override
    protected void onWorking(Message msg) throws Exception {
        if (handler != null) {
            handler.onWorking(data);
            return;
        }
        listener.onTaskBackground(data);
    }

    public static abstract class AbDataTaskHandler<T> implements OnTaskHandlerListener<T> {

        AfDataTask<T> task;

        private void setTask(AfDataTask<T> task) {
            this.task = task;
        }

        public boolean onPrepare(T data) {
            return false;
        }

        public void onCancel(T data) {
        }

        public String makeErrorToast(String tip) {
            return task.makeErrorToast(tip);
        }

        public final boolean onHandle(T data) {
            return onTaskHandle(data, task);
        }

        public boolean isFail() {
            return task.isFail();
        }

        public final void onWorking(T data) throws Exception {
            this.onTaskBackground(data);
        }

        public boolean isFinish() {
            return !isFail();
        }

        public void onException(T data, Throwable e) {

        }
    }
}
