package com.andframe.thread;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public abstract class AfHandlerTask extends AfTask implements Callback{

	protected abstract boolean onHandle(Message msg);

	protected AfHandlerTask() {
		super(null);
		this.mHandler = new Handler(AfApplication.getLooper(),this);
	}

	protected AfHandlerTask(int task) {
		super(null,task);
		this.mHandler = new Handler(AfApplication.getLooper(),this);
	}

	protected AfHandlerTask(Handler handler) {
		super(handler);
	}
	
	protected AfHandlerTask(Handler handler, int task) {
		super(handler,task);
	}

	@Override
	public final boolean handleMessage(Message msg) {
		boolean result = false;
		try {
			this.onHandle(msg);
		} catch (Throwable e) {
			String remark = "AfHandlerTask("+getClass().getName()+").handleMessage.onHandle";
			AfExceptionHandler.handler(e, remark);
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
