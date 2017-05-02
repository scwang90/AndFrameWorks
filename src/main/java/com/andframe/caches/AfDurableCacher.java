package com.andframe.caches;

import com.andframe.application.AfApp;

/**
 * AfDurableCacher 持久缓存
 * @author 树朾
 * 		继承AfJsonCache 主要持久化缓存
 * 		将缓存路径固定到<工程路径/Caches/DurableCache>  中
 */
public class AfDurableCacher extends AfJsonCache
{
	private static final String CACHE_NAME = "durable";

	public AfDurableCacher() {
		super(AfApp.get(), getPath(), CACHE_NAME);
	}

	public AfDurableCacher(String name) {
		super(AfApp.get(), getPath(), name);
	}

	public static String getPath() {
		return AfApp.get().getCachesPath("Durable").getPath();
	}

}
