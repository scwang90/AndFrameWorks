package com.andframe.task;

import android.os.Handler;
import android.os.Looper;

import com.andframe.api.task.TaskWithHandler;
import com.andframe.exception.AfExceptionHandler;

public abstract class AfHandlerTask extends AfTask implements TaskWithHandler {

	protected OnTaskFinishListener mListener;
	protected static Handler mHandler = new Handler(Looper.getMainLooper());

	protected abstract void onHandle();

	public AfHandlerTask setListener(OnTaskFinishListener mListener) {
		this.mListener = mListener;
		return this;
	}

	@Override
	public void run() {
		super.run();
		if (mStatus != Status.canceld && mHandler != null) {
			mHandler.post(this::handleMessage);
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
