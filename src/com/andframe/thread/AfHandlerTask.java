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
		// TODO Auto-generated constructor stub
		this.mHandler = new Handler(AfApplication.getLooper(),this);
	}

	protected AfHandlerTask(int task) {
		super(null,task);
		// TODO Auto-generated constructor stub
		this.mHandler = new Handler(AfApplication.getLooper(),this);
	}

	protected AfHandlerTask(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}
	
	protected AfHandlerTask(Handler handler, int task) {
		super(handler,task);
		// TODO Auto-generated constructor stub
	}

	@Override
	public final boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		boolean result = false;
		try {
			this.onHandle(msg);
		} catch (Throwable e) {
			// TODO: handle exception
			String remark = "AfHandlerTask.handleMessage.onHandle 出现异常！\r\n";
			remark += "class = " + getClass().toString();
			AfExceptionHandler.handler(e, remark);
		}
		return result;
	}

	public static AfHandlerTask getTask(Message msg) {
		// TODO Auto-generated method stub
		if (msg.obj instanceof AfHandlerTask) {
			return (AfHandlerTask) msg.obj;
		}
		return null;
	}
}
