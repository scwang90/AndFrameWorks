package com.andframe.caches;

import java.util.HashMap;

import com.andframe.application.AfApplication;

public class AfPrivateCaches  extends AfJsonCache
{
	private static final String CACHE_NAME = "private";

	private static AfPrivateCaches mInstance = null;
	private static HashMap<String, AfPrivateCaches> mHashMap = new HashMap<String, AfPrivateCaches>();

	private AfPrivateCaches() {
		super(AfApplication.getApp(), CACHE_NAME);
		// TODO Auto-generated constructor stub
	}
	private AfPrivateCaches(String name) {
		super(AfApplication.getApp(), name);
		// TODO Auto-generated constructor stub
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
