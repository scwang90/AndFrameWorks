package com.andframe.task;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.andframe.exception.AfExceptionHandler;

public abstract class AfHandlerTask extends AfTask implements Callback {

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
			handleMessage(null);
		} else {
			super.run();
			if (!mIsCanceled && mHandler != null) {
				mHandler.post(this);
			}
		}
	}

	@Override
	public final boolean handleMessage(Message msg) {
		try {
			if (mListener != null) {
                mListener.onTaskFinish(this);
            }
		} catch (Exception e) {
			String remark = "AfHandlerTask("+getClass().getName()+").handleMessage.onTaskFinish";
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
