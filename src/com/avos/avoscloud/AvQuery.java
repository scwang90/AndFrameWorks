package com.avos.avoscloud;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by SCWANG on 2017/4/22.
 */

public class AvQuery<T extends AVObject> extends AVQuery<T> {

    public static Map<String, String> mQueryMap = new ArrayMap<>();

    public AvQuery(String theClassName, Class<T> clazz) {
        super(theClassName, clazz);
    }

    @Override
    public List<T> find() throws AVException {
        List<T> list = super.find();
        mQueryMap.put(getQueryPath(), getClassName());
        return list;
    }

    @Override
    public int count() throws AVException {
        int count = super.count();
        mQueryMap.put(getQueryPath(), getClassName());
        return count;
    }

    public static <T extends AVObject > boolean clearCachedResultFor(Class<T> clazz) {
        String tableName = AvObject.getTableName(clazz);
        List<String> keys = new ArrayList<>(mQueryMap.size());
        AVCacheManager manager = AVCacheManager.sharedInstance();
        for (Map.Entry<String, String> entry : mQueryMap.entrySet()) {
            if (TextUtils.equals(tableName, entry.getValue())) {
                manager.delete(entry.getKey());
                keys.add(entry.getKey());
            }
        }
        for (String key : keys) {
            mQueryMap.remove(key);
        }
        return !keys.isEmpty();
    }

}
