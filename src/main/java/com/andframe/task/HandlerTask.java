package com.andframe.task;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import com.andframe.$;
import com.andframe.api.task.TaskWithHandler;

/**
 * 带 Handler 实现结束回调的任务
 */
@SuppressWarnings("WeakerAccess")
public abstract class HandlerTask extends AbstractTask implements TaskWithHandler {

	protected String mMasterName;
	protected OnTaskFinishListener mListener;
	protected final static Handler mHandler = new Handler(Looper.getMainLooper());

	protected abstract void onHandle();

	public HandlerTask() {
		mMasterName = null;
	}

	public HandlerTask(@NonNull Object master) {
		mMasterName = master.getClass().getName();
	}

	public HandlerTask setListener(OnTaskFinishListener mListener) {
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
				String remark = "AfHandlerTask(" + getMaster() + ").cancel.onCancel";
				$.error().handle(e, remark);
			}
		});
	}

	public void handleMessage() {
		try {
			if (mListener != null) {
                mListener.onTaskFinish(this);
            }
		} catch (Throwable e) {
			String remark = "AfHandlerTask(" + getMaster() + "->" + mListener.getClass().getName() + ").handleMessage.onTaskFinish";
			$.error().handle(e, remark);
		}
		try {
			this.onHandle();
		} catch (Throwable e) {
			String remark = "AfHandlerTask(" + getMaster() + ").handleMessage.onHandle";
			$.error().handle(e, remark);
		}
	}

	protected String getMaster() {
		if (mMasterName != null) {
			return mMasterName + " in (" + getClass().getName() + ")";
		}
		return getClass().getName();
	}
}
