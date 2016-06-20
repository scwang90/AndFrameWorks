package com.andframe.caches;

import com.andframe.application.AfApplication;

import java.util.HashMap;

public class AfPrivateCaches  extends AfJsonCache
{
	private static final String CACHE_NAME = "private";

	private static AfPrivateCaches mInstance = null;
	private static HashMap<String, AfPrivateCaches> mHashMap = new HashMap<String, AfPrivateCaches>();

	private AfPrivateCaches() {
		super(AfApplication.getApp(), CACHE_NAME);
	}
	private AfPrivateCaches(String name) {
		super(AfApplication.getApp(), name);
	}
	
	public static AfPrivateCaches getInstance() {
		if(mInstance == null){
			mInstance = new AfPrivateCaches();
		}
		return mInstance;
	}
	
	public static AfPrivateCaches getInstance(String name) {
		AfPrivateCaches caches = mHashMap.get(name);
		if(caches == null){
			caches = new AfPrivateCaches(name);
			mHashMap.put(name, caches);
		}
		return caches;
	}
}
