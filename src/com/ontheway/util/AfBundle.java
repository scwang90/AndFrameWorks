package com.ontheway.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.os.Bundle;

import com.google.gson.Gson;
/**
 * AfBundle 
 * @author SCWANG
 *		包装 Android Bundle 支持任意对象传输
 */
public class AfBundle {

	protected Bundle mBundle;
	protected static Gson mJson = new Gson();

	public AfBundle(Bundle bundle) {
		// TODO Auto-generated constructor stub
		mBundle = bundle;
	}
	
	public Bundle getBundle() {
		return mBundle;
	}

	public void put(String _key, Object value) {
		mBundle.putString(_key, value.getClass().getName());
		mBundle.putString(_key+"[0]", mJson.toJson(value));
	}

	public void putList(String _key, List<? extends Object> value) {
		int length = value.size();
		mBundle.putString(_key, mJson.toJson(length));
		for (int i = 0; i < length; i++) {
			mBundle.putString(_key+"["+i+"]", mJson.toJson(value.get(i)));
		}
	}

	public <T> T get(String _key,Class<T> clazz) {
		return get(_key,null,clazz);
	}

	public <T> T get(String _key,T defaul,Class<T> clazz) {
		T value = null;
		try {
			String name = mBundle.getString(_key);
			if(!name.equals(clazz.getName())){
				return defaul;
			}
			value = mJson.fromJson(mBundle.getString(_key+"[0]"), clazz);
		} catch (Throwable e) {
			// TODO: handle exception
		}
		return value == null ? defaul : value;
	}
	
	public <T> List<T> getList(String _key,Class<T> clazz) {
		return getList(_key,null,clazz);
	}
	
	public <T> List<T> getList(String _key,List<T> defaul,Class<T> clazz) {
		List<T> value = null;
		try {
			value = new ArrayList<T>();
			Integer length = mJson.fromJson(mBundle.getString(_key), Integer.class);
			for (int i = 0; i < length; i++) {
				T t = mJson.fromJson(mBundle.getString(_key+"["+i+"]"), clazz);
				if(t != null){
					value.add(t);
				}
			}
		} catch (Throwable e) {
			// TODO: handle exception
			if(value != null && value.size() == 0){
				value = null;
			}
		}
		return value == null ? defaul : value;
	}

	public String getString(String _key, String _default) {
		// TODO Auto-generated method stub
		return get(_key,_default,String.class);
	}

	public short getShort(String _key, short _default) {
		// TODO Auto-generated method stub
		return get(_key,_default,Short.class);
	}

	public boolean getBoolean(String _key, boolean _default) {
		// TODO Auto-generated method stub
		return get(_key,_default,Boolean.class);
	}

	public int getInt(String _key, int _default) {
		// TODO Auto-generated method stub
		return get(_key,_default,Integer.class);
	}

	public long getLong(String _key, long _default) {
		// TODO Auto-generated method stub
		return get(_key,_default,Long.class);
	}

	public float getFloat(String _key, float _default) {
		// TODO Auto-generated method stub
		return get(_key,_default,Float.class);
	}

	public double getDouble(String _key, double _default) {
		// TODO Auto-generated method stub
		return get(_key,_default,Double.class);
	}

	public UUID getUUID(String _key, UUID _default) {
		// TODO Auto-generated method stub
		return get(_key,_default,UUID.class);
	}
}
