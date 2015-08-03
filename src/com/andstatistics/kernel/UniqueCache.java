package com.andstatistics.kernel;

import java.io.File;

import android.os.Environment;

import com.andframe.application.AfApplication;
import com.andframe.caches.AfJsonCache;

public class UniqueCache extends AfJsonCache
{
	private static UniqueCache mDurableCache = null;
	
	private UniqueCache(String name) throws Exception {
		super(AfApplication.getApp(),getPath(), name);
		// TODO Auto-generated constructor stub
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
				// TODO: handle exception
//				AfExceptionHandler.handler(e, "获取持久缓存失败");
			}
		}
		return mDurableCache;
	}

	private static String getPath() {
		// TODO Auto-generated method stub
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

