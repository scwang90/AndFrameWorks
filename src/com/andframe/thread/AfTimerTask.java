package com.andframe.thread;

import com.andframe.application.AfExceptionHandler;

import java.util.TimerTask;

public abstract class AfTimerTask extends TimerTask{

	protected abstract void onTimer();

	@Override
	public final void run() {
		try {
			this.onTimer();
		} catch (Throwable e) {
			String remark = "AfTimerTask("+getClass().getName()+").run.onTimer";
			AfExceptionHandler.handler(e, remark);
		}
	}
}
