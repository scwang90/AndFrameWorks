package com.andadvert.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Sd卡缓存
 * Created by SCWANG on 2017/3/15.
 */

public class SdCache extends ACache {

    private static Map<String, SdCache> mInstanceMap = new HashMap<>();

    public static SdCache getDurable(Context context) {
        return get(context, "Android/.sdcache/");
    }

    public static SdCache get(Context context) {
        return get(context, "SdCache");
    }

    public static SdCache get(Context context, String cachename) {
        return get(context, cachename, MAX_SIZE, MAX_COUNT);
    }

    public static SdCache get(Context context, String cachename, long max_zise, int max_count) {
        String key = cachename + myPid();
        SdCache manager = mInstanceMap.get(key);
        if (manager == null) {
            manager = new SdCache(context, cachename, max_zise, max_count);
            mInstanceMap.put(key, manager);
        }
        return manager;
    }

    private SdCache(Context context, String cachename, long max_size, int max_count) {
        super(getCacheDir(context, cachename), max_size, max_count);
    }

    private static File getCacheDir(Context context, String type) {
        File dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            dir = new File(sdcard + "/" + type);
            if (!dir.exists() && !dir.mkdirs()) {
                return getPrivatePath(context, type);
            }
        } else {
            return getPrivatePath(context, type);
        }
        return dir;
    }

    private static File getPrivatePath(Context context, String type) {
        File file = new File(context.getCacheDir().getAbsolutePath() + "/" + type);
        if (!file.exists() && !file.mkdirs()) {
            new IOException("获取私有路径失败").printStackTrace();
        }
        return file;
    }
}
