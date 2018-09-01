package com.andframe.task;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.andframe.api.task.Task;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.exception.AfToastException;

@SuppressWarnings("unused")
public abstract class AfTask implements Task, OnCancelListener {

	public Status mStatus = Status.none;
	public String mErrors;
	public Throwable mException;

	protected abstract void onWorking() throws Exception;

	public boolean success() {
		return mStatus == Status.success;
	}

	public boolean isFail() {
		return mStatus == Status.failed;
	}

	public boolean isCanceled() {
		return mStatus == Status.canceled;
	}

	@Override
	public Throwable exception() {
		return mException;
	}

	@Override
	public Status status() {
		return mStatus;
	}

	@Override
	public void run() {
		try {
			if (mStatus == Status.prepared) {
				mStatus = Status.running;
				this.onWorking();
				mStatus = Status.success;
			}
		} catch (Throwable e) {
			mException = e;
			mErrors = e.getMessage();
			if (mErrors == null || mErrors.length() == 0) {
				mErrors = e.toString();
			}
			if (mStatus == Status.running) {
				mStatus = Status.failed;
				this.onException(e);
			}
		}
	}

	@Override
	public final void onCancel(DialogInterface dialog) {
		this.onCancel();
	}

	@Override
	public void cancel() {
		this.onCancel();
	}

	/**
	 * Task任务执行过程中捕捉到的异常，并对异常信息做处理
	 * 	之后 success() 将会返回 false
	 */
	protected void onException(Throwable e) {
		if (AfApp.get().isDebug() && !(e instanceof AfToastException)) {
			e.printStackTrace();
		}
	}

	/**
	 * Task任务被取消 将不会被调用 mHandler
	 * 	这个方法可能在异步线程中执行
	 */
	public void onCancel() {
		mStatus = Status.canceled;
	}

	public boolean prepare() {
		if (mStatus == Status.none) {
			try {
				mStatus = onPrepare() ? Status.prepared : Status.canceled;
			} catch (Throwable e) {
				mStatus = Status.canceled;
				String remark = "AfTask("+getClass().getName()+").onPrepare";
				AfExceptionHandler.handle(e, remark);
				return false;
			}
		}
		return mStatus == Status.prepared;
	}
	/**
	 * 任务准备开始 （在UI线程中）
	 * @return 返回true 表示准备完毕 否则 false 任务将被取消
	 */
	protected boolean onPrepare() {
		return mStatus == Status.none;
	}

	public String makeErrorToast(String tip) {
		return AfExceptionHandler.tip(mException, tip);
	}

	public AfTask reset() {
		mStatus = Status.none;
		mException = null;
		return this;
	}
}
