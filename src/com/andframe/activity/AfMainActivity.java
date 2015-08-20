package com.andframe.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;

import com.andframe.application.AfAppSettings;
import com.andframe.application.AfApplication;
import com.andframe.application.AfApplication.INotifyNeedUpdate;
import com.andframe.application.AfApplication.INotifyNetworkStatus;
import com.andframe.application.AfApplication.INotifyUpdate;
import com.andframe.application.AfUpdateService;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.constant.AfNetworkEnum;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfVersion;
/**
 * 框架App主页面 
 * @author 树朾
 * 实现 更新通知，网络改变通知接口
 * 	主要实现了更新提示，网络无效提示
 * 	onCreate 向 App 注册主页面
 * 	onDestroy 向 App 解除主页面
 *
 * 	返回按键 提示 "再按一次退出程序"
 */
public abstract class AfMainActivity extends com.andframe.activity.framework.AfActivity
	implements INotifyNeedUpdate,INotifyUpdate,INotifyNetworkStatus,OnClickListener{

	protected static final String KEY_IGNORE = "39894915342252804102";
	
	protected long mExitTime;
	protected long mExitInterval = 2000;
	protected boolean mNotifyNetInvaild = true;

	protected abstract void onActivityCreate(Bundle savedInstanceState);

	@Override
	protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
		super.onCreate(bundle, intent);
		AfApplication app = AfApplication.getApp();
		app.setMainActivity(this);
//		if(!app.isInitialize()){
//			app.initialize(this);
//			if(onRestoreApp()){
//				makeToastShort("程序已修复");
//			}else{
//				this.finish();
//				return;
//			}
//		} 
		
		onActivityCreate(bundle);
		if (AfApplication.getNetworkStatus() == AfNetworkEnum.TYPE_NONE) {
			// 显示网络不可用对话框
			if(mNotifyNetInvaild)showNetworkInAvailable();
//		}else if(app.isNeedUpdate()){
//			// 显示去要更新对话框
//			showNeedUpdate();
		}else if (AfAppSettings.getInstance().isAutoUpdate()) {
			//自动更新
			AfUpdateService.checkUpdate();
		}
	}

	/**
	 * @deprecated 已经弃用
	 * @return
	 */
	protected boolean onRestoreApp() {
		return true;
	}

	/**
	 * 通知APP 前台已经关闭
	 */
	@Override
	protected void onDestroy() {
		AfApplication app = AfApplication.getApp();
		// 通知APP 界面已经退出
		app.notifyForegroundClosed(this);
		super.onDestroy();
	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			if ((System.currentTimeMillis() - mExitTime) > mExitInterval) {
//				makeToastShort("再按一次退出程序");
//				mExitTime = System.currentTimeMillis();
//			} else {
//				this.finish();
//			}
//			return true;
//		}
//		return super.dispatchKeyEvent(event);
//	}
	
	@Override
	protected boolean onBackKeyPressed() {
		boolean isHandled = super.onBackKeyPressed();
		if (!isHandled) {
			isHandled = true;
			if ((System.currentTimeMillis() - mExitTime) > mExitInterval) {
				makeToastShort("再按一次退出程序");
				mExitTime = System.currentTimeMillis();
			} else {
				this.finish();
			}
		}
		return isHandled;
	}

	@Override
	public void onNotifyUpdate(String curver, String server, String describe) {
		// 显示去要更新对话框
		AfApplication app = AfApplication.getApp();
		if (app.isForegroundRunning() && app.getCurActivity() == this) {
			showNeedUpdate();
		}
	}
	
	@Override
	public void onNotifyNeedUpdate(String curver, String server) {
		// 显示去要更新对话框
		AfApplication app = AfApplication.getApp();
		if (app.isForegroundRunning() && app.getCurActivity() == this) {
			showNeedUpdate();
		}
	}
	
	@Override
	public void onNetworkStatusChanged(int networkStatus) {
	}

	@Override
	public void onClick(View v) {
	}


	/**
	 * 显示网络不可用对话框
	 */
	protected void showNetworkInAvailable() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("当前网络不可用");
		builder.setMessage("你可以浏览离线信息或者设置网络连接。");
		builder.setNegativeButton("设置网络",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 如果没有网络连接，则进入网络设置界面
						startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
					}
				});
		builder.setPositiveButton("浏览离线信息",null);
		builder.create();
		builder.show();
	}

	protected void showNeedUpdate() {
		AfPrivateCaches caches = AfPrivateCaches.getInstance();
		String curversion = AfApplication.getVersion();
		String serversion = AfApplication.getApp().getServerVersion();
		String sedescribe = AfApplication.getApp().getUpdateDescribe();
		if (caches.getString(KEY_IGNORE, "").equals(serversion)) {
			return ;
		}
		int curver = AfVersion.transformVersion(curversion);
		int server = AfVersion.transformVersion(serversion);
		if (curver < server) {
			Builder builder = new Builder(this);
//			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("可用更新");
			if(sedescribe == null || sedescribe.length() == 0){
				builder.setMessage("系统检查到可用更新\r\n    更新版本："
						+ serversion + "\r\n    当前版本：" + curversion);
			}else{
				builder.setMessage("系统检查到可用更新\r\n    "
						+ curversion  + " -> " + serversion+ "\r\n\r\n" +sedescribe);
			}
			builder.setPositiveButton("下载更新", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String serversion = AfApplication.getApp().getServerVersion();
					String url = AfUpdateService.getInstance().getApkUrl(serversion);
					AfUpdateService.startDownLoadUpate(getContext(), url, serversion);
				}
			});
			builder.setNeutralButton("忽略此版本",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AfPrivateCaches caches = AfPrivateCaches.getInstance();
					String serversion = AfApplication.getApp().getServerVersion();
					caches.put(KEY_IGNORE, serversion);
				}
			});
			builder.setNegativeButton("暂不更新",null);
			builder.create();
			builder.show();
		}
	}
}
