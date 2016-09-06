package com.andframe.caches;

import com.andframe.application.AfApp;

import java.util.HashMap;

@SuppressWarnings("unused")
public class AfPrivateCaches  extends AfJsonCache
{
	private static final String CACHE_NAME = "private";

	private static AfPrivateCaches mInstance = null;
	private static HashMap<String, AfPrivateCaches> mHashMap = new HashMap<>();

	private AfPrivateCaches() {
		super(AfApp.get(), CACHE_NAME);
	}

	private AfPrivateCaches(String name) {
		super(AfApp.get(), name);
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
