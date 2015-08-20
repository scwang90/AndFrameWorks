package com.andframe.feature;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.andframe.feature.framework.AfExtrater;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 使用Gson实现多元数据传递
 * AfIntent
 * @author 树朾
 */
public class AfIntent extends Intent implements AfExtrater{

	protected static Gson mJson = new Gson();

	public AfIntent() {
		super();
	}

	public AfIntent(Context packageContext, Class<?> cls) {
		super(packageContext, cls);
	}

	public AfIntent(Intent o) {
		super(o!=null?o:new Intent());
	}

	public AfIntent(String action, Uri uri, Context packageContext, Class<?> cls) {
		super(action, uri, packageContext, cls);
	}

	public AfIntent(String action, Uri uri) {
		super(action, uri);
	}

	public AfIntent(String action) {
		super(action);
	}
	
	public AfIntent(String _key, Object value) {
		super();
		putExtra(_key, value.getClass().getName());
		putExtra(_key+"[0]", mJson.toJson(value));
	}

	public void put(String _key, Object value) {
		if (value instanceof List){
			putList(_key,(List<?>)value);
		} else {
			putExtra(_key, value.getClass().getName());
			putExtra(_key+"[0]", mJson.toJson(value));
		}
	}

	public void putList(String _key, List<? extends Object> value) {
		int length = value.size();
		putExtra(_key, mJson.toJson(length));
		for (int i = 0; i < length; i++) {
			putExtra(_key+"["+i+"]", mJson.toJson(value.get(i)));
		}
	}

	public <T> T get(String _key,Class<T> clazz) {
		return get(_key,null,clazz);
	}

	public <T> T get(String _key,T defaul,Class<T> clazz) {
		T value = null;
		try {
			String name = getStringExtra(_key);
			if(!clazz.getName().equals(name)){
				return defaul;
			}
			value = mJson.fromJson(getStringExtra(_key+"[0]"), clazz);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return value == null ? defaul : value;
	}

	/**
	 * 获取List<T>
	 * @param _key
	 * @param clazz
	 * @return 如果找不到返回 null， list 有可能为 0 个元素
	 */
	public <T> List<T> getList(String _key,Class<T> clazz) {
		return getList(_key,null,clazz);
	}

	/**
	/**
	 * 获取List<T>
	 * @param _key
	 * @param defaul 如果找不到返回 defaul
	 * @param clazz
	 * @return 如果找不到返回 null， list 有可能为 0 个元素
	 */
	public <T> List<T> getList(String _key,List<T> defaul,Class<T> clazz) {
		List<T> value = null;
		try {
			value = new ArrayList<T>();
			Integer length = mJson.fromJson(getStringExtra(_key), Integer.class);
			for (int i = 0; i < length; i++) {
				T t = mJson.fromJson(getStringExtra(_key+"["+i+"]"), clazz);
				if(t != null){
					value.add(t);
				}
			}
		} catch (Throwable e) {
			if(value != null && value.size() == 0){
				value = null;
			}
		}
		return value == null ? defaul : value;
	}

	public String getString(String _key, String _default) {
		return get(_key,_default,String.class);
	}

	public short getShort(String _key, short _default) {
		return get(_key,_default,Short.class);
	}

	public boolean getBoolean(String _key, boolean _default) {
		return get(_key,_default,Boolean.class);
	}

	public int getInt(String _key, int _default) {
		return get(_key,_default,Integer.class);
	}

	public long getLong(String _key, long _default) {
		return get(_key,_default,Long.class);
	}

	public float getFloat(String _key, float _default) {
		return get(_key,_default,Float.class);
	}

	public double getDouble(String _key, double _default) {
		return get(_key,_default,Double.class);
	}

	public UUID getUUID(String _key, UUID _default) {
		return get(_key,_default,UUID.class);
	}
}
