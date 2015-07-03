package com.andframe.caches;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;

/**
 * AfJsonCache
 * @author 树朾
 * 	包装 AfSharedPreference 主要实现
 * 		1.对任意对象 Object 的支持
 * 				public void put(String key, Object value) 
 * 				public void put(String key, Object value,Class<?> clazz)
 * 		2.对任意 List 的支持
 * 				putList(String key, List<T> values,Class<?> clazz)
 * 				public<T> List<T> getList(String key,Class<T> clazz) 
 */
public class AfJsonCache 
{
	private Gson mJson = new Gson();
    private AfSharedPreference mShared = null;

    public AfJsonCache(AfSharedPreference preference) {
		// TODO Auto-generated constructor stub
    	mShared = preference;
	}
    
    public AfJsonCache(Context context,String name){
    	mShared = new AfSharedPreference(context,name);
    }
    
    public AfJsonCache(Context context,String name,int mode){
    	mShared = new AfSharedPreference(context,name,mode);
    }
    
    public AfJsonCache(Context context,String path,String name) throws Exception{
    	mShared = new AfSharedPreference(context,path,name);
    }

	public void put(String key, Object value) {
		// TODO Auto-generated method stub
    	put(key, value,value.getClass());
	}
    
    public void put(String key, Object value,Class<?> clazz) {
		// TODO Auto-generated method stub
    	mShared.putString(key, mJson.toJson(value, clazz));
	}
    /**
     * 保存列表数据
     * （新的列表会覆盖原来的所有内容）
     * @param key
     * @param values
     * @param clazz
     */
    public<T> void putList(String key, List<T> values,Class<T> clazz) {
		// TODO Auto-generated method stub
    	List<String> set = new ArrayList<String>();
    	for (T value : values) {
    		set.add(mJson.toJson(value, clazz));
		}
    	mShared.putStringList(key, set);
	}
    /**
     * 追加列表数据 
     * （新的内容会和老的内容一起保存）
     * @param key
     * @param values
     * @param clazz
     */
    public<T> void pushList(String key, List<T> values,Class<T> clazz) {
		// TODO Auto-generated method stub
    	List<String> set = new ArrayList<String>(mShared.getStringList(key, new ArrayList<String>()));
    	for (T value : values) {
    		set.add(mJson.toJson(value, clazz));
		}
    	mShared.putStringList(key, set);
	}
    
    public<T> T get(String key,Class<T> clazz) {
    	return get(key,null, clazz);
    }
    
    public<T> T get(String key, T defaul,Class<T> clazz) {
		T value = null;
		try {
			String svalue = mShared.getString(key, "");
			value = mJson.fromJson(svalue, clazz);
		} catch (Throwable e) {
			// TODO: handle exception
		}
		return value == null ? defaul : value;
	}
    /**
     * 获取列表缓存
     * @param key
     * @param clazz
     * @return 即使缓存不存在 也不会返回null 而是空列表
     */
    public<T> List<T> getList(String key,Class<T> clazz) {
    	List<T> list = new ArrayList<T>();
		try {
			List<String> set = mShared.getStringList(key, new ArrayList<String>());
			for (String string : set) {
				list.add(mJson.fromJson(string, clazz));
			}
		} catch (Throwable e) {
			// TODO: handle exception
		}
		return list;
	}

    public void clear(){
    	mShared.clear();
    }
    
	public boolean isEmpty(String key) {
		// TODO Auto-generated method stub
		try {
			return mShared.getString(key, "").equals("")&&
					mShared.getStringSet(key, new HashSet<String>()).isEmpty();
		} catch (Throwable e) {
			// TODO: handle exception
			return true;
		}
	}

    public boolean getBoolean(String key, boolean value)
    {
        return get(key, value, Boolean.class);
    }

    public String getString(String key, String value)
    {
        return get(key, value, String.class);
    }

    public float getFloat(String key, float value)
    {
        return get(key, value, Float.class);
    }

    public int getInt(String key, int value)
    {
        return get(key, value, Integer.class);
    }

    public long getLong(String key, long value)
    {
        return get(key, value, Long.class);
    }

    public Date getDate(String key, Date value)
    {
        return get(key, value, Date.class);
    }
}
