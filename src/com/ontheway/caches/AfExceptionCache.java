package com.ontheway.caches;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.ontheway.application.AfApplication;
import com.ontheway.model.Exceptional;


public class AfExceptionCache
{
	private static final String CACHE_KEY = "EXCEPTIONCACHE_KEY";
	private static final String CACHE_NAME = "EXCEPTIONCACHE";
	
	private AfSharedPreference mShared = null;

	public AfExceptionCache() {
		// TODO Auto-generated constructor stub
		try {
			mShared = new AfSharedPreference(AfApplication.getApp(),getPath(), CACHE_NAME);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			mShared = new AfSharedPreference(AfApplication.getApp(), CACHE_NAME);
		}
	}

	private static String getPath() {
		// TODO Auto-generated method stub
		return AfApplication.getApp().getPrivatePath("Exception");//.getCachesPath("Exception");
	}

	public Set<Exceptional> getExceptionHandlerSet(Set<Exceptional> defvalue) {
		// TODO Auto-generated method stub
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
			// TODO: handle exception
			e.printStackTrace();
		}
		return setHandler;
	}

	public void putExceptionHandlerSet(Set<Exceptional> sthandler) {
		// TODO Auto-generated method stub
		Gson json = new Gson();
		Set<String> strset = new HashSet<String>();
		for (Exceptional handler : sthandler) {
			strset.add(json.toJson(handler));
		}
		mShared.putStringSet(CACHE_KEY, strset);
	}

	public void addExceptionHandlerSet(Exceptional handler) {
		// TODO Auto-generated method stub
		Gson json = new Gson();
		Set<String> strset = mShared.getStringSet(CACHE_KEY,new HashSet<String>());
		strset.add(json.toJson(handler));
		mShared.putStringSet(CACHE_KEY, strset);
	}
}
