package com.andframe.task;

import android.os.Handler;
import android.os.Looper;

import com.andframe.exception.AfExceptionHandler;

public abstract class AfHandlerTask extends AfTask {

	public interface OnTaskFinishListener {
		void onTaskFinish(AfHandlerTask task);
	}

	protected OnTaskFinishListener mListener;
	protected static Handler mHandler = new Handler(Looper.getMainLooper());

	protected abstract void onHandle();

	public AfHandlerTask setListener(OnTaskFinishListener mListener) {
		this.mListener = mListener;
		return this;
	}

	@Override
	public void run() {
		if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
			handleMessage();
		} else {
			super.run();
			if (!mIsCanceled && mHandler != null) {
				mHandler.post(this);
			}
		}
	}

	public final boolean handleMessage() {
		try {
			if (mListener != null) {
                mListener.onTaskFinish(this);
            }
		} catch (Exception e) {
			String remark = "AfHandlerTask("+mListener.getClass().getName()+").handleMessage.onTaskFinish";
			AfExceptionHandler.handle(e, remark);
		}
		try {
			this.onHandle();
		} catch (Throwable e) {
			String remark = "AfHandlerTask("+getClass().getName()+").handleMessage.onHandle";
			AfExceptionHandler.handle(e, remark);
		}
		return true;
	}

}
