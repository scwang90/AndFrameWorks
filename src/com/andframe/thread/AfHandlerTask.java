package com.andframe.thread;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.andframe.application.AfExceptionHandler;

public abstract class AfHandlerTask extends AfTask implements Callback{

	public interface OnTaskFinishListener {
		void onTaskFinish(AfHandlerTask task);
	}

//	private final int mTask;
	protected final Handler mHandler;
	protected OnTaskFinishListener mListener;

	protected abstract boolean onHandle(/*Message msg*/);

	protected AfHandlerTask() {
//		this.mTask = 0;
		this.mHandler = new Handler(Looper.getMainLooper(),this);
	}

	public AfHandlerTask setListener(OnTaskFinishListener mListener) {
		this.mListener = mListener;
		return this;
	}

	//	protected AfHandlerTask(int task) {
//		this.mTask = task;
//		this.mHandler = new Handler(AfApplication.getLooper(),this);
//	}

//	protected AfHandlerTask(Handler handle) {
//		super(handle);
//	}
	
//	protected AfHandlerTask(Handler handle, int task) {
//		super(handle,task);
//	}


	@Override
	public void run() {
		super.run();
		if (!mIsCanceled && mHandler != null) {
			Message message = Message.obtain();
			message.obj = this;
			message.what = mResult;
			mHandler.sendMessage(message);
		}
	}

	@Override
	public final boolean handleMessage(Message msg) {
		boolean result = false;
		try {
			if (mListener != null) {
                mListener.onTaskFinish(this);
            }
		} catch (Exception e) {
			String remark = "AfHandlerTask("+getClass().getName()+").handleMessage.onTaskFinish";
			AfExceptionHandler.handle(e, remark);
		}
		try {
			result = this.onHandle(/*msg*/);
		} catch (Throwable e) {
			String remark = "AfHandlerTask("+getClass().getName()+").handleMessage.onHandle";
			AfExceptionHandler.handle(e, remark);
		}
		return result;
	}

	public static AfHandlerTask getTask(Message msg) {
		if (msg.obj instanceof AfHandlerTask) {
			return (AfHandlerTask) msg.obj;
		}
		return null;
	}
}
