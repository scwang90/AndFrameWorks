package com.andframe.thread;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.andframe.application.AfExceptionHandler;

public abstract class AfDispatch implements Runnable, Callback{

	@Override
	public final void run() {
		try {
			this.onDispatch();
		} catch (Throwable e) {
			String remark = "AfDispatch("+getClass().getName()+").run.onDispatch";
			AfExceptionHandler.handle(e, remark);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		this.run();
		return true;
	}

	protected abstract void onDispatch();

	public void dispatch(Looper looper) {
		new Handler(looper,this).post(this);
	}

	public void dispatch(Looper looper, long delay) {
		new Handler(looper,this).postDelayed(this,delay);
	}
	
}
