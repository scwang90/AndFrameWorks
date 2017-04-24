package com.andframe.application;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andframe.BuildConfig;
import com.andframe.api.DialogBuilder;
import com.andframe.api.Toaster;
import com.andframe.api.event.EventManager;
import com.andframe.api.pager.PagerManager;
import com.andframe.api.pager.items.MoreFooter;
import com.andframe.api.pager.status.RefreshLayouter;
import com.andframe.api.pager.status.StatusLayouter;
import com.andframe.api.task.TaskExecutor;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.Viewer;
import com.andframe.caches.AfJsonCache;
import com.andframe.exception.AfException;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfDialogBuilder;
import com.andframe.impl.viewer.AfViewQuery;
import com.andframe.impl.AfToaster;
import com.andframe.impl.pager.items.DefaultMoreFooter;
import com.andframe.impl.pager.status.DefaultRefreshLayouter;
import com.andframe.impl.pager.status.DefaultStatusLayouter;
import com.andframe.impl.pager.AfPagerManager;
import com.andframe.task.AfTaskExecutor;
import com.andframe.util.java.AfReflecter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * AfApp 抽象类 （使用必须继承并使用，其他框架功能依赖于 AfApp）
 */
public abstract class AfApp extends Application {

	//<editor-fold desc="属性字段">

	//<editor-fold desc="常量">
	private static final String STATE_RUNNING = "STATE_RUNNING";
	private static final String STATE_TIME = "STATE_TIME";
	private static final String STATE_VERSION = "STATE_VERSION";
	//</editor-fold>

	//<editor-fold desc="状态">
	// 保存数据
	protected Date mStateTime = new Date();
	protected AfJsonCache mRunningState = null;
	//</editor-fold>

	//<editor-fold desc="通用">
	// 当前版本
	protected String mVersion = "0.0.0.0";
	private PackageInfo mPackageInfo = null;
	//</editor-fold>

	protected static AfApp mApp = null;

	//</editor-fold>

	public AfApp() {
		mApp = this;
	}

	public static AfApp get() {
		if (mApp == null) {
			try {
//				Applicatioin app = RuntimeInit.mApplicationObject.this$0.mInitialApplication;
				Class<?> runtimeInitClass = Class.forName("com.android.internal.os.RuntimeInit");
				Object mInitialApplication = AfReflecter.getMember(runtimeInitClass, "mApplicationObject.this$0.mInitialApplication");
				mApp = new AfApp() {{
					this.attachBaseContext((Context) mInitialApplication);
					this.initApp();
				}};
			} catch (Throwable e) {
				e.printStackTrace();
			}

			try {
				Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
				Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
				Context context = (Context) method.invoke(null);
				mApp = new AfApp() {{
					this.attachBaseContext(context);
					this.initApp();
				}};
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return mApp;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			initApp();
		} catch (Throwable e) {
			AfExceptionHandler.handle(e, "AfApp.onCreate");
		}
	}

	@CallSuper
	protected void initApp() throws Exception {
		AfExceptionHandler.register();
		mRunningState = new AfJsonCache(this, STATE_RUNNING);
		getPackageVersion();
	}

	private void getPackageVersion() throws Exception {
		int get = PackageManager.GET_CONFIGURATIONS;
		String tPackageName = getPackageName();
		PackageManager magager = getPackageManager();
		mPackageInfo = magager.getPackageInfo(tPackageName, get);
		mVersion = mPackageInfo.versionName;
	}

	/**
	 * 获取在Application 中定义的 meta-data
	 * @return meta-data or null
	 */
	@SuppressWarnings("unused")
	public Bundle getMetaData() {
		try {
			String name = this.getPackageName();
			int type = PackageManager.GET_META_DATA;
			PackageManager manager = this.getPackageManager();
			ApplicationInfo info = manager.getApplicationInfo(name, type);
			return info.metaData;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return new Bundle();
		}
	}

	/**
	 * 获取在Application 中定义的 meta-data
	 * @param key 键
	 * @return meta-data or null
	 */
	@SuppressWarnings("unused")
	public String getMetaData(String key) {
		return getMetaData(key, null);
	}

	/**
	 * 获取在Application 中定义的 meta-data
	 * @param key 键
	 * @param defvalue 默认值
	 * @return meta-data or defvalue
	 */
	public String getMetaData(String key,String defvalue) {
		try {
			Object data = getMetaData().get(key);
			if (data == null) {
				throw new Exception("getMetaData null");
			}
			key = String.valueOf(data);
			return key;
		} catch (Throwable e) {
			//AfExceptionHandler.handle(e, "AfApplication.getMetaData");
		}
		return defvalue;
	}


	//<editor-fold desc="通用方法">
	public boolean isDebug() {
		try {
			Class<?> clazz = Class.forName(getPackageName() + ".BuildConfig");
			return Boolean.valueOf(true).equals(AfReflecter.getMember(clazz,"DEBUG"));
		} catch (Throwable ignored) {
		}
		return BuildConfig.DEBUG;
	}
	/**
	 * 获取APP名称,子类可以继承返回getString(R.string.app_name);
	 * @return APP名称
	 */
	public String getAppName() {
		Resources resources = getResources();
		int id = resources.getIdentifier(getPackageName() + ":string/app_name", null, null);
		if(id > 0){
			return resources.getString(id);
		}
		return "AndFrame";
	}

	public PackageInfo getPackageInfo() {
		return mPackageInfo;
	}

	public String getVersion() {
		return mVersion;
	}

	//</editor-fold>

	//<editor-fold desc="目录分类">
	/**
	 * 获取app工作目录
	 */
	public synchronized file getprivatepath(string type) {
		file file = new file(getcachedir(), type);
		if (!file.exists() && !file.mkdirs()) {
			if (isdebug()) {
				new ioexception("获取私有路径失败").printstacktrace();
			}
		}
		return file;
	}

	/**
	 * 获取app工作目录
	 */
	public synchronized file getworkspacepath(string type) {
		file workspace;
		if (hasexternalstorage()) {
			string sdcard = environment.getexternalstoragedirectory().getpath();
			workspace = new file(sdcard + "/" + getappname() + "/" + type);
			if (!workspace.exists() && !workspace.mkdirs()) {
				return getprivatepath(type);
			}
		} else {
			return getprivatepath(type);
		}
		return workspace;
	}

	/**
	 * 获取caches目录
	 */
	public synchronized file getcachespath(string type) {
		file caches = getworkspacepath("caches");
		caches = new file(caches, type);
		if (!caches.exists() && !caches.mkdirs()) {
			if (isdebug()) {
				new ioexception("获取缓存路径失败").printstacktrace();
			}
		}
		return caches;
	}

	/**
	 * 是否存在扩展卡
     */
	public boolean hasexternalstorage() {
		return environment.media_mounted.equals(environment.getexternalstoragestate())
				/*|| !environment.isexternalstorageremovable()*/;
	}

	@override
	public file getexternalcachedir() {
		file path;
		if (hasexternalstorage()) {
			path = super.getexternalcachedir();
		} else {
			path = super.getcachedir();
		}
		if (path != null && !path.exists() && !path.mkdirs() && isdebug()) {
			log.w(afapp.class.getname(), "getexternalcachedir.mkdirs fail");
		} else if (path == null) {
			log.w(afapp.class.getname(), "path = null");
		}
		return path;
	}

	@suppresswarnings("unused")
	public file getexternalcachedir(string type) {
		return new file(getexternalcachedir(), type);
	}

	@override
	public file getexternalfilesdir(string type) {
		file path;
		if (hasexternalstorage()) {
			path = super.getexternalfilesdir(type);
		} else {
			path = new file(super.getcachedir(), type);
		}
		if (path != null && !path.exists() && !path.mkdirs() && isdebug()) {
			log.w(afapp.class.getname(), "getexternalcachedir.mkdirs fail");
		} else if (path == null) {
			log.w(afapp.class.getname(), "path = null");
		}
		return path;
	}
	//</editor-fold>

	//<editor-fold desc="状态恢复">
	/**
	 * 更新保存转台时间，如果已经保存下次也可执行 onSaveInstanceState
	 */
	public void updateStateTime() {
		mStateTime = new Date();
	}

	/**
	 * 当APP被临时销毁时保存App 状态 在AfActivity 中调用
	 */
	public final void onRestoreInstanceState() {
		Date date = mRunningState.getDate(STATE_TIME,null);
		if (date != null) {
			onRestoreInstanceState(mRunningState);
			mRunningState.clear();
		}
	}

	/**
	 * 当APP被临时销毁时保存App 状态 在onRestoreInstanceState() 中调用
	 */
	protected void onRestoreInstanceState(AfJsonCache state) {
		mVersion = state.getString(STATE_VERSION, mVersion);
	}

	/**
	 * 当APP被还原时候还原原来状态 在AfActivity 中调用
	 */
	public final void onSaveInstanceState() {
		// 如果保存时间标记一直，则不用保存
		Date date = mRunningState.getDate(STATE_TIME, null);
		if (date != null && date.equals(mStateTime)) {
			return;
		}
		mRunningState.put(STATE_TIME, mStateTime);
		onSaveInstanceState(mRunningState);
	}

	/**
	 * 当APP被还原时候还原原来状态 在AfActivity 中调用
	 */
	protected void onSaveInstanceState(AfJsonCache state) {
		state.put(STATE_VERSION, mVersion);
	}
	//</editor-fold>

	//<editor-fold desc="页面状态">

	public boolean isUserLogined() {
		throw new AfToastException("App.isUserLogined必须由子类实现");
	}

	/**
	 * 判断是否在后台运行（按HOME之后）
	 * 需要额外权限 android.permission.GET_TASKS 
	 * @return isBackground
	 */
	public boolean isBackground() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		try {
			List<RunningTaskInfo> tasks = am.getRunningTasks(1);
			if (tasks.size() > 0) {
				ComponentName topActivity = tasks.get(0).topActivity;
				if (!topActivity.getPackageName().equals(getPackageName())) {
					return true;
				}
			}
		} catch (Throwable ignored) {
		}
		return false;
	}

	//</editor-fold>

	//<editor-fold desc="组件创建">
	/**
	 * 获取 ExceptionHandler
	 * @return handle
	 */
	public AfExceptionHandler newExceptionHandler() {
		return new AfExceptionHandler();
	}

	public ViewQuery<? extends ViewQuery> newViewQuery(Viewer view) {
		return new AfViewQuery<>(view);
	}

	public TaskExecutor newTaskExecutor() {
		return new AfTaskExecutor();
	}

	public DialogBuilder newDialogBuilder(Context context) {
		return new AfDialogBuilder(context);
	}

	@NonNull
	public RefreshLayouter newRefreshLayouter(Context context) {
		return new DefaultRefreshLayouter(context);
	}

	@NonNull
	public StatusLayouter newStatusLayouter(Context context) {
		return new DefaultStatusLayouter(context);
	}

	public AfAppSettings newAppSetting() {
		return new AfAppSettings(this);
	}

	public MoreFooter newMoreFooter() {
		return new DefaultMoreFooter();
	}

	public PagerManager newPagerManager() {
		return new AfPagerManager();
	}

	public Toaster newToaster() {
		return new AfToaster();
	}

	public EventManager getEventManager() {
		throw new AfException("如果项目中要使用事件组件，请先在App重写getEventManager方法，建议使用流行的EventBus来实现");
	}
	//</editor-fold>

}
