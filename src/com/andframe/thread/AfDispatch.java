package com.andframe.thread;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.andframe.application.AfExceptionHandler;

public abstract class AfDispatch implements Runnable, Callback{

	@Override
	public final void run() {
		// TODO Auto-generated method stub
		try {
			this.onDispatch();
		} catch (Throwable e) {
			// TODO: handle exception
			String remark = "AfUIHandle.run ≥ˆœ÷“Ï≥££°\r\n";
			remark += "class = " + getClass().toString();
			AfExceptionHandler.handler(e, remark);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		this.run();
		return true;
	}

	protected abstract void onDispatch();

	public void dispatch(Looper looper) {
		// TODO Auto-generated method stub
		//new Handler(looper,this).dispatchMessage(Message.obtain());
		new Handler(looper,this).post(this);
	}
	
}
