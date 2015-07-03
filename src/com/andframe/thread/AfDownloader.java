package com.andframe.thread;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;

import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfFileUtil;
import com.andframe.util.java.AfStringUtil;

/**
 * @Description: 下载器类
 * @Author: scwang
 * @Version: V1.0, 2015-3-13 下午5:27:48
 * @Modified: 初次创建AfDownloader类
 */
public class AfDownloader {
	/**
	 * 广播接收
	 */
	public static final String FILE_NOTIFICATION = "FILE_NOTIFICATION";

	/**
	 * @Description: 异步下载 url 到 path 文件名为 name
	 * @param url 下载链接
	 * @param path 下载目录
	 * @param name 下载文件名
	 */
	public static void download(String url,String path,String name) {
		// TODO Auto-generated method stub
		DownloadEntity entity = new DownloadEntity();
		entity.Name = name;
		entity.DownloadUrl = url;
		entity.DownloadPath = path;
		AfApplication.postTask(new DownloadTask(entity,null));
	}

	/**
	 * @Description: 异步下载 url 到 path
	 * @param url 下载链接
	 * @param path 下载路径
	 */
	public static void download(String url,String path) {
		// TODO Auto-generated method stub
		DownloadEntity entity = new DownloadEntity();
		entity.DownloadUrl = url;
		entity.DownloadPath = path;
		AfApplication.postTask(new DownloadTask(entity,null));
	}

	/**
	 * @Description: 异步下载 url 到 path
	 * @param url 下载链接
	 * @param path 下载路径
	 */
	public static void download(String url,String path,DownloadListener listener) {
		// TODO Auto-generated method stub
		DownloadEntity entity = new DownloadEntity();
		entity.DownloadUrl = url;
		entity.DownloadPath = path;
		AfApplication.postTask(new DownloadTask(entity,listener));
	}
	/**
	 * @Description: 异步下载
	 * @param entity 下载配置实体
	 */
	public static void download(DownloadEntity entity) {
		// TODO Auto-generated method stub
		AfApplication.postTask(new DownloadTask(entity,null));
	}

	/**
	 * @Description: 异步下载
	 * @param entity 下载配置实体
	 * @param listener 加载进度监听器
	 */
	public static void download(DownloadEntity entity,DownloadListener listener) {
		// TODO Auto-generated method stub
		AfApplication.postTask(new DownloadTask(entity,listener));
	}
	/**
	 * @Description: 判断 url是否正在下载
	 * @param url
	 * @return true 正在下载 false 没有在下载
	 */
	public static boolean isDownloading(DownloadEntity entity) {
		// TODO Auto-generated method stub
		return isDownloading(entity.DownloadUrl);
	}
	/**
	 * @Description: 判断 url是否正在下载
	 * @param url
	 * @return true 正在下载 false 没有在下载
	 */
	public static boolean isDownloading(String url) {
		// TODO Auto-generated method stub
//		for (DownloadTask task : DownloadTask.mltDownloading) {
			for (int i = 0; i < DownloadTask.mltDownloading.size(); i++) {
				DownloadTask task = DownloadTask.mltDownloading.get(i);
				
//			}
			if (AfStringUtil.equals(task.mEndityUrl, url)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * @Description: 判断 url是否正在下载
	 * @param tag 
	 * @return true 正在下载 false 没有在下载
	 */
	public static boolean isDownloading(Object tag) {
		// TODO Auto-generated method stub
//		for (DownloadTask task : DownloadTask.mltDownloading) {
			for (int i = 0; i < DownloadTask.mltDownloading.size(); i++) {
				DownloadTask task = DownloadTask.mltDownloading.get(i);
				
//			}
			if (task.mEndity.tag == tag) {
				return true;
			}
		}
		return false;
	}
	/**
	 * @Description: 新的绑定进度监听器
	 * @param entity 下载配置实体
	 * @param listener 加载进度监听器
	 * @return 返回最新的监听器 
	 * 如果等于 传入的监听器 绑定成功 否则 上一个监听器拒绝新的绑定
	 * 如果null 匹配不到下载任务
	 */
	public static DownloadListener setListener(DownloadEntity entity, DownloadListener listener) {
		// TODO Auto-generated method stub
		return setListener(entity.DownloadUrl, listener);
	}
	/**
	 * @Description: 新的绑定进度监听器
	 * @param url 下载路劲
	 * @param listener 加载进度监听器
	 * @return 返回最新的监听器 
	 * 如果等于 传入的监听器 绑定成功 否则 上一个监听器拒绝新的绑定
	 * 如果null 匹配不到下载任务
	 */
	public static DownloadListener setListener(String url, DownloadListener listener) {
		// TODO Auto-generated method stub
//		for (DownloadTask task : DownloadTask.mltDownloading) {
			for (int i = 0; i < DownloadTask.mltDownloading.size(); i++) {
				DownloadTask task = DownloadTask.mltDownloading.get(i);
				
//			}
			if (listener != null && AfStringUtil.equals(task.mEndityUrl, url)) {
				DownloadListener cListener = task.setDownloadListener(listener);
				if (cListener != listener && cListener instanceof DownloadManagerListener
						&& listener instanceof DownloadViewerListener) {
					DownloadViewerListener vListener = (DownloadViewerListener)listener;
					DownloadManagerListener mListener = (DownloadManagerListener)cListener;
					if (mListener.setDownloadListener(vListener)) {
						return vListener;
					}
				}
				return cListener;
			}
		}
		return null;
	}
	/**
	 * @Description: 任务栏配置
	 */
	public static class NotifyEntity{

		public String ContentTitle;
		public String FinishText;
		private String FailText;
		/**
		 * @return "附件下载XXXX"
		 */
		public String getContentTitle() {
			// TODO Auto-generated method stub
			if (ContentTitle == null) {
				return "文件下载";
			}
			return ContentTitle;
		}
		/**
		 * @return "文件XXXXX下载完成,大小XXXX"
		 */
		public String getFinishText() {
			// TODO Auto-generated method stub
			if (FinishText == null) {
				return "文件下载完成";
			}
			return FinishText;
		}
		/**
		 * @return "文件XXXXX下载失败"
		 */
		public String getFailText() {
			// TODO Auto-generated method stub
			if (FinishText == null) {
				return "文件下载失败";
			}
			return FailText;
		}
		
	}
	
	public static class DownloadEntity{
		/**
		 * 下载后的文件名，影响 DownloadPath的意义
		 */
		public String Name = "";
		public String Size = "";
		public String DownloadUrl = "";
		/**
		 * 当 Name 有值时候 表示目录,否则全路径 
		 */
		public String DownloadPath = "";
		/**
		 * 任务栏下载通知配置
		 */
		public NotifyEntity Notify;
		/**
		 * 额外绑定项
		 */
		public Object tag;
		
		private boolean isDownloaded = false;
		
		String Error = "";
		Throwable Excption = null;
		
		public boolean isDownloaded() {
			// TODO Auto-generated method stub
			return isDownloaded;
		}
		
		void setDownloaded() {
			// TODO Auto-generated method stub
			isDownloaded = true;
		}
		/**
		 * 判断是否下载失败
		 */
		public boolean isDownloadFail(){
			return Excption != null;
		}
		/**
		 * 获取错误信息
		 */
		public String getError() {
			return Error;
		}
		/**
		 * 获取异常信息
		 */
		public Throwable getExcption() {
			return Excption;
		}
		/**
		 * 获取下载目录
		 */
		public String getDir(){
			if (AfStringUtil.isNotEmpty(Name)) {
				return DownloadPath;
			}else {
				return new File(DownloadPath).getParent();
			}
		}
		/**
		 * 获取下载全路径
		 */
		public String getFullPath(){
			if (AfStringUtil.isNotEmpty(Name)) {
				return DownloadPath +"/"+Name;
			}else {
				return DownloadPath;
			}
		}
	}
	
	public interface DownloadListener{
		/**
		 * @Description 下载开始
		 */
		void onDownloadStart();
		/**
		 * @Description 接口脱离
		 * @return false 拒绝脱离 true 同意脱离
		 */
		boolean onBreakAway();
		/**
		 * @Description: 下载进度
		 * @param rate 百分比
		 * @param loaded 已下载长度
		 * @param total 文件总大小
		 */
		void onDownloadProgress(float rate, long loaded, long total);
		/**
		 * @Description: 通知栏点击事件
		 * @param endity 下载实体描述
		 */
		void notifyClick(DownloadEntity endity);
		/**
		 * @Description: 下载完成
		 * @return true 已经处理 false 没有处理（影响到notifyClick）
		 */
		boolean onDownloadFinish();
		/**
		 * @Description: 下载失败
		 * @param error 错误信息
		 * @param e 错误异常
		 * @return true 已经处理 false 没有处理（影响到notifyClick）
		 */
		boolean onDownloadFail(String error, Throwable e);
	}
	/**
	 * @Description: 常用监听接口
	 */
	public static abstract class DownloadViewerListener implements DownloadListener{

		@Override
		public void onDownloadStart() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onBreakAway() {
			// TODO Auto-generated method stub
			return true;//同意脱离
		}

		@Override
		public void notifyClick(DownloadEntity endity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onDownloadFinish() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDownloadFail(String error, Throwable e) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	/**
	 * 下载管理监听接口
	 */
	public static abstract class DownloadManagerListener implements DownloadListener{

		protected DownloadViewerListener listener;

		public DownloadManagerListener() {
			// TODO Auto-generated constructor stub
		}
		
		public DownloadManagerListener(DownloadViewerListener listener) {
			super();
			this.listener = listener;
		}

		@Override
		public void onDownloadStart() {
			// TODO Auto-generated method stub
			if (listener != null) {
				listener.onDownloadStart();
			}
		}

		@Override
		public boolean onBreakAway() {
			// TODO Auto-generated method stub
			return false;//同意脱离
		}

		@Override
		public void onDownloadProgress(float rate, long loaded, long total) {
			// TODO Auto-generated method stub
			if (listener != null) {
				listener.onDownloadProgress(rate, loaded, total);
			}
		}

		@Override
		public boolean onDownloadFinish() {
			// TODO Auto-generated method stub
			if (listener != null) {
				return listener.onDownloadFinish();
			}
			return false;
		}

		@Override
		public boolean onDownloadFail(String error, Throwable e) {
			// TODO Auto-generated method stub
			if (listener != null) {
				return listener.onDownloadFail(error,e);
			}
			return false;
		}
		/**
		 * 绑定新的监听器
		 * @return true 绑定成功 false 上一个监听器 拒绝
		 */
		public boolean setDownloadListener(DownloadViewerListener listener) {
			// TODO Auto-generated method stub
			if (this.listener == null || this.listener.onBreakAway()) {
				this.listener = listener;
				return true;
			}
			return false;
		}
	}
	
	protected static class Notifier{
		
		private static Random rand = new Random();

		private Builder mBuilder;
		private NotifyEntity mEotify;
		private NotificationManager mManager;
		private int notifyid = 1000 + rand.nextInt(1000);
		
		public Notifier(Context context, DownloadEntity entity) {
			// TODO Auto-generated constructor stub
			mEotify = entity.Notify;
			mBuilder = new Builder(context);
			mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);

//			if (Build.VERSION.SDK_INT < 11) {
				int flag = PendingIntent.FLAG_CANCEL_CURRENT;
				AfIntent intent = new AfIntent(FILE_NOTIFICATION);
				intent.put(FILE_NOTIFICATION, entity.DownloadUrl);
				PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, flag);
				mBuilder.setContentIntent(pintent);
//			}

			// 构造 Manager
			String server = Context.NOTIFICATION_SERVICE;
			mManager = (NotificationManager) context.getSystemService(server);
		}

		public void notifyProgress(int max, int precent, boolean indeterminate) {
			// TODO Auto-generated method stub
			mBuilder.setProgress(max, precent, false);// 设置为false，表示刻度
			mBuilder.setContentText("已下载 "+precent+"% ");
			mManager.notify(notifyid, mBuilder.build());
		}

		public void notifyStart() {
			// TODO Auto-generated method stub
			mBuilder.setContentTitle(mEotify.getContentTitle());
			mBuilder.setTicker(mEotify.getContentTitle());
//			mBuilder.setTicker("正在下载...");
			mBuilder.setAutoCancel(false);
			mBuilder.setOngoing(true);
			mManager.notify(notifyid, mBuilder.build());
		}

		public void notifyFinish() {
			// TODO Auto-generated method stub
			mBuilder.setProgress(100, 100, false);// 设置为false，表示刻度
			mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
			mBuilder.setTicker(mEotify.getFinishText());
			mBuilder.setContentText(mEotify.getFinishText());
//			mBuilder.setContentText("文件下载完成，大小"+mBack.Size);
//			mBuilder.setTicker("背景下载完成，点击设置");
			mBuilder.setAutoCancel(true);
			mBuilder.setOngoing(false);
			
			mManager.notify(notifyid, mBuilder.build());
		}

		public void notifyFail() {
			// TODO Auto-generated method stub
			mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
			mBuilder.setTicker(mEotify.getFailText());
			mBuilder.setContentText(mEotify.getFailText());
			mBuilder.setAutoCancel(true);
			mBuilder.setOngoing(false);
			mManager.notify(notifyid, mBuilder.build());
		}

		public void cancel() {
			// TODO Auto-generated method stub
			mManager.cancel(notifyid);
		}
		
	}
	
	public static class DownloadTask extends AfHandlerTask{

		public static final int DOWNLOAD_FINISH = 20;
		public static final int DOWNLOAD_PROGRESS = 10;
		
		private DownloadEntity mEndity;
		private Notifier mNotifier;
		private String mEndityUrl;
		private String mDownloadPath;
		private DownloadListener mListener;
		private File mTempFile;
		private int mPrecent = 0;
		
		private long mCount = 0;
		private long mTotal = 0;
		/**
		 * 正在下载任务列表
		 */
		private static List<DownloadTask> mltDownloading = new ArrayList<DownloadTask>();
		/**
		 * 成功或者失败 通知栏任务列表
		 */
		private static Map<String,DownloadTask> mNotifyMap = new HashMap<String, DownloadTask>();

		public DownloadTask(DownloadEntity endity,DownloadListener listener) {
			// TODO Auto-generated constructor stub
			mEndity = endity;
			mListener = listener;
			mEndityUrl = endity.DownloadUrl;
			mDownloadPath = endity.getFullPath();
		}
		/**
		 * @Description: 重新绑定监听器
		 * @param listener 新的监听器
		 * @return 返回最新的监听器 
		 * 如果等于 传入的监听器 绑定成功 否则 上一个监听器拒绝新的绑定
		 */
		public DownloadListener setDownloadListener(DownloadListener listener) {
			// TODO Auto-generated method stub
			if (mListener == null || mListener.onBreakAway()) {
				mListener = listener;
				if (mListener != null) {
					mListener.onDownloadStart();
				}
			}
			return mListener;
		}

		@Override
		public boolean onPrepare() {
			// TODO Auto-generated method stub
			mltDownloading.add(this);
			// 构造 通知
			if (mEndity.Notify != null) {
				mNotifier = new Notifier(AfApplication.getApp(),mEndity);
				mNotifier.notifyStart();
			}
			if (mListener != null) {
				mListener.onDownloadStart();
			}
			return super.onPrepare();
		}
		
		@Override
		protected void onWorking(Message msg) throws Exception {
			// TODO Auto-generated method stub
			if (!mEndity.isDownloaded()) {
				
				HttpGet get = new HttpGet(mEndityUrl);
				HttpResponse response = new DefaultHttpClient().execute(get);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();

				//创建文件并开始下载
				mTempFile = new File(mDownloadPath);
				if(mTempFile.exists()){
					mTempFile.delete();
				}else{
					String spath = mTempFile.getParent();
					if(spath != null){
						File path = new File(spath);
						if (!path.exists()) {
							path.mkdir();
						}
					}
				}
				mTempFile.createNewFile();
				
				// 创建一个新的写入流，讲读取到的图像数据写入到文件中
				FileOutputStream fos = new FileOutputStream(mTempFile);
				// 已读出流作为参数创建一个带有缓冲的输出流
				BufferedInputStream bis = new BufferedInputStream(is);
				// 已写入流作为参数创建一个带有缓冲的写入流
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				int read = 0, precent = 0;
				byte[] buffer = new byte[1024];
				long now, last = System.currentTimeMillis();
				long count = 0, length = entity.getContentLength();
				mEndity.Size = AfFileUtil.getFileSize(length);
				mCount = count;
				mTotal = length;
				mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_PROGRESS, this));
				while ((read = bis.read(buffer)) != -1 && !mIsCanceled) {
					count += read;
					bos.write(buffer, 0, read);
					precent = (int) (((double) count / length) * 100);
					// 每下载完成3%就通知任务栏进行修改下载进度
					now = System.currentTimeMillis();
					if (precent - mPrecent >= 3 || now - last > 500) {
						last = now;
						mPrecent = precent;
						mCount = count;
						mTotal = length;
						mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_PROGRESS, this));
					}
				}
				bos.flush();
				fos.flush();
				bos.close();
				fos.close();
				bis.close();
				is.close();
				
				if (mIsCanceled) {
					mTempFile.delete();
				}else{
					mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINISH, this));
				}
				mIsCanceled = true;
			}else {
				mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINISH, this));
			}
		}

		@Override
		protected boolean onHandle(Message msg) {
			// TODO Auto-generated method stub
			if (mResult == AfTask.RESULT_FINISH) {
				if (msg.what == DOWNLOAD_PROGRESS) {
					// 更新状态栏上的下载进度信息
					if (mNotifier != null) {
						mNotifier.notifyProgress(100, mPrecent, false);
					}
					if (mListener != null) {
						mListener.onDownloadProgress(0.01f*mPrecent, mCount, mTotal);
					}
				} else if (msg.what == DOWNLOAD_FINISH) {
					mEndity.setDownloaded();
					mltDownloading.remove(this);
					boolean neednotify = true;
					if (mListener != null && mListener.onDownloadFinish()) {
						neednotify = false;
					}
					if (mNotifier != null) {
						if (neednotify) {
							mNotifier.notifyFinish();
							mNotifyMap.put(mEndityUrl, this);
						}else {
							mNotifier.cancel();
						}
					}
				}
			} else if (mResult == AfTask.RESULT_FAIL) {
				mltDownloading.remove(this);
				boolean neednotify = true;
				if (mListener != null && mListener.onDownloadFail(mErrors, mException)) {
					neednotify = false;
				}
				if (mNotifier != null) {
					if (neednotify) {
						mEndity.Error = mErrors;
						mEndity.Excption = mException;
						mNotifier.notifyFail();
						mNotifyMap.put(mEndityUrl, this);
					}else {
						mNotifier.cancel();
					}
				}
			}
			return true;
		}

		public void notifyClick() {
			// TODO Auto-generated method stub
			if (mListener != null) {
				mListener.notifyClick(mEndity);
			}
		}

	}

	protected static class DownloadBroadcast extends BroadcastReceiver {
		
		// 该方法用于实现接收到广播的具体处理，其中参数intent：为接受到的intent
		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取意图的动作
			try {
				if (FILE_NOTIFICATION.equals(intent.getAction())) {
					String Url = new AfIntent(intent).getString(FILE_NOTIFICATION, null);
					DownloadTask task = DownloadTask.mNotifyMap.get(Url);
					if (task != null) {
						task.notifyClick();
						DownloadTask.mNotifyMap.remove(Url);
					}
//					for (DownloadTask task : DownloadTask.mltDownloading) {
//						if (AfStringUtil.equals(task.mEndityUrl, Url)) {
//							task.notifyClick();
//							break;
//						}
//					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				AfExceptionHandler.handler(e, "DownloadBroadcast.onReceive");
			}
		}
	}
	
	static{
		try {
			IntentFilter filter = new IntentFilter();
			filter.addAction(FILE_NOTIFICATION);
			DownloadBroadcast receiver = new DownloadBroadcast();
			AfApplication.getApp().registerReceiver(receiver, filter);
		} catch (Throwable e) {
			// TODO: handle exception
			AfExceptionHandler.handleAttach(e, "DownloadBroadcast.registerReceiver error");
		}
	}
}
