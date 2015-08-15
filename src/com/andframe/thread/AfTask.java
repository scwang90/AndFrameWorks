package com.andframe.thread;

import com.andframe.exception.AfException;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;

public abstract class AfTask implements Runnable, OnCancelListener {

	// Task 执行状态枚举
	public static final int RESULT_FAIL = 0;
	public static final int RESULT_FINISH = 1;
	// Task 执行类型枚举
	public static final int TASK_LOAD = 0; // 第一次加载数据

	public int mTask = -1;
	public int mResult = -1;
	public String mErrors;
	public Throwable mException = new AfException();

	protected Handler mHandler;
	protected boolean mIsCanceled = false;

	protected abstract void onWorking(Message msg) throws Exception;

	public AfTask() {
		// TODO Auto-generated constructor stub
	}
	
	protected AfTask(Handler handler) {
		// TODO Auto-generated constructor stub
		this.mHandler = handler;
	}

	protected AfTask(Handler handler, int task) {
		// TODO Auto-generated constructor stub
		this.mTask = task;
		this.mHandler = handler;
	}

	public boolean isFinish() {
		// TODO Auto-generated method stub
		return mResult == RESULT_FINISH;
	}

	public boolean isFail() {
		// TODO Auto-generated method stub
		return mResult == RESULT_FAIL;
	}
	
	@Override
	public final void run() {
		// TODO Auto-generated method stub
		Message tMessage = Message.obtain();
		try {
			tMessage.what = mResult = RESULT_FINISH;
			if (!mIsCanceled) {
				this.onWorking(tMessage);
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			mException = e;
			mErrors = e.getMessage();
			tMessage.what = mResult = RESULT_FAIL;
			if (mErrors == null || mErrors.length() == 0) {
				mErrors = e.toString();
			}
			if (!mIsCanceled) {
				this.onException(e);
			}
		}
		if (!mIsCanceled && mHandler != null) {
			tMessage.obj = this;
			mHandler.sendMessage(tMessage);
		}
	}

	@Override
	public final void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		mIsCanceled = true;
		this.onCancel();
	}

	/**
	 * Task任务执行过程中捕捉到的异常，并对异常信息做处理
	 * 	之后 isFinish() 将会返回 false
	 * @param e
	 */
	protected void onException(Throwable e) {
		// TODO Auto-generated method stub
	}

	/**
	 * Task任务被取消 将不会被调用 mHandler
	 * 	这个方法可能在异步线程中执行
	 */
	protected void onCancel() {
		// TODO Auto-generated method stub
	}

	/**
	 * 任务准备开始 （在UI线程中）
	 * @return 返回true 表示准备完毕 否则 false 任务将被取消
	 */
	public boolean onPrepare() {
		// TODO Auto-generated method stub
		return true;
	}

	public String makeErrorToast(String tip) {
		// TODO Auto-generated method stub
		return AfException.handle(mException, tip);
	}
	
	public static AfTask getTask(Message msg) {
		// TODO Auto-generated method stub
		if (msg.obj instanceof AfTask) {
			return (AfTask) msg.obj;
		}
		return null;
	}
	
	public static <T extends AfTask> T getTask(Message msg,Class<T> clazz) {
		// TODO Auto-generated method stub
		if (clazz.isInstance(msg.obj)) {
			return clazz.cast(msg.obj);
		}
		return null;
	}

	public static Message putTask(AfTask task) {
		// TODO Auto-generated method stub
		Message msg = Message.obtain();
		msg.what = task.mResult;
		msg.obj = task;
		return msg;
	}

}
