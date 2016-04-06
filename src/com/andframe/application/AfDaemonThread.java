package com.andframe.application;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

import com.andframe.thread.AfDispatch;
import com.andframe.thread.AfTask;
import com.andframe.util.java.AfDateGuid;

/**
 * DaemonThread 后台线程管理
 * @author 树朾
 */
public class AfDaemonThread {
	// 定义运行的最大线程数量
	private static final int MAXTHREADNUM = 5;
	// 定义等待任务队列
	private static Queue<AfTask> mqeTask = new LinkedList<AfTask>();
	// 定义线程列表容器
	private static List<ThreadWorker> mltWorker = new ArrayList<ThreadWorker>();

	/**
	 * 定时往后台抛送一个 Task 任务（会在UI线程中dispatch，任务开始）
	 * @author 树朾
	 * @param task 任务
	 * @return 返回传入的  task 任务对象
	 */
	public static AfTask postTask(AfTask task) {
		return postTaskDelayed(task, 0);
	}
	
	/**
	 * 定时往后台抛送一个 Task 任务
	 * 不会会在UI线程中dispatch，用于UI线程死亡的时候
	 * 会影响到 AfTask.onPrepare 的线程调用
	 * @author 树朾
	 * @param task 任务
	 * @return 返回传入的  task 任务对象
	 */
	public static AfTask postTaskNoDispatch(AfTask task) {
		return postTaskDelayedNoDispatch(task, 0);
	}

	/**
	 * 定时往后台抛送一个 Task 任务
	 * 会在UI线程中dispatch，任务开始
	 * AfTask.onPrepare 会在UI线程中调用
	 * @author 树朾
	 * @param task 任务
	 * @param delay 延时执行
	 * @return 返回传入的  task 任务对象
	 */
	public static AfTask postTaskDelayed(final AfTask task,final long delay) {
		AfApplication.dispatch(new AfDispatch() {
			@Override
			protected void onDispatch() {
				synchronized (mltWorker) {
					if (mltWorker.size() < MAXTHREADNUM) {
						if (!task.isCanceled() && task.onPrepare()) {
							ThreadWorker tWorker = new ThreadWorker(AfDateGuid.NewID(),task,delay);
							mltWorker.add(tWorker);
							tWorker.start();
						}
						return;
					}
				}
				synchronized (mqeTask) {
					mqeTask.offer(task);
				}
			}
		});
		return task;
	}
	
	/**
	 * 定时往后台抛送一个 Task 任务
	 * 不会会在UI线程中dispatch，用于UI线程死亡的时候
	 * 会影响到 AfTask.onPrepare 的线程调用
	 * @author 树朾
	 * @param task 任务
	 * @param delay 延时执行
	 * @return 返回传入的  task 任务对象
	 */
	public static AfTask postTaskDelayedNoDispatch(final AfTask task,final long delay) {
		synchronized (mltWorker) {
			if (mltWorker.size() < MAXTHREADNUM) {
				if (!task.isCanceled() && task.onPrepare()) {
					ThreadWorker tWorker = new ThreadWorker(AfDateGuid.NewID(),task,delay);
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
				}
			}
			try {
				mTask.run();
			} catch (Throwable e) {
				AfExceptionHandler.handler(e, "AfDaemonThread.ThreadWorker.Task.run");
			}
			onTaskFinish(this);
		}

	}
}
