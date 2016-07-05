package com.andframe.thread;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;

import com.andframe.application.AfApplication;
import com.andframe.application.AfDaemonThread;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfFileUtil;
import com.andframe.util.java.AfMD5;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 下载器类
 * @author 树朾
 */
@SuppressWarnings("deprecation")
public class AfDownloader {
    /**
     * 广播接收
     */
    public static final String FILE_NOTIFICATION = "com.andframe.thread.AfDownloader";

    /**
     * 异步下载 url 到 path 文件名为 name
     * @param url  下载链接
     * @param path 下载目录
     * @param name 下载文件名
     */
    public static void download(String url, String path, String name) {
        DownloadEntity entity = new DownloadEntity();
        entity.Name = name;
        entity.DownloadUrl = url;
        entity.DownloadPath = path;
        AfDaemonThread.postTask(new DownloadTask(entity, null));
    }

    /**
     * 异步下载 url 到 path
     * @param url  下载链接
     * @param path 下载路径
     */
    public static void download(String url, String path) {
        DownloadEntity entity = new DownloadEntity();
        entity.DownloadUrl = url;
        entity.DownloadPath = path;
        AfDaemonThread.postTask(new DownloadTask(entity, null));
    }

    /**
     * 异步下载 url 到 path
     * @param url  下载链接
     * @param path 下载路径
     */
    public static void download(String url, String path, DownloadListener listener) {
        DownloadEntity entity = new DownloadEntity();
        entity.DownloadUrl = url;
        entity.DownloadPath = path;
        AfDaemonThread.postTask(new DownloadTask(entity, listener));
    }

    /**
     * 异步下载
     * @param entity 下载配置实体
     */
    public static void download(DownloadEntity entity) {
        AfDaemonThread.postTask(new DownloadTask(entity, null));
    }

    /**
     * 异步下载
     * @param entity   下载配置实体
     * @param listener 加载进度监听器
     */
    public static void download(DownloadEntity entity, DownloadListener listener) {
        AfDaemonThread.postTask(new DownloadTask(entity, listener));
    }

    /**
     * 判断 url是否正在下载
     * @param entity 下载实体
     * @return true 正在下载 false 没有在下载
     */
    public static boolean isDownloading(DownloadEntity entity) {
        return isDownloading(entity.DownloadUrl);
    }

    /**
     * 判断 url是否正在下载
     * @param url 下载url
     * @return true 正在下载 false 没有在下载
     */
    public static boolean isDownloading(String url) {
        for (int i = 0; i < DownloadTask.mltDownloading.size(); i++) {
            DownloadTask task = DownloadTask.mltDownloading.get(i);
            if (TextUtils.equals(task.mEndityUrl, url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断 url是否正在下载
     * @param tag 下载绑定目标
     * @return true 正在下载 false 没有在下载
     */
    public static boolean isDownloading(Object tag) {
        for (int i = 0; i < DownloadTask.mltDownloading.size(); i++) {
            DownloadTask task = DownloadTask.mltDownloading.get(i);
            if (task.mEndity.tag == tag) {
                return true;
            }
        }
        return false;
    }

    /**
     * 新的绑定进度监听器
     * @param entity   下载配置实体
     * @param listener 加载进度监听器
     * @return 返回最新的监听器
     * 如果等于 传入的监听器 绑定成功 否则 上一个监听器拒绝新的绑定
     * 如果null 匹配不到下载任务
     */
    public static DownloadListener setListener(DownloadEntity entity, DownloadListener listener) {
        return setListener(entity.DownloadUrl, listener);
    }

    /**
     * 新的绑定进度监听器
     * @param url      下载路径
     * @param listener 加载进度监听器
     * @return 返回最新的监听器
     * 如果等于 传入的监听器 绑定成功 否则 上一个监听器拒绝新的绑定
     * 如果null 匹配不到下载任务
     */
    public static DownloadListener setListener(String url, DownloadListener listener) {
        for (int i = 0; i < DownloadTask.mltDownloading.size(); i++) {
            DownloadTask task = DownloadTask.mltDownloading.get(i);
            if (listener != null && TextUtils.equals(task.mEndityUrl, url)) {
                DownloadListener cListener = task.setDownloadListener(listener);
                if (cListener != listener && cListener instanceof DownloadManagerListener
                        && listener instanceof DownloadViewerListener) {
                    DownloadViewerListener vListener = (DownloadViewerListener) listener;
                    DownloadManagerListener mListener = (DownloadManagerListener) cListener;
                    if (mListener.setDownloadListener(task.mEndity, vListener)) {
                        return vListener;
                    }
                }
                return cListener;
            }
        }
        return null;
    }

    /**
     * 任务栏配置
     */
    public static class NotifyEntity {

        public String ContentTitle;
        public String FinishText;
        public String FailText;

        /**
         * @return "附件下载XXXX"
         */
        public String getContentTitle() {
            if (ContentTitle == null) {
                return "文件下载";
            }
            return ContentTitle;
        }

        /**
         * @return "文件XXXXX下载完成,大小XXXX"
         */
        public String getFinishText() {
            if (FinishText == null) {
                return "文件下载完成";
            }
            return FinishText;
        }

        /**
         * @return "文件XXXXX下载失败"
         */
        public String getFailText() {
            if (FailText == null) {
                return "文件下载失败";
            }
            return FailText;
        }

    }

    public static class DownloadEntity {
        /**
         * 下载后的文件名，影响 DownloadPath的意义
         */
        public String Name = "";
        public String DownloadUrl = "";
        /**
         * 大小，开始下载时下载器赋值，传入时忽略
         */
        public String Size = "";
        /**
         * 当 Name 有值时候 表示目录,否则全路径
         */
        public String DownloadPath = "";
        /**
         * 任务栏下载通知配置
         */
        public NotifyEntity Notify;
        /**
         * 额外绑定项
         */
        public Object tag;

        private boolean isDownloaded = false;

        String Error = "";
        Throwable Excption = null;

        public boolean isDownloaded() {
            return isDownloaded;
        }

        void setDownloaded() {
            isDownloaded = true;
        }

        /**
         * 判断是否下载失败
         */
        public boolean isDownloadFail() {
            return Excption != null;
        }

        /**
         * 获取错误信息
         */
        public String getError() {
            return Error;
        }

        /**
         * 获取异常信息
         */
        public Throwable getExcption() {
            return Excption;
        }

        /**
         * 获取下载目录
         */
        public String getDir() {
            if (!TextUtils.isEmpty(Name)) {
                return DownloadPath;
            } else {
                return new File(DownloadPath).getParent();
            }
        }

        /**
         * 获取下载全路径
         */
        public String getFullPath() {
            if (!TextUtils.isEmpty(Name)) {
                return DownloadPath + "/" + Name;
            } else {
                return DownloadPath;
            }
        }
        /**
         * 获取唯一ID
         */
        public String getId() {
            return AfMD5.getMD5(DownloadUrl + DownloadPath + Name);
        }
    }

    public interface DownloadListener {
        /**
         * 下载开始
         */
        void onDownloadStart(DownloadEntity entity);

        /**
         * 接口脱离
         * @return false 拒绝脱离 true 同意脱离
         */
        boolean onBreakAway(DownloadEntity entity);

        /**
         * 下载进度
         *
         * @param rate   百分比
         * @param loaded 已下载长度
         * @param total  文件总大小
         */
        void onDownloadProgress(DownloadEntity entity,float rate, long loaded, long total);

        /**
         * 通知栏点击事件
         *
         * @param endity 下载实体描述
         */
        void notifyClick(DownloadEntity endity);

        /**
         * 下载完成
         *
         * @return true 已经处理 false 没有处理（影响到notifyClick）
         */
        boolean onDownloadFinish(DownloadEntity entity);

        /**
         * 下载失败
         *
         * @param error 错误信息
         * @param e     错误异常
         * @return true 已经处理 false 没有处理（影响到notifyClick）
         */
        boolean onDownloadFail(DownloadEntity entity,String error, Throwable e);
    }

    /**
     * 常用监听接口
     */
    public static abstract class DownloadViewerListener implements DownloadListener {

        @Override
        public void onDownloadStart(DownloadEntity entity) {

        }

        @Override
        public boolean onBreakAway(DownloadEntity entity) {
            return true;//同意脱离
        }

        @Override
        public void notifyClick(DownloadEntity endity) {

        }

        @Override
        public boolean onDownloadFinish(DownloadEntity entity) {
            return false;
        }

        @Override
        public boolean onDownloadFail(DownloadEntity entity,String error, Throwable e) {
            return false;
        }

    }

    /**
     * 下载管理监听接口
     */
    public static abstract class DownloadManagerListener implements DownloadListener {

        protected DownloadViewerListener listener;

        public DownloadManagerListener() {
        }

        public DownloadManagerListener(DownloadViewerListener listener) {
            super();
            this.listener = listener;
        }

        @Override
        public void onDownloadStart(DownloadEntity entity) {
            if (listener != null) {
                listener.onDownloadStart(entity);
            }
        }

        @Override
        public boolean onBreakAway(DownloadEntity entity) {
            return false;//同意脱离
        }

        @Override
        public void onDownloadProgress(DownloadEntity entity, float rate, long loaded, long total) {
            if (listener != null) {
                listener.onDownloadProgress(entity, rate, loaded, total);
            }
        }

        @Override
        public boolean onDownloadFinish(DownloadEntity entity) {
            return listener != null && listener.onDownloadFinish(entity);
        }

        @Override
        public boolean onDownloadFail(DownloadEntity entity, String error, Throwable e) {
            return listener != null && listener.onDownloadFail(entity, error, e);
        }

        /**
         * 绑定新的监听器
         *
         * @return true 绑定成功 false 上一个监听器 拒绝
         */
        boolean setDownloadListener(DownloadEntity entity, DownloadViewerListener listener) {
            if (this.listener == null || this.listener.onBreakAway(entity)) {
                this.listener = listener;
                return true;
            }
            return false;
        }
    }

    protected static class Notifier {

        private static Random rand = new Random();

        private Builder mBuilder;
        private NotifyEntity mEotify;
        private NotificationManager mManager;
        private int notifyid = 1000 + rand.nextInt(1000);

        public Notifier(Context context, DownloadEntity entity) {
            mEotify = entity.Notify;
            mBuilder = new Builder(context);
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);

//			if (Build.VERSION.SDK_INT < 11) {
            int flag = PendingIntent.FLAG_CANCEL_CURRENT;
            AfIntent intent = new AfIntent(FILE_NOTIFICATION);
            intent.put(FILE_NOTIFICATION, entity.DownloadUrl);
            PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, flag);
            mBuilder.setContentIntent(pintent);
//			}

            // 构造 Manager
            String server = Context.NOTIFICATION_SERVICE;
            mManager = (NotificationManager) context.getSystemService(server);
        }

        public void notifyProgress(int max, int precent, boolean indeterminate) {
            mBuilder.setProgress(max, precent, false);// 设置为false，表示刻度
            mBuilder.setContentText("已下载 " + precent + "% ");
            mManager.notify(notifyid, mBuilder.build());
        }

        public void notifyStart() {
            mBuilder.setContentTitle(mEotify.getContentTitle());
            mBuilder.setTicker(mEotify.getContentTitle());
//			mBuilder.setTicker("正在下载...");
            mBuilder.setAutoCancel(false);
            mBuilder.setOngoing(true);
            mManager.notify(notifyid, mBuilder.build());
        }

        public void notifyFinish() {
            mBuilder.setProgress(100, 100, false);// 设置为false，表示刻度
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
            mBuilder.setTicker(mEotify.getFinishText());
            mBuilder.setContentText(mEotify.getFinishText());
//			mBuilder.setContentText("文件下载完成，大小"+mBack.Size);
//			mBuilder.setTicker("背景下载完成，点击设置");
            mBuilder.setAutoCancel(true);
            mBuilder.setOngoing(false);

            mManager.notify(notifyid, mBuilder.build());
        }

        public void notifyFail() {
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
            mBuilder.setTicker(mEotify.getFailText());
            mBuilder.setContentText(mEotify.getFailText());
            mBuilder.setAutoCancel(true);
            mBuilder.setOngoing(false);
            mManager.notify(notifyid, mBuilder.build());
        }

        public void cancel() {
            mManager.cancel(notifyid);
        }

    }

    public static class DownloadTask extends AfTask implements Handler.Callback {

        public static final int DOWNLOAD_FINISH = 20;
        public static final int DOWNLOAD_PROGRESS = 10;

        private Handler mHandler;
        private DownloadEntity mEndity;
        private Notifier mNotifier;
        private String mEndityUrl;
        private String mDownloadPath;
        private DownloadListener mListener;
        private File mTempFile;
        private int mPrecent = 0;

        private long mCount = 0;
        private long mTotal = 0;
        /**
         * 正在下载任务列表
         */
        private static List<DownloadTask> mltDownloading = new ArrayList<>();
        /**
         * 成功或者失败 通知栏任务列表
         */
        private static Map<String, DownloadTask> mNotifyMap = new HashMap<>();


        public DownloadTask(DownloadEntity endity, DownloadListener listener) {
            mEndity = endity;
            mListener = listener;
            mEndityUrl = endity.DownloadUrl;
            mDownloadPath = endity.getFullPath();
            mHandler = new Handler(Looper.getMainLooper(),this);
        }

        /**
         * 重新绑定监听器
         *
         * @param listener 新的监听器
         * @return 返回最新的监听器
         * 如果等于 传入的监听器 绑定成功 否则 上一个监听器拒绝新的绑定
         */
        public DownloadListener setDownloadListener(DownloadListener listener) {
            if (mListener == null || mListener.onBreakAway(mEndity)) {
                mListener = listener;
                if (mListener != null) {
                    mListener.onDownloadStart(mEndity);
                }
            }
            return mListener;
        }

        @Override
        protected boolean onPrepare() {
            mltDownloading.add(this);
            // 构造 通知
            if (mEndity.Notify != null) {
                mNotifier = new Notifier(AfApplication.getApp(), mEndity);
                mNotifier.notifyStart();
            }
            if (mListener != null) {
                mListener.onDownloadStart(mEndity);
            }
            return super.onPrepare();
        }

        @Override
        protected void onWorking() throws Exception {
            if (!mEndity.isDownloaded()) {

                HttpGet get = new HttpGet(mEndityUrl);
                HttpResponse response = new DefaultHttpClient().execute(get);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();

                //创建文件并开始下载
                mTempFile = new File(mDownloadPath);
                if (mTempFile.exists()) {
                    mTempFile.delete();
                } else {
                    String spath = mTempFile.getParent();
                    if (spath != null) {
                        File path = new File(spath);
                        if (!path.exists()) {
                            path.mkdirs();
                        }
                    }
                }
                mTempFile.createNewFile();

                // 创建一个新的写入流，讲读取到的图像数据写入到文件中
                FileOutputStream fos = new FileOutputStream(mTempFile);
                // 已读出流作为参数创建一个带有缓冲的输出流
                BufferedInputStream bis = new BufferedInputStream(is);
                // 已写入流作为参数创建一个带有缓冲的写入流
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                int read, precent;
                byte[] buffer = new byte[1024];
                long now, last = System.currentTimeMillis();
                long count = 0, length = entity.getContentLength();
                mEndity.Size = AfFileUtil.getFileSize(length);
                mCount = count;
                mTotal = length;
                mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_PROGRESS, this));
                while ((read = bis.read(buffer)) != -1 && !mIsCanceled) {
                    count += read;
                    bos.write(buffer, 0, read);
                    precent = (int) (((double) count / length) * 100);
                    // 每下载完成3%就通知任务栏进行修改下载进度
                    now = System.currentTimeMillis();
                    if (precent - mPrecent >= 3 || now - last > 500) {
                        last = now;
                        mPrecent = precent;
                        mCount = count;
                        mTotal = length;
                        mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_PROGRESS, this));
                    }
                }
                bos.flush();
                fos.flush();
                bos.close();
                fos.close();
                bis.close();
                is.close();

                if (mIsCanceled) {
                    mTempFile.delete();
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINISH, this));
                }
                mIsCanceled = true;
            } else {
                mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINISH, this));
            }
        }

        public void notifyClick() {
            if (mListener != null) {
                mListener.notifyClick(mEndity);
            }
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (isFinish()) {
                if (msg.what == DOWNLOAD_PROGRESS) {
                    // 更新状态栏上的下载进度信息
                    if (mNotifier != null) {
                        mNotifier.notifyProgress(100, mPrecent, false);
                    }
                    if (mListener != null) {
                        mListener.onDownloadProgress(mEndity, 0.01f * mPrecent, mCount, mTotal);
                    }
                } else if (msg.what == DOWNLOAD_FINISH) {
                    mEndity.setDownloaded();
                    mltDownloading.remove(this);
                    boolean neednotify = true;
                    if (mListener != null && mListener.onDownloadFinish(mEndity)) {
                        neednotify = false;
                    }
                    if (mNotifier != null) {
                        if (neednotify) {
                            mNotifier.notifyFinish();
                            mNotifyMap.put(mEndityUrl, this);
                        } else {
                            mNotifier.cancel();
                        }
                    }
                }
            } else {
                mltDownloading.remove(this);
                boolean neednotify = true;
                if (mListener != null && mListener.onDownloadFail(mEndity, mErrors, mException)) {
                    neednotify = false;
                }
                if (mNotifier != null) {
                    if (neednotify) {
                        mEndity.Error = mErrors;
                        mEndity.Excption = mException;
                        mNotifier.notifyFail();
                        mNotifyMap.put(mEndityUrl, this);
                    } else {
                        mNotifier.cancel();
                    }
                }
            }
            return true;
        }
    }

    protected static class DownloadBroadcast extends BroadcastReceiver {

        // 该方法用于实现接收到广播的具体处理，其中参数intent：为接受到的intent
        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取意图的动作
            try {
                if (FILE_NOTIFICATION.equals(intent.getAction())) {
                    String Url = new AfIntent(intent).getString(FILE_NOTIFICATION, null);
                    DownloadTask task = DownloadTask.mNotifyMap.get(Url);
                    if (task != null) {
                        task.notifyClick();
                        DownloadTask.mNotifyMap.remove(Url);
                    }
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, "DownloadBroadcast.onReceive");
            }
        }
    }

    static {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(FILE_NOTIFICATION);
            DownloadBroadcast receiver = new DownloadBroadcast();
            AfApplication.getApp().registerReceiver(receiver, filter);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "DownloadBroadcast.registerReceiver error");
        }
    }
}
