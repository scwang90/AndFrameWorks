package com.andframe.util.android;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import com.andframe.BuildConfig;
import com.andframe.util.java.AfReflecter;

public class PackageUtility {

    /**
     * 获取 APP 名称, 子类可以继承返回 getString(R.string.app_name);
     * @return APP名称
     */
    public static String getAppName(Application app) {
        try {
            String name = app.getPackageName();
            PackageManager manager = app.getPackageManager();
            ApplicationInfo info = manager.getApplicationInfo(name, 0);
            return manager.getApplicationLabel(info) + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Resources resources = app.getResources();
            int id = resources.getIdentifier(app.getPackageName() + ":string/app_name", null, null);
            if(id > 0){
                return resources.getString(id);
            }
            return "AndFrame";
        }
    }

    /**
     * 判断是否运行在 DEBUG 状态
     */
    private static Boolean isDebug = null;
    public static boolean isDebug(Application app) {
        if (isDebug != null) {
            return isDebug;
        }
        try {
            Class<?> clazz = Class.forName(app.getPackageName() + ".BuildConfig");
            return isDebug = Boolean.valueOf(true).equals(AfReflecter.getMember(clazz,"DEBUG"));
        } catch (Throwable ignored) {
        }
        return isDebug = BuildConfig.DEBUG;
    }
}
