package com.andframe.impl.storage;

import android.app.Application;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import com.andframe.$;
import com.andframe.api.storage.StorageManager;
import com.andframe.util.android.PackageUtility;

import java.io.File;

public class DefaultStorageManager implements StorageManager {

    private final Application app;

    public DefaultStorageManager(@NonNull Application app) {
        this.app = app;
    }

    /**
     * 获取并创建 APP 工作目录
     * 优先在 扩展卡 上创建（注意先获取权限），否则在 APP 私有目录中创建
     * 工作目录将会在 SDK 根目录中以 AppName 开头存在
     * @param type 工作目录中的子类型目录，如 download 、 caches 、images 等
     */
    @Override
    public File workspaceDir(String type) {
        File workspace;
        if (externalExisting()) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            workspace = new File(sdcard + "/" + PackageUtility.getAppName(app) + "/" + type);
            if (!workspace.exists() && !workspace.mkdirs()) {
                return privateDir(type);
            }
        } else {
            return privateDir(type);
        }
        return workspace;
    }

    /**
     * 获取私有工作目录
     * @param type 工作目录中的子类型目录，如 download 、 caches 、images 等
     */
    @Override
    public File privateDir(String type) {
        File file = new File(app.getFilesDir(), type);
        if (!file.exists() && !file.mkdirs()) {
            $.error().handle("获取私有路径失败","DefaultStorageManager.privateDir");
        }
        return file;
    }

    /**
     * 获取扩展卡工作目录
     * 如果获取失败，将自动转为 获取私有工作目录 privateDir
     * @param type 工作目录中的子类型目录，如 download 、 caches 、images 等
     */
    @Override
    public File publicDir(String type) {
        File file = app.getExternalFilesDir(type);
        if (file == null || !file.exists() && !file.mkdirs()) {
            return privateDir(type);
        }
        return file;
    }

    @Override
    public File downloadDir() {
        File dir = externalPublicDir(Environment.DIRECTORY_DOWNLOADS);
        if (dir != null) {
            File download = new File(dir, PackageUtility.getAppName(app));
            if (!download.exists() && !download.mkdirs()) {
                $.error().handle("创建下载目录失败","DefaultStorageManager.downloadDir");
            }
            return download;
        }
        return null;
    }

    @Override
    public File downloadFile(String fileName) {
        File dir = downloadDir();
        if (dir != null) {
            return new File(dir, fileName);
        }
        return null;
    }

    @Override
    public File albumDir() {
        File dir = externalPublicDir(Environment.DIRECTORY_DCIM);
        if (dir != null) {
            File download = new File(dir, PackageUtility.getAppName(app));
            if (!download.exists() && !download.mkdirs()) {
                $.error().handle("创建相册目录失败","DefaultStorageManager.downloadDir");
            }
            return download;
        }
        return null;
    }

    @Override
    public File albumFile(String fileName) {
        File dir = albumDir();
        if (dir != null) {
            return new File(dir, fileName);
        }
        return null;
    }

    @Override
    public File screenshotDir() {
        File dir = externalPublicDir(Environment.DIRECTORY_DCIM);
        if (dir != null) {
            File download = new File(dir, "screenshots");
            if (!download.exists() && !download.mkdirs()) {
                $.error().handle("创建截图目录失败","DefaultStorageManager.downloadDir");
            }
            return download;
        }
        return null;
    }

    @Override
    public File screenshotFile(String fileName) {
        File dir = screenshotDir();
        if (dir != null) {
            return new File(dir, fileName);
        }
        return null;
    }

    @Override
    public File dir(String name, int mode) {
        return app.getDir(name, mode);
    }

    @Override
    public File obbDir() {
        return app.getObbDir();
    }

    @Override
    public File filesDir() {
        return app.getFilesDir();
    }

    @Override
    public File cacheDir() {
        return app.getCacheDir();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public File cacheCodeDir() {
        return app.getCodeCacheDir();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public File dataDir() {
        return app.getDataDir();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public File noBackupFilesDir() {
        return app.getNoBackupFilesDir();
    }

    @Override
    public File externalCacheDir() {
        return app.getExternalCacheDir();
    }

    @Override
    public File externalFilesDir(String type) {
        return app.getExternalFilesDir(type);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public File[] obbDirs() {
        return app.getObbDirs();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public File[] externalCacheDirs() {
        return app.getExternalCacheDirs();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public File[] externalFilesDirs(String type) {
        return app.getExternalFilesDirs(type);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public File[] externalMediaDirs() {
        return app.getExternalMediaDirs();
    }

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
    @Override
    public File externalPublicDir(String type) {
        //Environment.getExternalStorageDirectory()
        return Environment.getExternalStoragePublicDirectory(type);
    }

    /**
     * 获取扩展卡根目录
     * @see Environment#getExternalStorageDirectory()
     * @see Environment#getExternalStorageState()
     * @see Environment#isExternalStorageRemovable()
     * @return 如果用户已将此目录安装到其计算机上、已从设备中删除或发生了其他问题，则此目录当前可能无法访问。
     */
    @Override
    public File externalDir() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 判断扩展卡是否可热拔插
     * @see Environment#isExternalStorageRemovable()
     * @return 返回主共享/外部存储媒体是否在物理上可移动。
     */
    @Override
    public boolean externalRemovable() {
        return Environment.isExternalStorageRemovable();
    }

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
    @Override
    public String externalState() {
        return Environment.getExternalStorageState();
    }

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
    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public String externalState(File path) {
        return Environment.getExternalStorageState(path);
    }

    /**
     * 是否存在扩展卡
     *
     * @see Environment#getExternalStorageState()
     */
    @Override
    public boolean externalExisting() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 返回下载/缓存内容目录。
     *
     * @see Environment#getDownloadCacheDirectory()
     */
    @Override
    public File downloadCacheDir() {
        return Environment.getDownloadCacheDirectory();
    }
}
