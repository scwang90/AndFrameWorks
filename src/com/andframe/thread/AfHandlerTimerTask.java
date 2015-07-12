package com.andframe.thread;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;

public abstract class AfHandlerTimerTask extends AfTimerTask implements Callback{

	protected Handler mHandler;
	
	protected abstract boolean onHandleTimer(Message msg);

	protected AfHandlerTimerTask() {
		// TODO Auto-generated constructor stub
		this.mHandler = new Handler(AfApplication.getLooper(),this);
	}

	@Override
	protected final void onTimer() {
		// TODO Auto-generated method stub
		Message tMessage = Message.obtain();
		this.mHandler.sendMessage(tMessage);
	}
	
	@Override
	public final boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		boolean result = false;
		try {
			result = this.onHandleTimer(msg);
		} catch (Throwable e) {
			// TODO: handle exception
			String remark = "AfHandlerTimerTask("+getClass().getName()+").handleMessage.onHandleTimer";
			AfExceptionHandler.handler(e, remark);
		}
		return result;
	}


}
