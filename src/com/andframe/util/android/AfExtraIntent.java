package com.andframe.util.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AfExtraIntent {

	private static HashMap<String, Object> mHashMap = new HashMap<String, Object>();

	public static void put(String key, Object value) {
		mHashMap.put(key, value);
	}

	public static void putList(String key, List<? extends Object> list) {
		mHashMap.put(key, list);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getList(String key, Class<T> clazz) {
		Object obj = get(key);
		if (obj instanceof List) {
			List<? extends Object> list = (List<? extends Object>) obj;
			List<T> retlist = new ArrayList<T>();
			for (Object object : list) {
				if (clazz.isInstance(object)) {
					retlist.add(clazz.cast(object));
				}
			}
			return retlist;
		}
		return null;
	}

	public static Object get(String key) {
		return mHashMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObject(String key) {
		try {
			return (T)get(key);
		} catch (Throwable e){
			return null;
		}
	}

	public static <T> T get(String key, Class<T> clazz) {
		return get(key,null,clazz);
	}

	public static <T> T get(String key, T Default, Class<T> clazz) {
		Object obj = mHashMap.get(key);
		if (clazz.isInstance(obj)) {
			return clazz.cast(obj);
		}
		return Default;
	}

	public static Object get(String key, Object Default) {
		Object obj = mHashMap.get(key);
		if (obj != null) {
			return obj;
		}
		return Default;
	}

	public static String getString(String key, String Default) {
		Object obj = mHashMap.get(key);
		if (obj != null && obj instanceof String) {
			return obj.toString();
		}
		return Default;
	}

	public static short getShort(String key, short Default) {
		// TODO Auto-generated method stub
		Object obj = mHashMap.get(key);
		if (obj != null && obj instanceof Short) {
			return (Short) obj;
		}
		return Default;
	}

	public static boolean getBoolean(String key, boolean Default) {
		// TODO Auto-generated method stub
		Object obj = mHashMap.get(key);
		if (obj != null && obj instanceof Boolean) {
			return (Boolean) obj;
		}
		return Default;
	}

	public static int getInt(String key, int Default) {
		// TODO Auto-generated method stub
		Object obj = mHashMap.get(key);
		if (obj != null && obj instanceof Integer) {
			return (Integer) obj;
		}
		return Default;
	}

	public static long getLong(String key, long Default) {
		// TODO Auto-generated method stub
		Object obj = mHashMap.get(key);
		if (obj != null && obj instanceof Long) {
			return (Long) obj;
		}
		return Default;
	}

	public static float getFloat(String key, float Default) {
		// TODO Auto-generated method stub
		Object obj = mHashMap.get(key);
		if (obj != null && obj instanceof Float) {
			return (Float) obj;
		}
		return Default;
	}

	public static double getDouble(String key, double Default) {
		// TODO Auto-generated method stub
		Object obj = mHashMap.get(key);
		if (obj != null && obj instanceof Double) {
			return (Double) obj;
		}
		return Default;
	}

	public static UUID getUUID(String key, UUID Default) {
		// TODO Auto-generated method stub
		Object obj = mHashMap.get(key);
		if (obj != null && obj instanceof UUID) {
			return (UUID) obj;
		}
		return Default;
	}

}
