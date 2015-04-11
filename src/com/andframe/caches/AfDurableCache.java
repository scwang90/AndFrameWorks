package com.andframe.caches;

import java.util.HashMap;

import com.andframe.application.AfApplication;

/**
 * AfDurableCache 持久缓存
 * @author SCWANG
 *			继承AfJsonCache 主要持久化缓存
 *			将缓存路径固定到<工程路径/Caches/DurableCache>  中
 */
public class AfDurableCache extends AfJsonCache
{
	private static final String CACHE_NAME = "durable";

	private static AfDurableCache mInstance = null;
	
	private AfDurableCache() throws Exception {
		super(AfApplication.getApp(),getPath(), CACHE_NAME);
		// TODO Auto-generated constructor stub
	}
	/**
	 * SD卡被占用时会返回null
	 * @return
	 */
	public static AfDurableCache getInstance() {
		if(mInstance == null){
			try {
				mInstance = new AfDurableCache();
			} catch (Throwable e) {
				// TODO: handle exception
//				AfExceptionHandler.handler(e, "获取持久缓存失败");
			}
		}
		return mInstance;
	}

	private static String getPath() {
		// TODO Auto-generated method stub
		return AfApplication.getApp().getCachesPath("Durable");
	}

	private static HashMap<String, AfDurableCache> mHashMap = new HashMap<String, AfDurableCache>();

	private AfDurableCache(String name) throws Exception{
		super(AfApplication.getApp(),getPath(), name);
		// TODO Auto-generated constructor stub
	}
	
	public static AfDurableCache getInstance(String name) {
		AfDurableCache caches = mHashMap.get(name);
		if(caches == null){
			try {
				caches = new AfDurableCache(name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mHashMap.put(name, caches);
		}
		return caches;
	}
}
