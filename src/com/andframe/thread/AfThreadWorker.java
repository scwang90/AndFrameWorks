package com.andframe.thread;

import android.os.Handler;
import android.os.Looper;


public class AfThreadWorker extends Thread{

	private final Object mLock = new Object();
    private Looper mLooper;
    private Handler mHandler;
    private Thread mThread;
    /**
     * Creates a worker thread with the given name. The thread
     * then runs a {@link android.os.Looper}.
     * @param name A name for the new thread
     */
    public AfThreadWorker(String name) {
        super(name);
        this.setPriority(Thread.MIN_PRIORITY);
        this.start();
        synchronized (mLock) {
            while (mLooper == null) {
                try {
                    mLock.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    /**
     * Creates a worker thread with the given name. The thread
     * then runs a {@link android.os.Looper}.
     * @param mTvName A name for the new thread
     */
    public AfThreadWorker(Object obj) {
        this(obj.getClass().getSimpleName());
    }

	@Override
	public void run() {
		synchronized (mLock) {
			Looper.prepare();
			mLooper = Looper.myLooper();
			mHandler = new Handler(mLooper);
			mLock.notifyAll();
		}
		Looper.loop();
	}
	
    public Looper getLooperu() {
        return mLooper;
    }
    
    public void quit() {
        mLooper.quit();
    }

	public Handler getHandler() {
		return mHandler;
	}

	public Thread getThread() {
		return mThread;
	}
	
	public void post(AfTask task) {
		if (!task.isCanceled() && task.onPrepare()) {
			getHandler().post(task);
		}
	}
	public AfTask postTask(AfTask task) {
		if (!task.isCanceled() && task.onPrepare()) {
			getHandler().post(task);
		}
		return task;
	}
	public AfTask postTaskDelayed(AfTask task,long delay) {
		if (!task.isCanceled() && task.onPrepare()) {
			getHandler().postDelayed(task,delay);
		}
		return task;
	}

	/**
	 * 抛送带数据任务到Worker执行
	 */
	public <T> AfTask postDataTask(T t, AfDataTask.OnTaskHandlerListener<T> task) {
		return postTask(new AfDataTask<>(t, task));
	}

	/**
	 * 抛送带数据任务到Worker执行
	 */
	public <T, TT> AfTask postDataTask(T t, TT tt, AfData2Task.OnData2TaskHandlerListener<T, TT> task) {
		return postTask(new AfData2Task<>(t, tt, task));
	}

	/**
	 * 抛送带数据任务到Worker执行
	 */
	public <T, TT, TTT> AfTask postDataTask(T t, TT tt, TTT ttt, AfData3Task.OnData3TaskHandlerListener<T, TT, TTT> task) {
		return postTask(new AfData3Task<>(t, tt, ttt, task));
	}
    
}
