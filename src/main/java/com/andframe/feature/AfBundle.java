package com.andframe.feature;

import android.os.Bundle;
import android.os.Parcelable;

import com.andframe.api.Extrater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AfBundle
 *
 * @author 树朾
 *         包装 Android Bundle 支持任意对象传输
 */
@SuppressWarnings("unused")
public class AfBundle implements Extrater<AfBundle> {

    protected Bundle mBundle;

    public AfBundle() {
        mBundle = new Bundle();
    }

    public AfBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public AfBundle put(String _key, Object value) {
        if (value != null && !putOrgin(_key, value)) {
            if (value.getClass().isArray()) {
                putList(_key, (Object[]) value);
            } else if (value instanceof List) {
                putList(_key, (List<?>) value);
            } else {
                mBundle.putString(_key + "[o]", value.getClass().getName());
                mBundle.putString(_key + "[0]", AfJsoner.toJson(value));
            }
        }
        return this;
    }

    public Object getOrgin(String _key, Object defaul, Class<?> clazz) {
        if (!mBundle.containsKey(_key)) {
            return null;
        } else if (clazz.equals(int[].class)) {
            return mBundle.getIntArray(_key);
        } else if (clazz.equals(short[].class)) {
            return mBundle.getShortArray(_key);
        } else if (clazz.equals(long[].class)) {
            return mBundle.getLongArray(_key);
        } else if (clazz.equals(float[].class)) {
            return mBundle.getFloatArray(_key);
        } else if (clazz.equals(double[].class)) {
            return mBundle.getDoubleArray(_key);
        } else if (clazz.equals(byte[].class)) {
            return mBundle.getByteArray(_key);
        } else if (clazz.equals(char[].class)) {
            return mBundle.getCharArray(_key);
        } else if (clazz.equals(boolean[].class)) {
            return mBundle.getBooleanArray(_key);
        } else if (clazz.equals(String[].class)) {
            return mBundle.getStringArray(_key);
        } else if (clazz.equals(CharSequence[].class)) {
            return mBundle.getCharSequenceArray(_key);
        } else if (clazz.equals(Parcelable[].class)) {
            return mBundle.getParcelableArray(_key);
        } else if (clazz.equals(int.class)) {
            return mBundle.getInt(_key, defaul == null ? 0 : (int) (defaul));
        } else if (clazz.equals(short.class)) {
            return mBundle.getShort(_key, defaul == null ? 0 : (short) (defaul));
        } else if (clazz.equals(long.class)) {
            return mBundle.getLong(_key, defaul == null ? 0 : (long) (defaul));
        } else if (clazz.equals(float.class)) {
            return mBundle.getFloat(_key, defaul == null ? 0 : (float) (defaul));
        } else if (clazz.equals(double.class)) {
            return mBundle.getDouble(_key, defaul == null ? 0 : (double) (defaul));
        } else if (clazz.equals(byte.class)) {
            return mBundle.getByte(_key, defaul == null ? 0 : (byte) (defaul));
        } else if (clazz.equals(char.class)) {
            return mBundle.getChar(_key, defaul == null ? 0 : (char) (defaul));
        } else if (clazz.equals(boolean.class)) {
            return mBundle.getBoolean(_key, defaul instanceof Boolean && (boolean) (defaul));
        } else if (clazz.equals(Integer.class)) {
            return defaulOrNull(_key, mBundle.getInt(_key, defaul == null ? 0 : (int) (defaul)), defaul);
        } else if (clazz.equals(Short.class)) {
            return defaulOrNull(_key, mBundle.getShort(_key, defaul == null ? 0 : (short) (defaul)), defaul);
        } else if (clazz.equals(Long.class)) {
            return defaulOrNull(_key, mBundle.getLong(_key, defaul == null ? 0 : (long) (defaul)), defaul);
        } else if (clazz.equals(Float.class)) {
            return defaulOrNull(_key, mBundle.getFloat(_key, defaul == null ? 0 : (float) (defaul)), defaul);
        } else if (clazz.equals(Double.class)) {
            return defaulOrNull(_key, mBundle.getDouble(_key, defaul == null ? 0 : (double) (defaul)), defaul);
        } else if (clazz.equals(Byte.class)) {
            return defaulOrNull(_key, mBundle.getByte(_key, defaul == null ? 0 : (byte) (defaul)), defaul);
        } else if (clazz.equals(Character.class)) {
            return defaulOrNull(_key, mBundle.getChar(_key, defaul == null ? 0 : (char) (defaul)), defaul);
        } else if (clazz.equals(Boolean.class)) {
            return defaulOrNull(_key, getBoolean(_key, defaul instanceof Boolean && (boolean) (defaul)), defaul);
        } else if (clazz.equals(String.class)) {
            return mBundle.getString(_key);
        } else if (clazz.equals(CharSequence.class)) {
            return mBundle.getCharSequence(_key);
        } else if (clazz.equals(Bundle.class)) {
            return mBundle.getBundle(_key);
        } else if (Parcelable.class.isAssignableFrom(clazz)) {
            return mBundle.getParcelable(_key);
        } else if (Serializable.class.isAssignableFrom(clazz)) {
            return mBundle.getSerializable(_key);
        }
        return null;
    }

    private Object defaulOrNull(String _key, Object obj, Object defaul) {
        if (obj != null && defaul == null) {
            if (!mBundle.containsKey(_key)) {
                return null;
            }
        }
        return obj;
    }

    private boolean putOrgin(String key, Object value) {
        boolean ret = true;
        if (value instanceof int[]) {
            mBundle.putIntArray(key, (int[]) value);
        } else if (value instanceof short[]) {
            mBundle.putShortArray(key, (short[]) value);
        } else if (value instanceof long[]) {
            mBundle.putLongArray(key, (long[]) value);
        } else if (value instanceof float[]) {
            mBundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof double[]) {
            mBundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof byte[]) {
            mBundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof char[]) {
            mBundle.putCharArray(key, (char[]) value);
        } else if (value instanceof boolean[]) {
            mBundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof String[]) {
            mBundle.putStringArray(key, (String[]) value);
        } else if (value instanceof CharSequence[]) {
            mBundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof Parcelable[]) {
            mBundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof Integer) {
            mBundle.putInt(key, (int) value);
        } else if (value instanceof Short) {
            mBundle.putShort(key, (short) value);
        } else if (value instanceof Long) {
            mBundle.putLong(key, (long) value);
        } else if (value instanceof Float) {
            mBundle.putFloat(key, (float) value);
        } else if (value instanceof Double) {
            mBundle.putDouble(key, (double) value);
        } else if (value instanceof Byte) {
            mBundle.putByte(key, (byte) value);
        } else if (value instanceof Character) {
            mBundle.putChar(key, (char) value);
        } else if (value instanceof Boolean) {
            mBundle.putBoolean(key, (boolean) value);
        } else if (value instanceof String) {
            mBundle.putString(key, (String) value);
        } else if (value instanceof CharSequence) {
            mBundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Bundle) {
            mBundle.putBundle(key, (Bundle) value);
        } else if (value instanceof Parcelable) {
            mBundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Serializable) {
            mBundle.putSerializable(key, (Serializable) value);
        } else {
            ret = false;
        }
        return ret;
    }

    public AfBundle putList(String _key, List<?> value) {
        int length = value.size();
        mBundle.putString(_key + "[o]", AfJsoner.toJson(length));
        for (int i = 0; i < length; i++) {
            mBundle.putString(_key + "[" + i + "]", AfJsoner.toJson(value.get(i)));
        }
        return this;
    }

    public AfBundle putList(String _key, Object[] value) {
        int length = value.length;
        mBundle.putString(_key + "[o]", AfJsoner.toJson(length));
        for (int i = 0; i < length; i++) {
            mBundle.putString(_key + "[" + i + "]", AfJsoner.toJson(value[i]));
        }
        return this;
    }

    public <T> T get(String _key, Class<T> clazz) {
        return get(_key, null, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String _key, T defaul, Class<T> clazz) {
        T value = null;
        try {
            value = (T) getOrgin(_key, defaul, clazz);
            if (value != null) {
                return value;
            }
            String name = mBundle.getString(_key + "[o]");
            if (!clazz.getName().equals(name) && !clazz.isPrimitive()) {
                Class<?> orgin = Class.forName(name);
                if (clazz.isAssignableFrom(orgin) && !orgin.isAnonymousClass()) {
                    clazz = (Class<T>) orgin;
                }
            }
            value = AfJsoner.fromJson(mBundle.getString(_key + "[0]"), clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value == null ? defaul : value;
    }

    /**
     * 获取List<T>
     *
     * @return 如果找不到返回 null， list 有可能为 0 个元素
     */
    public <T> List<T> getList(String _key, Class<T> clazz) {
        return getList(_key, null, clazz);
    }

    /**
     * /**
     * 获取List<T>
     *
     * @param defaul 如果找不到返回 defaul
     * @return 如果找不到返回 null， list 有可能为 0 个元素
     */
    @SuppressWarnings({"ConstantConditions","unchecked"})
    public <T> List<T> getList(String _key, List<T> defaul, Class<T> clazz) {
        List<T> value = null;
        try {
            value = new ArrayList<>();
            Integer length = AfJsoner.fromJson(mBundle.getString(_key + "[o]"), Integer.class);
            for (int i = 0; i < length; i++) {
                T t = AfJsoner.fromJson(mBundle.getString(_key + "[" + i + "]"), clazz);
                if (t != null) {
                    value.add(t);
                }
            }
        } catch (Throwable e) {
            Class<?>[] clazzs = {Serializable.class, Parcelable.class};
            for (Class<?> classz : clazzs){
                Object obj = getOrgin(_key, defaul, classz);
                if (obj instanceof List) {
                    List objs = (List) obj;
                    for (Object objt : objs) {
                        if (objt != null && clazz.isInstance(objt)) {
                            return (List<T>) objs;
                        }
                    }
                } else if (obj != null && obj.getClass().isArray()) {
                    Object[] objs = (Object[]) obj;
                    for (Object objt : objs) {
                        if (objt != null && clazz.isInstance(objt)) {
                            for (Object objts : objs) {
                                value.add((T) objts);
                            }
                            return value;
                        }
                    }
                }
            }
            if (value != null && value.size() == 0) {
                value = null;
            }
        }
        return value == null ? defaul : value;
    }

    public String getString(String _key, String _default) {
        return get(_key, _default, String.class);
    }

    public short getShort(String _key, short _default) {
        return get(_key, _default, Short.class);
    }

    public boolean getBoolean(String _key, boolean _default) {
        return get(_key, _default, Boolean.class);
    }

    public int getInt(String _key, int _default) {
        return get(_key, _default, Integer.class);
    }

    public long getLong(String _key, long _default) {
        return get(_key, _default, Long.class);
    }

    public float getFloat(String _key, float _default) {
        return get(_key, _default, Float.class);
    }

    public double getDouble(String _key, double _default) {
        return get(_key, _default, Double.class);
    }

    public String[] getStrings(String _key, String[] _default) {
        return get(_key, _default, String[].class);
    }

    public short[] getShorts(String _key, short[] _default) {
        return get(_key, _default, short[].class);
    }

    public boolean[] getBooleans(String _key, boolean[] _default) {
        return get(_key, _default, boolean[].class);
    }

    public int[] getInts(String _key, int[] _default) {
        return get(_key, _default, int[].class);
    }

    public long[] getLongs(String _key, long[] _default) {
        return get(_key, _default, long[].class);
    }

    public float[] getFloats(String _key, float[] _default) {
        return get(_key, _default, float[].class);
    }

    public double[] getDoubles(String _key, double[] _default) {
        return get(_key, _default, double[].class);
    }

    public UUID getUUID(String _key, UUID _default) {
        return get(_key, _default, UUID.class);
    }
}
