package com.andframe.thread;

import android.os.Handler;
import android.os.Looper;

public class AfThreadWorkerOld extends Thread{

	private final Object mLock = new Object();
    private Looper mLooper;
    private Handler mHandler;
    private Thread mThread;
    /**
     * Creates a worker thread with the given name. The thread
     * then runs a {@link android.os.Looper}.
     * @param name A name for the new thread
     */
    public AfThreadWorkerOld(String name) {
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
    public AfThreadWorkerOld(Object obj) {
        this(obj.getClass().getSimpleName());
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		if (task.onPrepare()) {
			getHandler().post(task);
		}
	}
	public AfTask postTask(AfTask task) {
		// TODO Auto-generated method stub
		if (task.onPrepare()) {
			getHandler().post(task);
		}
		return task;
	}
	public AfTask postTaskDelayed(AfTask task,long delay) {
		// TODO Auto-generated method stub
		if (task.onPrepare()) {
			getHandler().postDelayed(task,delay);
		}
		return task;
	}
    
}
