package com.andframe.task;

import android.os.Handler;
import android.os.Looper;

import com.andframe.api.task.TaskWithHandler;
import com.andframe.exception.AfExceptionHandler;

public abstract class AfHandlerTask extends AfTask implements TaskWithHandler {

	protected OnTaskFinishListener mListener;
	protected final static Handler mHandler = new Handler(Looper.getMainLooper());

	protected abstract void onHandle();

	public AfHandlerTask setListener(OnTaskFinishListener mListener) {
		this.mListener = mListener;
		return this;
	}

	@Override
	public void run() {
		super.run();
		if (mStatus != Status.canceled) {
			mHandler.post(this::handleMessage);
		}
	}

	@Override
	public void cancel() {
		mStatus = Status.canceled;
		mHandler.post(() -> {
			try {
				onCancel();
			} catch (Throwable e) {
				String remark = "AfHandlerTask("+getClass().getName()+").cancel.onCancel";
				AfExceptionHandler.handle(e, remark);
			}
		});
	}

	public boolean handleMessage() {
		try {
			if (mListener != null) {
                mListener.onTaskFinish(this);
            }
		} catch (Throwable e) {
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
