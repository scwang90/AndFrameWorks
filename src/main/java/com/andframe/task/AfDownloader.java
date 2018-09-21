package com.andframe.task;

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
import android.text.format.Formatter;

import com.andframe.$;
import com.andframe.application.AfApp;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfIntent;
import com.andframe.util.java.AfMD5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
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
    public static final String FILE_NOTIFICATION = "com.andframe.task.AfDownloader";

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
        $.task().postTask(new DownloadTask(entity, null));
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
        $.task().postTask(new DownloadTask(entity, null));
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
        $.task().postTask(new DownloadTask(entity, listener));
    }

    /**
     * 异步下载
     * @param entity 下载配置实体
     */
    public static void download(DownloadEntity entity) {
        $.task().postTask(new DownloadTask(entity, null));
    }

    /**
     * 异步下载
     * @param entity   下载配置实体
     * @param listener 加载进度监听器
     */
    public static void download(DownloadEntity entity, DownloadListener listener) {
        $.task().postTask(new DownloadTask(entity, listener));
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
            if (TextUtils.equals(task.mEntityUrl, url)) {
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
            if (task.mEntity.tag == tag) {
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
            if (listener != null && TextUtils.equals(task.mEntityUrl, url)) {
                DownloadListener cListener = task.setDownloadListener(listener);
                if (cListener != listener && cListener instanceof DownloadManagerListener
                        && listener instanceof DownloadViewerListener) {
                    DownloadViewerListener vListener = (DownloadViewerListener) listener;
                    DownloadManagerListener mListener = (DownloadManagerListener) cListener;
                    if (mListener.setDownloadListener(task.mEntity, vListener)) {
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
        Throwable Exception = null;

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
            return Exception != null;
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
        public Throwable getException() {
            return Exception;
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
        void onDownloadProgress(DownloadEntity entity, float rate, long loaded, long total);

        /**
         * 通知栏点击事件
         *
         * @param entity 下载实体描述
         */
        void notifyClick(DownloadEntity entity);

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
        boolean onDownloadFail(DownloadEntity entity, String error, Throwable e);
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
        public void notifyClick(DownloadEntity entity) {

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
        private NotifyEntity mEntity;
        private NotificationManager mManager;
        private int notifyId = 1000 + rand.nextInt(1000);

        public Notifier(Context context, DownloadEntity entity) {
            mEntity = entity.Notify;
            mBuilder = new Builder(context);
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);

//			if (Build.VERSION.SDK_INT < 11) {
            int flag = PendingIntent.FLAG_CANCEL_CURRENT;
            AfIntent intent = new AfIntent(FILE_NOTIFICATION);
            intent.put(FILE_NOTIFICATION, entity.DownloadUrl);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, flag);
            mBuilder.setContentIntent(pIntent);
//			}

            // 构造 Manager
            String server = Context.NOTIFICATION_SERVICE;
            mManager = (NotificationManager) context.getSystemService(server);
        }

        public void notifyProgress(int max, int precent, boolean indeterminate) {
            mBuilder.setProgress(max, precent, false);// 设置为false，表示刻度
            mBuilder.setContentText("已下载 " + precent + "% ");
            mManager.notify(notifyId, mBuilder.build());
        }

        public void notifyStart() {
            mBuilder.setContentTitle(mEntity.getContentTitle());
            mBuilder.setTicker(mEntity.getContentTitle());
//			mBuilder.setTicker("正在下载...");
            mBuilder.setAutoCancel(false);
            mBuilder.setOngoing(true);
            mManager.notify(notifyId, mBuilder.build());
        }

        public void notifyFinish() {
            mBuilder.setProgress(100, 100, false);// 设置为false，表示刻度
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
            mBuilder.setTicker(mEntity.getFinishText());
            mBuilder.setContentText(mEntity.getFinishText());
//			mBuilder.setContentText("文件下载完成，大小"+mBack.Size);
//			mBuilder.setTicker("背景下载完成，点击设置");
            mBuilder.setAutoCancel(true);
            mBuilder.setOngoing(false);

            mManager.notify(notifyId, mBuilder.build());
        }

        public void notifyFail() {
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
            mBuilder.setTicker(mEntity.getFailText());
            mBuilder.setContentText(mEntity.getFailText());
            mBuilder.setAutoCancel(true);
            mBuilder.setOngoing(false);
            mManager.notify(notifyId, mBuilder.build());
        }

        public void cancel() {
            mManager.cancel(notifyId);
        }

    }

    public static class DownloadTask extends AfTask implements Handler.Callback {

        public static final int DOWNLOAD_FINISH = 20;
        public static final int DOWNLOAD_PROGRESS = 10;

        private Handler mHandler;
        private DownloadEntity mEntity;
        private Notifier mNotifier;
        private String mEntityUrl;
        private String mDownloadPath;
        private File mTempFile;
        private WeakReference<DownloadListener> mListener;

        private int mPercent = 0;
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


        public DownloadTask(DownloadEntity entity, DownloadListener listener) {
            mEntity = entity;
            mListener = new WeakReference<>(listener);
            mEntityUrl = entity.DownloadUrl;
            mDownloadPath = entity.getFullPath();
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
            DownloadListener oldListener = mListener == null ? null : mListener.get();
            if (oldListener == null || oldListener.onBreakAway(mEntity)) {
                mListener = new WeakReference<>(listener);
                if (listener != null) {
                    listener.onDownloadStart(mEntity);
                }
            }
            return mListener == null ? null : mListener.get();
        }

        @Override
        protected boolean onPrepare() {
            mltDownloading.add(this);
            // 构造 通知
            if (mEntity.Notify != null) {
                mNotifier = new Notifier(AfApp.get(), mEntity);
                mNotifier.notifyStart();
            }
            DownloadListener listener = mListener == null ? null : mListener.get();
            if (listener != null) {
                listener.onDownloadStart(mEntity);
            }
            return super.onPrepare();
        }

        @Override
        protected void onWorking() throws Exception {
            if (!mEntity.isDownloaded()) {
                // 构造URL
                URL url = new URL(mEntityUrl);
                // 打开连接
                URLConnection con = url.openConnection();
//                //获得文件的长度
//                int contentLength = con.getContentLength();
                // 输入流
                InputStream input = new BufferedInputStream(con.getInputStream());

                //创建文件并开始下载
                mTempFile = new File(mDownloadPath);
//                if (mTempFile.exists() && mTempFile.length() == contentLength) {
//                    System.out.println("文件已经下载~");
//                    mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINISH, this));
//                    return;
//                }
                if (mTempFile.exists() && !mTempFile.delete()) {
                    throw new AfToastException("删除原有文件失败");
                }
                if (!mTempFile.getParentFile().exists() && !mTempFile.getParentFile().mkdirs()) {
                    throw new AfToastException("创建下载目录失败");
                }
                if (!mTempFile.createNewFile()) {
                    throw new AfToastException("创建下载文件失败");
                }

                // 创建一个新的写入流，讲读取到的图像数据写入到文件中
                OutputStream output = new BufferedOutputStream(new FileOutputStream(mTempFile));

                int read, percent;
                byte[] buffer = new byte[1024];
                long now, last = System.currentTimeMillis();
                long count = 0, length = con.getContentLength();
                mEntity.Size = Formatter.formatFileSize(AfApp.get(), length);
                mCount = count;
                mTotal = length;
                mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_PROGRESS, this));
                while ((read = input.read(buffer)) != -1 && mStatus != Status.canceled/*!mIsCanceled*/) {
                    count += read;
                    output.write(buffer, 0, read);
                    percent = (int) (((double) count / length) * 100);
                    // 每下载完成3%就通知任务栏进行修改下载进度
                    now = System.currentTimeMillis();
                    if ((percent - mPercent >= 3 && (now - last) > 250) || now - last > 500) {
                        mPercent = percent;
                        mCount = count;
                        mTotal = length;
                        mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_PROGRESS, this));
                        last = now;
                    }
                }
                output.flush();
                output.close();
                input.close();

                if (mStatus == Status.canceled/*mIsCanceled*/) {
                    //noinspection ResultOfMethodCallIgnored
                    mTempFile.delete();
                } else {
                    mPercent = 100;
                    mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINISH, this));
                }
                mStatus = Status.canceled;
                //mIsCanceled = true;
            } else {
                mPercent = 100;
                mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FINISH, this));
            }
        }

        public void notifyClick() {
            DownloadListener listener = mListener == null ? null : mListener.get();
            if (listener != null) {
                listener.notifyClick(mEntity);
            }
        }

        @Override
        protected void onException(Throwable e) {
            super.onException(e);
            mHandler.sendMessage(mHandler.obtainMessage(0, this));
        }

        @Override
        public boolean handleMessage(Message msg) {
            DownloadListener listener = mListener == null ? null : mListener.get();
            if (!failed()) {
                if (msg.what == DOWNLOAD_PROGRESS) {
                    // 更新状态栏上的下载进度信息
                    if (mNotifier != null) {
                        System.out.println("handleMessage:DOWNLOAD_PROGRESS:"+mPercent);
                        mNotifier.notifyProgress(100, mPercent, false);
                    }
                    if (listener != null) {
                        listener.onDownloadProgress(mEntity, 0.01f * mPercent, mCount, mTotal);
                    }
                } else if (msg.what == DOWNLOAD_FINISH) {
                    mEntity.setDownloaded();
                    mltDownloading.remove(this);
                    boolean needNotify = true;
                    if (listener != null && listener.onDownloadFinish(mEntity)) {
                        needNotify = false;
                    }
                    if (mNotifier != null) {
                        System.out.println("handleMessage:DOWNLOAD_FINISH:"+mPercent);
                        if (needNotify) {
                            mNotifier.notifyFinish();
                            mNotifyMap.put(mEntityUrl, this);
                        } else {
                            mNotifier.cancel();
                        }
                    }
                }
            } else {
                mltDownloading.remove(this);
                boolean needNotify = true;
                if (listener != null && listener.onDownloadFail(mEntity, mErrors, mException)) {
                    needNotify = false;
                }
                if (mNotifier != null) {
                    if (needNotify) {
                        mEntity.Error = mErrors;
                        mEntity.Exception = mException;
                        mNotifier.notifyFail();
                        mNotifyMap.put(mEntityUrl, this);
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
            AfApp.get().registerReceiver(receiver, filter);
        } catch (Throwable e) {
            AfExceptionHandler.handle(e, "DownloadBroadcast.registerReceiver error");
        }
    }
}
