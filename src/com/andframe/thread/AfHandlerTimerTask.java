package com.andframe.thread;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.andframe.application.AfExceptionHandler;

public abstract class AfHandlerTimerTask extends AfTimerTask implements Callback{

	protected Handler mHandler;
	
	protected abstract boolean onHandleTimer(Message msg);

	protected AfHandlerTimerTask() {
		this.mHandler = new Handler(Looper.getMainLooper(),this);
	}

	@Override
	protected final void onTimer() {
		Message tMessage = Message.obtain();
		this.mHandler.sendMessage(tMessage);
	}
	
	@Override
	public final boolean handleMessage(Message msg) {
		boolean result = false;
		try {
			result = this.onHandleTimer(msg);
		} catch (Throwable e) {
			String remark = "AfHandlerTimerTask("+getClass().getName()+").handleMessage.onHandleTimer";
			AfExceptionHandler.handler(e, remark);
		}
		return result;
	}


}
