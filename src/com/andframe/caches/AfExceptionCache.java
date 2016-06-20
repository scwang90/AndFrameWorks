package com.andframe.caches;

import com.andframe.application.AfApplication;
import com.andframe.model.Exceptional;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;


public class AfExceptionCache
{
	private static final String CACHE_KEY = "EXCEPTIONCACHE_KEY";
	private static final String CACHE_NAME = "EXCEPTIONCACHE";
	
	private AfSharedPreference mShared = null;

	public AfExceptionCache() {
		try {
			mShared = new AfSharedPreference(AfApplication.getApp(),getPath(), CACHE_NAME);
		} catch (Throwable e) {
			mShared = new AfSharedPreference(AfApplication.getApp(), CACHE_NAME);
		}
	}

	private static String getPath() {
		return AfApplication.getApp().getPrivatePath("Exception");//.getCachesPath("Exception");
	}

	public Set<Exceptional> getExceptionHandlerSet(Set<Exceptional> defvalue) {
		Set<String> strset = mShared.getStringSet(CACHE_KEY,null);
		if(strset == null){
			return defvalue;
		}
		Gson json = new Gson();
		Set<Exceptional> setHandler = new HashSet<Exceptional>();
		try {
			for (String jvalue : strset) {
				setHandler.add(json.fromJson(jvalue, Exceptional.class));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return setHandler;
	}

	public void putExceptionHandlerSet(Set<Exceptional> sthandler) {
		Gson json = new Gson();
		Set<String> strset = new HashSet<String>();
		for (Exceptional handler : sthandler) {
			strset.add(json.toJson(handler));
		}
		mShared.putStringSet(CACHE_KEY, strset);
	}

	public void addExceptionHandlerSet(Exceptional handler) {
		Gson json = new Gson();
		Set<String> strset = mShared.getStringSet(CACHE_KEY,new HashSet<String>());
		strset.add(json.toJson(handler));
		mShared.putStringSet(CACHE_KEY, strset);
	}
}
