package com.andframe.api.storage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import com.andframe.application.AfApp;

import java.io.File;

public interface StorageManager {

    //<editor-fold desc="框架封装 API">
    /**
     * 获取并创建 APP 工作目录
     * 优先在 扩展卡 上创建（注意先获取权限），否则在 APP 私有目录中创建
     * 工作目录将会在 SDK 根目录中以 AppName 开头存在
     * @param type 工作目录中的子类型目录，如 download 、 caches 、images 等
     */
    File workspaceDir(String type);

    /**
     * 获取私有工作目录
     * @param type 工作目录中的子类型目录，如 download 、 caches 、images 等
     */
    File privateDir(String type);

    /**
     * 获取扩展卡工作目录
     * 如果获取失败，将自动转为 获取私有工作目录 privateDir
     * @param type 工作目录中的子类型目录，如 download 、 caches 、images 等
     */
    File publicDir(String type);

    /**
     * 注意先要获取权限
     * 获取系统下载目录，并创建 AppName 目录文件夹 如：/sdcard/Download/[AppName]
     */
    File downloadDir();

    /**
     * 注意先要获取权限
     * 获取系统下载目录，并创建 AppName 目录文件夹 如：/sdcard/Download/[AppName]/[fileName]
     */
    File downloadFile(String fileName);

    /**
     * 注意先要获取权限
     * 获取系统相册目录，并创建 AppName 目录文件夹 如：/sdcard/Download/[AppName]
     */
    File albumDir();

    /**
     * 注意先要获取权限
     * 获取系统相册目录，并创建 AppName 目录文件夹 如：/sdcard/Download/[AppName]/[fileName]
     */
    File albumFile(String fileName);

    /**
     * 注意先要获取权限
     * 获取系统截屏目录，并创建文件夹，如：/sdcard/DCIM/screenshots
     */
    File screenshotDir();

    /**
     * 注意先要获取权限
     * 获取系统截屏目录，并创建文件夹，如：/sdcard/DCIM/screenshots/[fileName]
     */
    File screenshotFile(String fileName);
    //</editor-fold>


    //<editor-fold desc="Context 目录API">
    File dir(String name, int mode);

    File obbDir();
    File filesDir();
    File cacheDir();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    File cacheCodeDir();
    @RequiresApi(api = Build.VERSION_CODES.N)
    File dataDir();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    File noBackupFilesDir();

    File externalCacheDir();
    File externalFilesDir(String type);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    File[] obbDirs();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    File[] externalCacheDirs();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    File[] externalFilesDirs(String type);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    File[] externalMediaDirs();
    //</editor-fold>

    //<editor-fold desc="Environment 目录 API">
    /**
     * 获取系统公共目录，如相册、音乐等等
     * @param type The type of storage directory to return. Should be one of
     *            {@link Environment#DIRECTORY_MUSIC}, {@link Environment#DIRECTORY_PODCASTS},
     *            {@link Environment#DIRECTORY_RINGTONES}, {@link Environment#DIRECTORY_ALARMS},
     *            {@link Environment#DIRECTORY_NOTIFICATIONS}, {@link Environment#DIRECTORY_PICTURES},
     *            {@link Environment#DIRECTORY_MOVIES}, {@link Environment#DIRECTORY_DOWNLOADS},
     *            {@link Environment#DIRECTORY_DCIM}, or {@link Environment#DIRECTORY_DOCUMENTS}. May not be null.
     * @return Returns the File path for the directory. Note that this directory
     *         may not yet exist, so you must make sure it exists before using
     *         it such as with {@link File#mkdirs File.mkdirs()}.
     */
    File externalPublicDir(String type);

    /**
     * 获取扩展卡根目录
     * 如果用户已将此目录安装到其计算机上、已从设备中删除或发生了其他问题，则此目录当前可能无法访问。
     * @see Environment#getExternalStorageDirectory()
     * @see Environment#getExternalStorageState()
     * @see Environment#isExternalStorageRemovable()
     * @return 如果用户已将此目录安装到其计算机上、已从设备中删除或发生了其他问题，则此目录当前可能无法访问。
     */
    File externalDir();

    /**
     * 判断扩展卡是否可热拔插
     * @see Environment#isExternalStorageRemovable()
     * @return 返回主共享/外部存储媒体是否在物理上可移动。
     */
    boolean externalRemovable();

    /**
     * 获取扩展卡的当前状态。
     *
     * @see Environment#getExternalStorageState()
     * @see Environment#getExternalStorageDirectory()
     * @return 返回主共享/外部存储媒体的当前状态。
     *         one of {@link Environment#MEDIA_UNKNOWN}, {@link Environment#MEDIA_REMOVED},
     *         {@link Environment#MEDIA_UNMOUNTED}, {@link Environment#MEDIA_CHECKING},
     *         {@link Environment#MEDIA_NOFS}, {@link Environment#MEDIA_MOUNTED},
     *         {@link Environment#MEDIA_MOUNTED_READ_ONLY}, {@link Environment#MEDIA_SHARED},
     *         {@link Environment#MEDIA_BAD_REMOVAL}, or {@link Environment#MEDIA_UNMOUNTABLE}.
     */
    String externalState();

    /**
     * 获取扩展卡的当前状态。
     *
     * @see Environment#getExternalStorageState()
     * @see Environment#getExternalStorageDirectory()
     * @return 返回给定路径上共享/外部存储媒体的当前状态。
     *         one of {@link Environment#MEDIA_UNKNOWN}, {@link Environment#MEDIA_REMOVED},
     *         {@link Environment#MEDIA_UNMOUNTED}, {@link Environment#MEDIA_CHECKING},
     *         {@link Environment#MEDIA_NOFS}, {@link Environment#MEDIA_MOUNTED},
     *         {@link Environment#MEDIA_MOUNTED_READ_ONLY}, {@link Environment#MEDIA_SHARED},
     *         {@link Environment#MEDIA_BAD_REMOVAL}, or {@link Environment#MEDIA_UNMOUNTABLE}.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    String externalState(File path);

    /**
     * 是否存在扩展卡
     *
     * @see Environment#getExternalStorageState()
     */
    boolean externalExisting();

    /**
     * 返回下载/缓存内容目录。
     *
     * @see Environment#getDownloadCacheDirectory()
     */
    File downloadCacheDir();

    //</editor-fold>
}
