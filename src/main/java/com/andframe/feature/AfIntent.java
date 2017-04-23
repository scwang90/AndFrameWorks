package com.andframe.feature;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.andframe.api.Extrater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AfIntent
 * 使用Gson实现多元数据传递
 * @author 树朾
 */
@SuppressLint("ParcelCreator")
@SuppressWarnings("unused")
public class AfIntent extends Intent implements Extrater<AfIntent> {

    public AfIntent() {
        super();
    }

    public AfIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public AfIntent(Intent o) {
        super(o != null ? o : new Intent());
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
        put(_key, value);
    }

    public AfIntent(Context context, Class<? extends Activity> clazz, Object... args) {
        this(context, clazz);
        putKeyVaules(args);
    }

    public AfIntent newTask() {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return this;
    }

    public AfIntent putKeyVaules(Object... args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length / 2; i++) {
                if (args[2 * i] instanceof String) {
                    Object arg = args[2 * i + 1];
                    if (arg != null && arg instanceof List) {
                        this.putList((String) args[2 * i], (List<?>) arg);
                    } else {
                        this.put((String) args[2 * i], arg);
                    }
                }
            }
        }
        return this;
    }

    public AfIntent put(String _key, Object value) {
        if (value != null && putOrgin(_key, value) == null) {
            if (value.getClass().isArray()) {
                putList(_key, (Object[]) value);
            } else if (value instanceof List) {
                putList(_key, (List<?>) value);
            } else {
                putExtra(_key + "[o]", value.getClass().getName());
                putExtra(_key + "[0]", AfJsoner.toJson(value));
            }
        }
        return this;
    }

    public Object getOrgin(String _key, Object defaul, Class<?> clazz) {
        if (!hasExtra(_key)) {
            return null;
        } else if (clazz.equals(int[].class)) {
            return getIntArrayExtra(_key);
        } else if (clazz.equals(short[].class)) {
            return getShortArrayExtra(_key);
        } else if (clazz.equals(long[].class)) {
            return getLongArrayExtra(_key);
        } else if (clazz.equals(float[].class)) {
            return getFloatArrayExtra(_key);
        } else if (clazz.equals(double[].class)) {
            return getDoubleArrayExtra(_key);
        } else if (clazz.equals(byte[].class)) {
            return getByteArrayExtra(_key);
        } else if (clazz.equals(char[].class)) {
            return getCharArrayExtra(_key);
        } else if (clazz.equals(boolean[].class)) {
            return getBooleanArrayExtra(_key);
        } else if (clazz.equals(String[].class)) {
            return getStringArrayExtra(_key);
        } else if (clazz.equals(CharSequence[].class)) {
            return getCharSequenceArrayExtra(_key);
        } else if (clazz.equals(Parcelable[].class)) {
            return getParcelableArrayExtra(_key);
        } else if (clazz.equals(int.class)) {
            return getIntExtra(_key, defaul == null ? 0 : (int) (defaul));
        } else if (clazz.equals(short.class)) {
            return getShortExtra(_key, defaul == null ? 0 : (short) (defaul));
        } else if (clazz.equals(long.class)) {
            return getLongExtra(_key, defaul == null ? 0 : (long) (defaul));
        } else if (clazz.equals(float.class)) {
            return getFloatExtra(_key, defaul == null ? 0 : (float) (defaul));
        } else if (clazz.equals(double.class)) {
            return getDoubleExtra(_key, defaul == null ? 0 : (double) (defaul));
        } else if (clazz.equals(byte.class)) {
            return getByteExtra(_key, defaul == null ? 0 : (byte) (defaul));
        } else if (clazz.equals(char.class)) {
            return getCharExtra(_key, defaul == null ? 0 : (char) (defaul));
        } else if (clazz.equals(boolean.class)) {
            return getBooleanExtra(_key, defaul != null && (boolean) (defaul));
        } else if (clazz.equals(Integer.class)) {
            return defaulOrNull(_key, getIntExtra(_key, defaul == null ? 0 : (int) (defaul)), defaul);
        } else if (clazz.equals(Short.class)) {
            return defaulOrNull(_key, getShortExtra(_key, defaul == null ? 0 : (short) (defaul)), defaul);
        } else if (clazz.equals(Long.class)) {
            return defaulOrNull(_key, getLongExtra(_key, defaul == null ? 0 : (long) (defaul)), defaul);
        } else if (clazz.equals(Float.class)) {
            return defaulOrNull(_key, getFloatExtra(_key, defaul == null ? 0 : (float) (defaul)), defaul);
        } else if (clazz.equals(Double.class)) {
            return defaulOrNull(_key, getDoubleExtra(_key, defaul == null ? 0 : (double) (defaul)), defaul);
        } else if (clazz.equals(Byte.class)) {
            return defaulOrNull(_key, getByteExtra(_key, defaul == null ? 0 : (byte) (defaul)), defaul);
        } else if (clazz.equals(Character.class)) {
            return defaulOrNull(_key, getCharExtra(_key, defaul == null ? 0 : (char) (defaul)), defaul);
        } else if (clazz.equals(Boolean.class)) {
            return defaulOrNull(_key, getBooleanExtra(_key, defaul != null && (boolean) (defaul)), defaul);
        } else if (clazz.equals(String.class)) {
            return getStringExtra(_key);
        } else if (clazz.equals(CharSequence.class)) {
            return getCharSequenceExtra(_key);
        } else if (clazz.equals(Bundle.class)) {
            return getBundleExtra(_key);
        } else if (Parcelable.class.isAssignableFrom(clazz)) {
            return getParcelableExtra(_key);
        } else if (Serializable.class.isAssignableFrom(clazz)) {
            return getSerializableExtra(_key);
        }
        return null;
    }

    private Object defaulOrNull(String _key, Object obj, Object defaul) {
        if (obj != null && defaul == null) {
            if (!this.hasExtra(_key)) {
                return null;
            }
        }
        return obj;
    }

    private Intent putOrgin(String key, Object value) {
        if (value instanceof int[]) {
            return putExtra(key, (int[]) value);
        } else if (value instanceof short[]) {
            return putExtra(key, (short[]) value);
        } else if (value instanceof long[]) {
            return putExtra(key, (long[]) value);
        } else if (value instanceof float[]) {
            return putExtra(key, (float[]) value);
        } else if (value instanceof double[]) {
            return putExtra(key, (double[]) value);
        } else if (value instanceof byte[]) {
            return putExtra(key, (byte[]) value);
        } else if (value instanceof char[]) {
            return putExtra(key, (char[]) value);
        } else if (value instanceof boolean[]) {
            return putExtra(key, (boolean[]) value);
        } else if (value instanceof String[]) {
            return putExtra(key, (String[]) value);
        } else if (value instanceof CharSequence[]) {
            return putExtra(key, (CharSequence[]) value);
        } else if (value instanceof Parcelable[]) {
            return putExtra(key, (Parcelable[]) value);
        } else if (value instanceof Integer) {
            return putExtra(key, (int) value);
        } else if (value instanceof Short) {
            return putExtra(key, (short) value);
        } else if (value instanceof Long) {
            return putExtra(key, (long) value);
        } else if (value instanceof Float) {
            return putExtra(key, (float) value);
        } else if (value instanceof Double) {
            return putExtra(key, (double) value);
        } else if (value instanceof Byte) {
            return putExtra(key, (byte) value);
        } else if (value instanceof Character) {
            return putExtra(key, (char) value);
        } else if (value instanceof Boolean) {
            return putExtra(key, (boolean) value);
        } else if (value instanceof String) {
            return putExtra(key, (String) value);
        } else if (value instanceof CharSequence) {
            return putExtra(key, (CharSequence) value);
        } else if (value instanceof Bundle) {
            return putExtra(key, (Bundle) value);
        } else if (value instanceof Parcelable) {
            return putExtra(key, (Parcelable) value);
        } else if (value instanceof Serializable) {
            return putExtra(key, (Serializable) value);
        }
        return null;
    }

    public AfIntent putList(String _key, List<?> value) {
        int length = value.size();
        putExtra(_key + "[o]", AfJsoner.toJson(length));
        for (int i = 0; i < length; i++) {
            putExtra(_key + "[" + i + "]", AfJsoner.toJson(value.get(i)));
        }
        return this;
    }

    public AfIntent putList(String _key, Object[] value) {
        int length = value.length;
        putExtra(_key + "[o]", AfJsoner.toJson(length));
        for (int i = 0; i < length; i++) {
            putExtra(_key + "[" + i + "]", AfJsoner.toJson(value[i]));
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
            String name = getStringExtra(_key + "[o]");
            if (name == null) {
                return defaul;
            }
            if (!clazz.getName().equals(name) && !clazz.isPrimitive()) {
                Class<?> orgin = Class.forName(name);
                if (clazz.isAssignableFrom(orgin) && !orgin.isAnonymousClass()) {
                    clazz = (Class<T>) orgin;
                }
            }
            value = AfJsoner.fromJson(getStringExtra(_key + "[0]"), clazz);
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
            Integer length = AfJsoner.fromJson(getStringExtra(_key + "[o]"), Integer.class);
            for (int i = 0; i < length; i++) {
                T t = AfJsoner.fromJson(getStringExtra(_key + "[" + i + "]"), clazz);
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
