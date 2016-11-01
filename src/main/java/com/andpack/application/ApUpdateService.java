package com.andpack.application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.andframe.$;
import com.andframe.activity.AfActivity;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.feature.AfDialogBuilder;
import com.andframe.task.AfDataTask;
import com.andframe.task.AfDownloader;
import com.andframe.task.AfDownloader.DownloadEntity;

import java.io.File;

@SuppressWarnings("deprecation")
public abstract class ApUpdateService {

	protected static ApUpdateService mInstance;

	public static class ServiceVersionInfo {
		public String serviceVersion = "0.0.0.0";
		public String updateDscribe = "";
		public String downloadUrl = "";
	}
	private Context mContext;
	private String mVersion;
	private DownloadEntity mEntity;//已经下载完成的实体

	private ServiceVersionInfo mVersionInfo;
	private DownloadListener managerListener = null;
	public static ApUpdateService getInstance() {
		if (mInstance == null) {
			mInstance = ApApp.getApp().getUpdateService();
		}
		return mInstance;
	}
	public String getVersion() {
		return mVersion;
	}

	public String getServiceVersion() {
		if (mVersionInfo == null) {
			return "请先检查更新";
		}
		return mVersionInfo.serviceVersion;
	}

	public void checkUpdate() {
		checkUpdate(false);
	}

	public void checkUpdate(boolean feedback) {
		$.task().postTask(new AfDataTask<>(feedback, new CheckUpdateTask()));
	}

	public ApUpdateService(Context context) {
		mContext = context;
		mVersion = ApApp.getApp().getVersion();
	}

	private void start(String url, String version) {
		if (mEntity != null && mEntity.Name.startsWith(version)) {
			instanll(new File(mEntity.getFullPath()), mContext);
		} else {
			DownloadEntity entity = new DownloadEntity();
			entity.Name = version + ".apk";
			entity.DownloadUrl = url;
			entity.DownloadPath = AfApp.get().getExternalCacheDir("update").getAbsolutePath();
			entity.Notify = new AfDownloader.NotifyEntity();
			entity.Notify.ContentTitle = AfApp.get().getAppName() + "更新";
			entity.Notify.FailText = "更新下载失败";
			entity.Notify.FailText = "更新下载完成";
			AfDownloader.download(entity, managerListener = new DownloadListener());
		}
	}

	private class DownloadListener extends AfDownloader.DownloadManagerListener {
		@Override
		public void notifyClick(DownloadEntity entity) {
			mEntity = entity;
			clearListenerMark();
			instanll(new File(entity.getFullPath()), mContext);
		}

		@Override
		public boolean onDownloadFinish(DownloadEntity entity) {
			mEntity = entity;
			clearListenerMark();
			instanll(new File(entity.getFullPath()), mContext);
			return true;
		}

		@Override
		public boolean onDownloadFail(DownloadEntity entity, String error, Throwable e) {
			clearListenerMark();
			return super.onDownloadFail(entity, error, e);
		}

		public void clearListenerMark() {
			if (managerListener == this) {
				managerListener = null;
			}
		}
	};

	protected static void instanll(File file, Context context) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
			context.startActivity(intent);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "更新服务，Instanll 调用安装失败");
		}
	}

	/**
	 * 获取App是否 需要更新
	 */
	public boolean isNeedUpdate() {
		if (mVersionInfo == null) {
			return false;
		}
		int curver = transformVersion(mVersion);
		int server = transformVersion(mVersionInfo.serviceVersion);
		return curver < server;
	}

	public static class CheckUpdateTask extends AfDataTask.AbDataTaskHandler<Boolean> {

		@Override
		public boolean onPrepare(Boolean ndfeedback) {
			AfActivity activity = AfApp.get().getCurActivity();
			if (activity != null && ndfeedback) {
				activity.makeToastShort("正在检查更新...");
			}
			return super.onPrepare(ndfeedback);
		}

		@Override
		public void onTaskBackground(Boolean ndfeedback) throws Exception {
			mInstance.mVersionInfo = mInstance.infoFromService(mInstance.mVersion);
		}

		@Override
		public void onTaskHandle(Boolean ndfeedback, AfDataTask task) {
			mInstance.showNeedUpdate();
			AfActivity activity = AfApp.get().getCurActivity();
			if (activity != null && !mInstance.isNeedUpdate() && ndfeedback) {
				activity.makeToastShort("恭喜你，目前已经是最新版本！");
			}
		}
	}

	/**
	 * 从服务器加载更新信息（后台线程）
	 * @param version 当前App版本
	 * @return ServiceVersionInfo 包括 【服务器最新版本】 【更新描述】 【下载地址】
	 */
	protected abstract ServiceVersionInfo infoFromService(String version) throws Exception;

	protected void showNeedUpdate() {
		AfActivity activity = AfApp.get().getCurActivity();
		if (activity != null && isNeedUpdate()) {
			String message = String.format("系统检查到可用更新\r\n" +
							"    更新版本：%s\r\n" +
							"    当前版本：%s\r\n\r\n%s",
					mVersionInfo.serviceVersion, mVersion, mVersionInfo.updateDscribe);
			AfDialogBuilder dialog = new AfDialogBuilder(activity);
			dialog.showDialog("可用更新", message,"暂不更新",null,
					"下载更新", (dialog1, which) -> startDownLoadUpate(mVersionInfo.downloadUrl, mVersionInfo.serviceVersion));
		}
	}

	public static void startDownLoadUpate(String url, String version) {
		mInstance.start(url, version);
	}

	public static String transformVersion(int version) {
		int version1 = (version & 0xFF000000) >>> 24;
		int version2 = (version & 0x00FF0000) >>> 16;
		int version3 = (version & 0x0000FF00) >>> 8;
		int version4 = (version & 0x000000FF);
		return version1 + "." + version2 + "." + version3 + "." + version4;
	}

	public static int transformVersion(String version) {
		try {
			String[] vers = version.split("\\.");
			int ver1 = Integer.parseInt(vers[0]);
			int ver2 = Integer.parseInt(vers.length > 1 ? vers[1] : "0");
			int ver3 = Integer.parseInt(vers.length > 2 ? vers[2] : "0");
			int ver4 = Integer.parseInt(vers.length > 3 ? vers[3] : "0");
			ver3 += ver4 / 256;
			ver4 %= 256;
			ver2 += ver4 / 256;
			ver3 %= 256;
			ver1 += ver4 / 256;
			ver2 %= 256;
			return (ver1 << 24) | (ver2 << 16) | (ver3 << 8) | (ver4);
		} catch (Throwable ignored) {
		}
		return 0;
	}

}
