package com.andstatistics.kernel;

import android.os.Environment;

import com.andframe.application.AfApplication;
import com.andframe.caches.AfJsonCache;

import java.io.File;

public class UniqueCache extends AfJsonCache
{
	private static UniqueCache mDurableCache = null;
	
	private UniqueCache(String name) throws Exception {
		super(AfApplication.getApp(),getPath(), name);
	}

	/**
	 * SD卡被占用时会返回null
	 * @return
	 */
	public static UniqueCache getInstance(String name) {
		if(mDurableCache == null){
			try {
				mDurableCache = new UniqueCache(name);
			} catch (Throwable e) {
//				AfExceptionHandler.handle(e, "获取持久缓存失败");
			}
		}
		return mDurableCache;
	}

	private static String getPath() {
		File workspace = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			workspace = new File(sdcard + "/.android/");
			if (!workspace.exists()) {
				workspace.mkdir();
			}
		} else {
			return AfApplication.getApp().getPrivatePath("");
		}
		return workspace.getPath();
	}
}

