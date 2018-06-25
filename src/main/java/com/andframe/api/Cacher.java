package com.andframe.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 缓存器接口
 * Created by SCWANG on 2016/11/2.
 */
public interface Cacher {

    Cacher put(String key, Object value);

    Cacher putList(String key, List<?> values);

    Cacher putList(String key, Object[] values);

    Cacher pushList(String key, List values);

    @Nullable
    <T> T get(String key, Class<T> clazz);

    @Nullable
    <T> T get(String key, T defaul, Class<T> clazz);

    @NonNull
    <T> List<T> getList(String key, Class<T> clazz);

    void clear();

    void remove(String key);

    void remove(String... keys);

    boolean isEmpty(String key);

    boolean getBoolean(String key, boolean value);

    String getString(String key, String value);

    float getFloat(String key, float value);

    int getInt(String key, int value);

    long getLong(String key, long value);

    Date getDate(String key, Date value);

    Map<String, ?> getAll();
}
