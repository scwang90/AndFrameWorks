package com.andframe.api;

import java.util.Date;
import java.util.List;

/**
 * 缓存器接口
 * Created by SCWANG on 2016/11/2.
 */
public interface Cacher {

    Cacher put(String key, Object value);

    Cacher putList(String key, List<?> values);

    Cacher putList(String key, Object[] values);

    Cacher pushList(String key, List values);

    <T> T get(String key, Class<T> clazz);

    <T> T get(String key, T defaul, Class<T> clazz);

    <T> List<T> getList(String key, Class<T> clazz);

    void clear();

    boolean isEmpty(String key);

    boolean getBoolean(String key, boolean value);

    String getString(String key, String value);

    float getFloat(String key, float value);

    int getInt(String key, int value);

    long getLong(String key, long value);

    Date getDate(String key, Date value);
}
