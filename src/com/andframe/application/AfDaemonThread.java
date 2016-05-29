package com.andframe.application;

import android.os.Looper;

import com.andframe.thread.AfData2Task;
import com.andframe.thread.AfData3Task;
import com.andframe.thread.AfDataTask;
import com.andframe.thread.AfDispatch;
import com.andframe.thread.AfTask;
import com.andframe.util.java.AfDateGuid;
import com.andframe.util.java.AfReflecter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * DaemonThread 后台线程管理
 * @author 树朾
 */
public class AfDaemonThread {
	// 定义运行的最大线程数量
	private static final int MAXTHREADNUM = 5;
	// 定义等待任务队列
	private static final Queue<AfTask> mqeTask = new LinkedList<>();
	// 定义线程列表容器
	private static final List<ThreadWorker> mltWorker = new ArrayList<>();

	/**
	 * 定时往后台抛送一个 Task 任务（会在UI线程中dispatch，任务开始）
	 * @param task 任务
	 * @return 返回传入的  task 任务对象
	 */
	public static <T extends AfTask> T postTask(T task) {
		return postTaskDelayed(task, 0);
	}

	/**
	 * 定时往后台抛送一个 Task 任务
	 * 不会会在UI线程中dispatch，用于UI线程死亡的时候
	 * 会影响到 AfTask.prepare 的线程调用
	 * @param task 任务
	 * @return 返回传入的  task 任务对象
	 */
	public static <T extends AfTask> T postTaskNoDispatch(T task) {
		return postTaskDelayedNoDispatch(task, 0);
	}

	/**
	 * 定时往后台抛送一个 Task 任务
	 * 会在UI线程中dispatch，任务开始
	 * AfTask.prepare 会在UI线程中调用
	 * @param task 任务
	 * @param delay 延时执行
	 * @return 返回传入的  task 任务对象
	 */
	public static <T extends AfTask> T postTaskDelayed(final T task,final long delay) {
		if (Looper.getMainLooper().equals(Looper.myLooper())) {
			postTaskDelayedNoDispatch(task, delay);
		} else {
			AfApplication.dispatch(new AfDispatch() {
				@Override
				protected void onDispatch() {
					postTaskDelayedNoDispatch(task, delay);
				}
			});
		}
		return task;
	}

	/**
	 * 定时往后台抛送一个 Task 任务
	 * 不会会在UI线程中dispatch，用于UI线程死亡的时候
	 * 会影响到 AfTask.prepare 的线程调用
	 * @param task 任务
	 * @param delay 延时执行
	 * @return 返回传入的  task 任务对象
	 */
	public static <T extends AfTask> T postTaskDelayedNoDispatch(T task,long delay) {
		synchronized (mltWorker) {
			if (mltWorker.size() < MAXTHREADNUM) {
				if (!task.isCanceled() && task.prepare()) {
					String name = AfDateGuid.NewID();
					if (AfApplication.getApp().isDebug()) {
						name = task.getClass().getSimpleName();
						if (task instanceof AfDataTask || task instanceof AfData2Task || task instanceof AfData3Task) {
							Object listener = AfReflecter.getMemberNoException(task, "listener");
							if (listener != null) {
								name = listener.getClass().getSimpleName();
							}
						}
					}
					ThreadWorker tWorker = new ThreadWorker("AfDaemonThread-" + name, task, delay);
					mltWorker.add(tWorker);
					tWorker.start();
				}
				return task;
			}
		}
		synchronized (mqeTask) {
			mqeTask.offer(task);
		}
		return task;
	}

	// 一个线程任务执行完成
	private static void onTaskFinish(ThreadWorker worker) {
		synchronized (mltWorker) {
			mltWorker.remove(worker);
		}
		synchronized (mqeTask) {
			if (mqeTask.size() > 0) {
				postTask(mqeTask.poll());
			}
		}
	}

	private static class ThreadWorker extends Thread {
		private long mDelay = 0;
		private Runnable mTask = null;

		public ThreadWorker(String name, Runnable task, long delay) {
			super(name);
			mTask = task;
			mDelay = delay;
		}

		@Override
		public void run() {
			if (mDelay > 0) {
				try {
					Thread.sleep(mDelay);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			try {
				mTask.run();
				onTaskFinish(this);
			} catch (Throwable e) {
				AfExceptionHandler.handler(e, "AfDaemonThread.ThreadWorker.run");
			}
		}

	}
}
