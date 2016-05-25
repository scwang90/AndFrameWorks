package com.andframe.thread;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.exception.AfException;
import com.andframe.exception.AfToastException;

public abstract class AfTask implements Runnable, OnCancelListener {

	// Task 执行状态枚举
	public static final int RESULT_FAIL = 0;
	public static final int RESULT_FINISH = 1;
	// Task 执行类型枚举
	public static final int TASK_LOAD = 0; // 第一次加载数据

	//	public int mTask = -1;
	public int mResult = -1;
	public String mErrors;
	public Throwable mException = new AfException();

	//protected Handler mHandler;
	protected boolean mIsCanceled = false;

	protected abstract void onWorking(/*Message msg*/) throws Exception;

	public AfTask() {
	}

//	protected AfTask(Handler handler) {
//		this.mHandler = handler;
//	}
//
//	protected AfTask(Handler handler, int task) {
//		this.mTask = task;
//		this.mHandler = handler;
//	}

	public boolean isFinish() {
		return mResult == RESULT_FINISH;
	}

	public boolean isFail() {
		return mResult == RESULT_FAIL;
	}

	@Override
	public void run() {
		//Message tMessage = Message.obtain();
		try {
			/*tMessage.what = */mResult = RESULT_FINISH;
			if (!mIsCanceled) {
				this.onWorking(/*tMessage*/);
			}
		} catch (Throwable e) {
			mException = e;
			mErrors = e.getMessage();
			/*tMessage.what = */mResult = RESULT_FAIL;
			if (mErrors == null || mErrors.length() == 0) {
				mErrors = e.toString();
			}
			if (!mIsCanceled) {
				this.onException(e);
			}
		}
//		if (!mIsCanceled && mHandler != null) {
//			tMessage.obj = this;
//			mHandler.sendMessage(tMessage);
//		}
	}

	public boolean isCanceled() {
		return mIsCanceled;
	}

	@Override
	public final void onCancel(DialogInterface dialog) {
		mIsCanceled = true;
		this.onCancel();
	}

	/**
	 * Task任务执行过程中捕捉到的异常，并对异常信息做处理
	 * 	之后 isFinish() 将会返回 false
	 * @param e
	 */
	protected void onException(Throwable e) {
		if (AfApplication.getApp() != null && AfApplication.getApp().isDebug() && !(e instanceof AfToastException)) {
			e.printStackTrace();
		}
	}

	/**
	 * Task任务被取消 将不会被调用 mHandler
	 * 	这个方法可能在异步线程中执行
	 */
	protected void onCancel() {
	}

	protected Boolean mPrepare = null;
	public boolean prepare() {
		if (mPrepare == null) {
			try {
				mPrepare = onPrepare();
			} catch (Throwable e) {
				e.printStackTrace();
				String remark = "AfTask("+getClass().getName()+").onPrepare";
				AfExceptionHandler.handler(e, remark);
				return false;
			}
		}
		return mPrepare;
	}
	/**
	 * 任务准备开始 （在UI线程中）
	 * @return 返回true 表示准备完毕 否则 false 任务将被取消
	 */
	protected boolean onPrepare() {
		return !mIsCanceled;
	}

	public String makeErrorToast(String tip) {
		return AfException.handle(mException, tip);
	}

//	public static AfTask getTask(Message msg) {
//		if (msg.obj instanceof AfTask) {
//			return (AfTask) msg.obj;
//		}
//		return null;
//	}
//
//	public static <T extends AfTask> T getTask(Message msg,Class<T> clazz) {
//		if (clazz.isInstance(msg.obj)) {
//			return clazz.cast(msg.obj);
//		}
//		return null;
//	}
//
//	public static Message putTask(AfTask task) {
//		Message msg = Message.obtain();
//		msg.what = task.mResult;
//		msg.obj = task;
//		return msg;
//	}

	public AfTask reset() {
		mPrepare = null;
		mIsCanceled = false;
		return this;
	}
}
