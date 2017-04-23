package com.andframe.api;

import java.util.List;
import java.util.UUID;

/**
 * 使用Json实现多元数据传递
 * AfIntent
 * @author 树朾
 */
@SuppressWarnings("unused")
public interface Extrater<ST extends Extrater> {

	 ST put(String _key, Object value);

	 ST putList(String _key, List<? extends Object> value);

	 <T> T get(String _key,Class<T> clazz);

	 <T> T get(String _key,T defaul,Class<T> clazz);

	/**
	 * 获取List<T>
	 * @return 如果找不到返回 null， list 有可能为 0 个元素
	 */
	 <T> List<T> getList(String _key,Class<T> clazz);

	/**
	 * 获取List<T>
	 * @param defaul 如果找不到返回 defaul
	 * @return 如果找不到返回 null， list 有可能为 0 个元素
	 */
	 <T> List<T> getList(String _key,List<T> defaul,Class<T> clazz);

	 String getString(String _key, String _default) ;

	 short getShort(String _key, short _default) ;

	 boolean getBoolean(String _key, boolean _default) ;

	 int getInt(String _key, int _default);

	 long getLong(String _key, long _default) ;

	 float getFloat(String _key, float _default) ;

	 double getDouble(String _key, double _default) ;

	 UUID getUUID(String _key, UUID _default);
}
