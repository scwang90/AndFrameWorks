package com.andframe.thread;

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

    public OnData2TaskHandlerListener<T, TT> getListener() {
        return listener;
    }

    @Override
    protected boolean onPrepare() {
        if (handler != null) {
            return handler.onPrepare(t, tt);
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
    protected boolean onHandle() {
        if (handler != null) {
            return handler.onHandle(t, tt);
        }
        return listener.onTaskHandle(t, tt, this);
    }

    @Override
    protected void onWorking() throws Exception {
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
            return true;
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

    @SuppressWarnings("unused")
    public static abstract class AbData2TaskResultHandler<T,TT,Result> extends AbData2TaskHandler<T,TT> {

        protected Result result;

        @Override
        public void onTaskBackground(T t, TT tt) throws Exception {
            result = onTaskBackgroundResult(t, tt);
        }

        protected abstract Result onTaskBackgroundResult(T t, TT tt);

        @Override
        public boolean onTaskHandle(T t, TT tt, AfData2Task task) {
            return onTaskHandle(result, t, tt, task);
        }

        protected abstract boolean onTaskHandle(Result result, T t, TT tt, AfData2Task task);
    }
}
