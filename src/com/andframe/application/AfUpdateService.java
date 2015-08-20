package com.andframe.application;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;

import com.andframe.activity.framework.AfActivity;
import com.andframe.exception.AfException;
import com.andframe.feature.AfIntent;
import com.andframe.thread.AfHandlerTask;
import com.andframe.thread.AfTask;

@SuppressWarnings("deprecation")
public class AfUpdateService implements Callback {
	
	protected Builder mBuilder;
	protected NotificationManager mManager;

	protected Context mContext = null;

	protected int notificationId = 1234;
	
	protected static AfUpdateService mInstance;

	public static void initialize(Context context){
		if(mInstance == null){
			mInstance = AfApplication.getApp().getUpdateService();
		}
	}

	public static AfUpdateService getInstance(){
		return mInstance;
	}

	public AfUpdateService(Context context) {
		mContext = context;
		// 设置任务栏中下载进程显示的views
		AfApplication app = AfApplication.getApp();

		mBuilder = new Builder(context);
		mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
		mBuilder.setContentTitle(app.getAppName()+"更新");
		mBuilder.setTicker(app.getAppName()+"正在更新...");
		mBuilder.setAutoCancel(false);
		mBuilder.setOngoing(true);

		if (Build.VERSION.SDK_INT < 11) {
			int flag = PendingIntent.FLAG_CANCEL_CURRENT;
			AfIntent intent = new AfIntent("FILE_NOTIFICATION");
			PendingIntent pintent = PendingIntent.getBroadcast(app, 0, intent, flag);
			mBuilder.setContentIntent(pintent);
		}
	}

	private void start(Context context, String url, String version) {
		String path = AfApplication.getApp().getWorkspacePath("update");
		AfDaemonThread.postTask(new DownloadTask(url, path, version + ".apk"));

		String server = Context.NOTIFICATION_SERVICE;
		mManager = (NotificationManager) context.getSystemService(server);
		mBuilder.setProgress(0, 0, true);// 设置为true，表示流动
		mBuilder.setContentText("已下载 0% ");
		mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
		mManager.notify(notificationId, mBuilder.build());
	}

	// 安装下载后的apk文件
	protected static void Instanll(File file, Context context) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		} catch (Throwable e) {
			e.printStackTrace();// handled
			AfExceptionHandler.handler(e, "更新服务，Instanll 调用安装失败");
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		DownloadTask task = (DownloadTask) msg.obj;
		if (task.mResult == AfTask.RESULT_FINISH) {
			if (task.mTask == DownloadTask.TASK_UPDATEPROGRESS) {
				// 更新状态栏上的下载进度信息
				mBuilder.setProgress(100, task.mPrecent, false);// 设置为false，表示刻度
				mBuilder.setContentText("已下载 "+task.mPrecent+"% ");
				mManager.notify(notificationId, mBuilder.build());
			} else if (task.mTask == DownloadTask.TASK_DOWNLOADFINISH) {
				mManager.cancel(notificationId);
				Instanll(task.mTempFile, mContext);
			}
		} else if (task.mResult == AfTask.RESULT_FAIL) {
			mManager.cancel(notificationId);
		}
		return false;
	}

	public static void checkUpdate() {
		AfDaemonThread.postTask(new CheckUpdateTask(false));
	}

	public static void checkUpdate(boolean feedback) {
		AfDaemonThread.postTask(new CheckUpdateTask(feedback));
	}

	public String getApkUpdateDescribe(String verson) throws Exception {
		return "";
	}

	public String getLastApkVersion() throws Exception{
		return "0.0.0.0";
	}

	public String getApkUrl(String serversion) {
		return "";
	}
	
	public String getDownloadUrl(){
		return "";
	}
	
	public static class CheckUpdateTask extends AfHandlerTask implements
			OnClickListener {

		public static String mServersion = "0.0.0.0";
		private Boolean ndFeedback = false;
		private String mDscribe = "";

		public CheckUpdateTask(boolean feedback) {
			ndFeedback = feedback;
		}

		@Override
		protected void onWorking(Message tMessage) throws Exception {
//			if (mServersion.equals("0.0.0.0")) {
				String version = AfApplication.getVersion();
				mServersion = mInstance.getLastApkVersion();
				mDscribe = mInstance.getApkUpdateDescribe(version);
//			}
		}

		@Override
		protected boolean onHandle(Message msg) {
			AfApplication app = AfApplication.getApp();
			if (mResult == AfTask.RESULT_FINISH) {
				app.setServerVersion(this,mServersion,mDscribe);
			}
			AfActivity activity = app.getCurActivity();
			if (ndFeedback && !app.isNeedUpdate() && activity!=null) {
				activity.makeToastShort("恭喜你，目前已经是最新版本！");
			}
			return false;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String url = mInstance.getApkUrl(mServersion);
			startDownLoadUpate(AfApplication.getApp(), url, mServersion);
		}
	}

	public static void startDownLoadUpate(Context context, String url,
			String version) {
		String path = AfApplication.getApp().getWorkspacePath("update");
		File upadate = new File(path + "/" + version + ".apk");
		try {
			int length = getContentLengthFormUrl(url);
			if (upadate.exists() && upadate.length() != length) {
				upadate.delete();
			}
		} catch (Throwable e) {
			AfApplication app = AfApplication.getApp();
			AfActivity activity = app.getCurActivity();
			if (activity != null) {
				activity.makeToastShort(AfException.handle(e, "下载更新出错"));
			}
			return;
		}
		if (upadate.exists() == false) {
			mInstance.start(context, url, version);
		} else {
			Instanll(upadate, context);
		}
	}

	/**
	 * 描述：获取网络文件的大小.
	 *
	 * @param Url 图片的网络路径
	 * @return int 网络文件的大小
	 */
	public static int getContentLengthFormUrl(String Url){
		int mContentLength = 0;
		try {
			 URL url = new URL(Url);
			 HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
			 mHttpURLConnection.setConnectTimeout(5 * 1000);
			 mHttpURLConnection.setRequestMethod("GET");
			 mHttpURLConnection.setRequestProperty("Accept","image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			 mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			 mHttpURLConnection.setRequestProperty("Referer", Url);
			 mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			 mHttpURLConnection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			 mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			 mHttpURLConnection.connect();
			 if (mHttpURLConnection.getResponseCode() == 200){
				 // 根据响应获取文件大小
				 mContentLength = mHttpURLConnection.getContentLength();
			 }
	    } catch (Throwable e) {
	    	 e.printStackTrace();
		}
		return mContentLength;
	}
	
	private class DownloadTask extends AfTask {
		private static final int TASK_UPDATEPROGRESS = 1;
		private static final int TASK_DOWNLOADFINISH = 2;

		public int mPrecent = 0;
		public File mTempFile = null;
		public String mDownloadUrl = null;
		public String mDownloadPath = null;
		public String mDownloadFile = null;

		public DownloadTask(String url, String path, String file) {
			super(new Handler(AfUpdateService.this));
			mDownloadUrl = url;
			mDownloadPath = path;
			mDownloadFile = file;
		}

		@Override
		protected void onWorking(Message tMessage) throws Exception {
			mTask = TASK_UPDATEPROGRESS;
			mHandler.sendMessage(mHandler.obtainMessage(mResult, this));

			HttpGet get = new HttpGet(mDownloadUrl);
			HttpResponse response = new DefaultHttpClient().execute(get);
			HttpEntity entity = response.getEntity();

			InputStream is = entity.getContent();
			File path = new File(mDownloadPath);
			if (!path.exists()) {
				path.mkdir();
			}

			mTempFile = new File(path, mDownloadFile);
			if (mTempFile.exists()) {
				mTempFile.delete();
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
			long count = 0, length = entity.getContentLength();
			while ((read = bis.read(buffer)) != -1 && !mIsCanceled) {
				count += read;
				bos.write(buffer, 0, read);
				precent = (int) (((double) count / length) * 100);

				// 每下载完成5%就通知任务栏进行修改下载进度
				if (precent - mPrecent >= 5) {
					mPrecent = precent;
					mHandler.sendMessage(mHandler.obtainMessage(mResult, this));
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
			}
			mTask = TASK_DOWNLOADFINISH;
		}

	}

}
