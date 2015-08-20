package com.andframe.caches;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.andframe.application.AfExceptionHandler;
import com.andframe.util.java.AfMD5;
/**
 * AfImageCaches 图片缓存
 * @author 树朾
 * 	1.内存缓存 （用于加速图片的读取，没有的时候用文件缓存）
 * 	2.文件缓存 （文件缓存>=内存缓存）
 * 	
 * 	a.请在 Application 初始化的时候 调用 initialize 初始化 path 是文件缓存路径
 * 		public static synchronized void initialize(Context context, String path) 
 * 	b.使用方法为单例模式，调用getInstance获取缓存实例
 * 		public static synchronized AfImageCaches getInstance()
 * 	c.详细使用
				 * 获取缓存 BitmapDrawable 
				public BitmapDrawable get(Context context, String url) 
				 * 获取缓存 Bitmap 
				public Bitmap get(String url) 
				 * 添加缓存 
				public void put(String url, Bitmap bitmap) 
				 * 添加缓存 
				public void put(String url, BitmapDrawable bitmap) 
				 * 清除所有缓存
				public void clear() 
				 * 获取缓存大小
				public int getCachesSize() 
				 * 获取缓存路劲文件
				public File getCachePath() 
				 * 删除指定的 图片缓存
				 * @param key
				public void remove(String url) 
 */
public class AfImageCaches {
	// 使用 12.5% 的内存作为内存存储
	private int mMemorySize = 10 * 1024 * 1024;// (int)(Runtime.getRuntime().maxMemory()/8);
	private static File mCacheDirFile = null;
	private static AfImageCaches mInstance = null;

	public static synchronized void initialize(Context context, String path) {
		mCacheDirFile = new File(path);
		mInstance = new AfImageCaches(context);
		if (mCacheDirFile.exists() == false) {
			mCacheDirFile.mkdirs();
		}
	}

	public static synchronized AfImageCaches getInstance() {
		return mInstance;
	}

	private LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(
			mMemorySize) {
		private String last_key = "";

		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			last_key = key;
			int size = bitmap.getHeight() * bitmap.getWidth() * 2;
			return size;
		}

		@Override
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldBitmap, Bitmap newBitmap) {
			if (!key.equalsIgnoreCase(last_key)) {
				oldBitmap.recycle();
				oldBitmap = null;
				System.gc();
			}
		}
	};

	/**
	 * @param context
	 */
	private AfImageCaches(Context context) {
		// mImageCacheDao = new ImageCacheDao(context);
		if (mCacheDirFile == null) {
			mCacheDirFile = context.getCacheDir();
		}

		if (mCacheDirFile.exists() == false) {
			mCacheDirFile.mkdirs();
		}
	}

	/**
	 * 获取缓存 BitmapDrawable 
	 * @param context 
	 * @param url 图片的URl
	 * @return BitmapDrawable 否则 null
	 */
	public BitmapDrawable get(Context context, String url) {
		Bitmap bitmap = get(url);
		if(bitmap == null){
			return null;
		}
		return new BitmapDrawable(context.getResources(), bitmap);
	}

	/**
	 * 获取缓存 Bitmap 
	 * @param url 图片的URl
	 * @return Bitmap 否则 null
	 */
	public Bitmap get(String url) {
		Log.e("BitmapCaches", url);

		String key = AfMD5.getMD5(url.getBytes());
		// 从内存中取
		synchronized (mMemoryCache) {
			Bitmap bitmap = mMemoryCache.get(key);
			if (bitmap != null) {
				if (bitmap.isRecycled()) {
					mMemoryCache.remove(key);
					return null;
				}
				return bitmap;
			}
		}
		// 从文件中取
		try {
			String Path = mCacheDirFile.getAbsolutePath() + File.separator
					+ key;
			File file = new File(Path);
			if (!file.isFile() || !file.exists()) {
				return null;
			}
			Bitmap bitmap = BitmapFactory.decodeFile(Path);
			if (bitmap != null) {
				synchronized (mMemoryCache) {
					mMemoryCache.put(key, bitmap);
				}
			}
			return bitmap;
		} catch (Throwable e) {
			e.printStackTrace();// handled
//			AfExceptionHandler.handler(e, "图片缓存，get 出现异常");
		}
		return null;
	}

	/**
	 * 添加缓存 
	 * @param url 图片的URl
	 * @param bitmap 缓存图片
	 */
	public void put(String url, Bitmap bitmap) {
		String key = AfMD5.getMD5(url.getBytes());
		if (key != null && bitmap != null) {
			synchronized (mMemoryCache) {
//				Log.w("Bitmpa Cache", "add cache: " + key);
				mMemoryCache.put(key, bitmap);
				// 移到文件缓存
				this.putToFile(key, bitmap);
			}
		}
	}

	/**
	 * 添加缓存 
	 * @param url 图片的URl
	 * @param bitmap 缓存图片
	 */
	public void put(String url, BitmapDrawable bitmap) {
		put(url, bitmap.getBitmap());
	}

	/**
	 * 清除所有缓存
	 */
	public void clear() {
		if (mMemoryCache != null) {
			mMemoryCache.evictAll();
		}

		File[] files = mCacheDirFile.listFiles();
		// mImageCacheDao.delAll();
		if (files != null) {
			for (File file : files)
				file.delete();
		}
		System.gc();
	}

	/**
	 * 获取缓存大小
	 */
	public int getCachesSize() {
		int size = 0;
		File[] files = mCacheDirFile.listFiles();
		if (files != null) {
			for (File file : files) {
				size += file.length();
			}
		}
		return size;
	}

	/**
	 * 获取缓存路劲文件
	 * @return
	 */
	public File getCachePath() {
		return mCacheDirFile;
	}

	/**
	 * 删除指定的 图片缓存
	 * @param key
	 */
	public void remove(String url) {
		try {
			String key = AfMD5.getMD5(url.getBytes());
			String Path = mCacheDirFile.getAbsolutePath() + File.separator
					+ key;
			File file = new File(Path);
			if(file.exists()){
				file.delete();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			AfExceptionHandler.handler(e, "AfImageCaches.remove.Exception");
		}
	}

	/**
	 * @param key
	 * @param bitmap
	 */
	private void putToFile(String key, Bitmap bitmap) {
		try {
			// if(mImageCacheDao.Exist(key)){
			// return;
			// }
			String Path = mCacheDirFile.getAbsolutePath() + File.separator
					+ key;
			// ImageCache tImageCache = new ImageCache("", key, Path, "");

			FileOutputStream tFileOutputStream = new FileOutputStream(Path);

			if (bitmap.compress(CompressFormat.JPEG, 100, tFileOutputStream)) {
				// mImageCacheDao.add(tImageCache);
			}

			tFileOutputStream.flush();
			tFileOutputStream.close();
		} catch (Throwable e) {
			e.printStackTrace();// handled
//			AfExceptionHandler.handler(e, "图片缓存，putToFile 出现异常");
		}
	}

	/**
	 * 映射一个url到本地缓存路径到
	 * @param mLinkUrl
	 * @return
	 */
	public String mapPath(String mLinkUrl) {
		String key = AfMD5.getMD5(mLinkUrl.getBytes());
		return mCacheDirFile.getAbsolutePath() + File.separator
				+ key;
	}

}
