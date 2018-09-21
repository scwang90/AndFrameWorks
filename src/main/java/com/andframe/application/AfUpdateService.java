package com.andframe.application;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.andframe.$;
import com.andframe.activity.AfActivity;
import com.andframe.api.service.UpdateService;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.model.ServiceVersion;
import com.andframe.task.AfDownloader;
import com.andframe.task.AfDownloader.DownloadEntity;
import com.andframe.task.AfHandlerTask;

import java.io.File;

public abstract class AfUpdateService implements UpdateService {

	protected String mVersion;
	protected DownloadEntity mEntity;//已经下载完成的实体

	protected ServiceVersion mVersionInfo;
	protected DownloadListener managerListener = null;

	@Override
	public String getVersion() {
		return mVersion;
	}

	@Override
	public String getServiceVersion() {
		if (mVersionInfo == null) {
			return "请先检查更新";
		}
		return mVersionInfo.serviceVersion;
	}

	@Override
	public void checkUpdate() {
		checkUpdate(false);
	}

	@Override
	public void checkUpdate(boolean feedback) {
		$.task().postTask(new CheckUpdateTask(feedback));
	}

	public AfUpdateService() {
		mVersion = AfApp.get().getVersion();
	}

	protected void start(String url, String version) {
		if (mEntity != null && mEntity.Name.startsWith(version)) {
			install(new File(mEntity.getFullPath()));
		} else {
			DownloadEntity entity = new DownloadEntity();
			entity.Name = version + ".apk";
			entity.DownloadUrl = url;
			entity.DownloadPath = AfApp.get().getExternalCacheDir("update").getAbsolutePath();
			entity.Notify = new AfDownloader.NotifyEntity();
			entity.Notify.ContentTitle = AfApp.get().getAppName() + "更新";
			entity.Notify.FailText = "更新下载失败";
			entity.Notify.FinishText = "更新下载完成";
			AfDownloader.download(entity, managerListener = new DownloadListener());
		}
	}

	private class DownloadListener extends AfDownloader.DownloadManagerListener {
		@Override
		public void notifyClick(DownloadEntity entity) {
			mEntity = entity;
			clearListenerMark();
			install(new File(entity.getFullPath()));
		}

		@Override
		public boolean onDownloadFinish(DownloadEntity entity) {
			mEntity = entity;
			clearListenerMark();
			install(new File(entity.getFullPath()));
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

	@Override
	public void install(File file) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
			AfApp.get().startActivity(intent);
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "更新服务，Instanll 调用安装失败");
		}
	}

	/**
	 * 获取App是否 需要更新
	 */
	@Override
	public boolean isNeedUpdate() {
		if (mVersionInfo == null) {
			return false;
		}
		int curver = transformVersion(mVersion);
		int server = transformVersion(mVersionInfo.serviceVersion);
		return curver < server;
	}

	public class CheckUpdateTask extends AfHandlerTask {

		private boolean feedback;

		public CheckUpdateTask(boolean feedback) {
			this.feedback = feedback;
		}

		@Override
		public boolean onPrepare() {
			AfActivity activity = $.pager().currentActivity();
			if (activity != null && feedback) {
				activity.makeToastShort("正在检查更新...");
			}
			return super.onPrepare();
		}

		@Override
		protected void onWorking() throws Exception {
			mVersionInfo = infoFromService(mVersion);
		}

		@Override
		protected void onHandle() {
			showNeedUpdate();
			AfActivity activity = $.pager().currentActivity();
			if (activity != null && !isNeedUpdate() && feedback) {
				activity.makeToastShort("恭喜你，目前已经是最新版本！");
			}
		}
	}

	/**
	 * 从服务器加载更新信息（后台线程）
	 * @param version 当前App版本
	 * @return ServiceVersion 包括 【服务器最新版本】 【更新描述】 【下载地址】
	 */
	protected abstract ServiceVersion infoFromService(String version) throws Exception;

	protected void showNeedUpdate() {
		Activity activity = $.pager().currentActivity();
		if (activity != null && isNeedUpdate()) {
			String message = String.format("系统检查到可用更新\r\n" +
							"    更新版本：%s\r\n" +
							"    当前版本：%s\r\n\r\n%s",
					mVersionInfo.serviceVersion, mVersion, mVersionInfo.updateDscribe);
			$.dialog(activity).showDialog("可用更新", message,"暂不更新",null,
					"下载更新", (d, w) -> start(mVersionInfo.downloadUrl, mVersionInfo.serviceVersion));
		}
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
			String[] versions = version.split("\\.");
			int ver1 = Integer.parseInt(versions[0]);
			int ver2 = Integer.parseInt(versions.length > 1 ? versions[1] : "0");
			int ver3 = Integer.parseInt(versions.length > 2 ? versions[2] : "0");
			int ver4 = Integer.parseInt(versions.length > 3 ? versions[3] : "0");
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
