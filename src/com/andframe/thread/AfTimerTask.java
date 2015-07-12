package com.andframe.thread;

import java.util.TimerTask;

import com.andframe.application.AfExceptionHandler;

public abstract class AfTimerTask extends TimerTask{

	protected abstract void onTimer();

	@Override
	public final void run() {
		// TODO Auto-generated method stub
		try {
			this.onTimer();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			String remark = "AfTimerTask("+getClass().getName()+").run.onTimer";
			AfExceptionHandler.handler(e, remark);
		}
	}
}
