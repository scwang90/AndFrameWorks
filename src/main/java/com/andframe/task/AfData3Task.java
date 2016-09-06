package com.andframe.task;

/**
 * AbDataTask
 * Created by SCWANG on 2016/3/11.
 */
@SuppressWarnings("unused")
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

    public OnData3TaskHandlerListener<T, TT, TTT> getListener() {
        return listener;
    }

    @Override
    protected boolean onPrepare() {
        if (handler != null) {
            return handler.onPrepare(t, tt, ttt);
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
    protected void onHandle() {
        if (handler != null) {
            handler.onHandle(t, tt, ttt);
        } else {
            listener.onTaskHandle(t, tt, ttt, this);
        }
    }

    @Override
    protected void onWorking() throws Exception {
        if (handler != null) {
            handler.onWorking(t, tt, ttt);
            return;
        }
        listener.onTaskBackground(t, tt, ttt);
    }

    public T getData1() {
        return t;
    }

    public TT getData2() {
        return tt;
    }

    public TTT getData3() {
        return ttt;
    }

    public static abstract class AbData3TaskHandler<T, TT, TTT> implements OnData3TaskHandlerListener<T, TT, TTT> {

        AfData3Task<T, TT, TTT> task;

        private void setTask(AfData3Task<T, TT, TTT> task) {
            this.task = task;
        }

        public AfData3Task<T, TT, TTT> getTask() {
            return task;
        }

        public boolean onPrepare(T t, TT tt, TTT ttt) {
            return true;
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

    @SuppressWarnings("unused")
    public static abstract class AbData2TaskResultHandler<T,TT,TTT,Result> extends AbData3TaskHandler<T,TT,TTT> {

        protected Result result;

        @Override
        public void onTaskBackground(T t, TT tt, TTT ttt) throws Exception {
            result = onTaskBackgroundResult(t, tt, ttt);
        }

        protected abstract Result onTaskBackgroundResult(T t, TT tt, TTT ttt);

        @Override
        public boolean onTaskHandle(T t, TT tt, TTT ttt, AfData3Task task) {
            return onTaskHandle(result, t, tt, ttt, task);
        }

        protected abstract boolean onTaskHandle(Result result, T t, TT tt, TTT ttt, AfData3Task task);
    }
}
