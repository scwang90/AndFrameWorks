package com.andframe.exception;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.andframe.$;
import com.andframe.application.AfApp;
import com.andframe.application.DialogService;
import com.andframe.caches.AfDurableCacher;
import com.andframe.model.Exceptional;
import com.andframe.task.Dispatcher;
import com.andframe.util.java.AfDateFormat;
import com.andframe.util.java.AfDateGuid;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertificateExpiredException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * 异常处理
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AfExceptionHandler implements UncaughtExceptionHandler {

    protected static final String DURABLE_HANDLER = "63214261915190904102";
    protected static final String DURABLE_UNCAUGHT = "02589350915190904102";

    protected static boolean mIsShowDialog = AfApp.get().isDebug();
    protected static AfExceptionHandler INSTANCE = null;
    protected Thread.UncaughtExceptionHandler mDefaultHandler;

    public AfExceptionHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static void register() {
        if (INSTANCE == null) {
            INSTANCE = AfApp.get().newExceptionHandler();
        }
    }

    public static AfExceptionHandler getInstance() {
        return INSTANCE;
    }

    public String onHandleTip(Throwable e, CharSequence tip) {
        Throwable ex = e;
        StringBuilder title = new StringBuilder(ex.getClass().getSimpleName());
        StringBuilder message = new StringBuilder(ex.getMessage() + "");
        boolean debug = AfApp.get().isDebug();

        for (int i = 0; i < 10; i++) {
            if (ex instanceof InvocationTargetException) {
                Throwable throwable = ((InvocationTargetException) ex).getTargetException();
                if (throwable != null && ex != throwable) {
                    ex = throwable;
                    message.append("\n    ←").append(ex.getMessage());
                    title.append("\n    ←").append(ex.getClass().getSimpleName());
                    continue;
                }
            } else if (ex instanceof UndeclaredThrowableException) {
                Throwable throwable = ((UndeclaredThrowableException) ex).getUndeclaredThrowable();
                if (throwable != null && ex != throwable) {
                    ex = throwable;
                    message.append("\n    ←").append(ex.getMessage());
                    title.append("\n    ←").append(ex.getClass().getSimpleName());
                    continue;
                }
            } else if (ex instanceof AfToastException) {
                return ex.getMessage();
            } else if (!debug && (ex instanceof ConnectException || e instanceof UnknownHostException)) {
                return "连接服务器失败";
            } else if (!debug && (ex instanceof SocketTimeoutException || e instanceof ConnectTimeoutException)) {
                return "网络请求超时";
            } else if (!debug && ex instanceof TimeoutException) {
                return "请求超时";
            } else if (ex instanceof CertificateExpiredException) {
                return "服务器安全证书过期";
            }

            Throwable throwable = ex.getCause();
            if (throwable != null && ex != throwable) {
                ex = throwable;
                message.append("\n    ←").append(ex.getMessage());
                title.append("\n    ←").append(ex.getClass().getSimpleName());
            } else {
                break;
            }
        }

        if (debug) {
            message.insert(0, "内容:");
            message = new StringBuilder(String.format("异常:%s\r\n%s", title.toString(), message.toString()));
            if (tip != null && !tip.equals("")) {
                return String.format("消息:%s\r\n%s", tip, message.toString());
            }
        } else {
            message = new StringBuilder("[" + e.getMessage() + "]");
            if (TextUtils.isEmpty(message)) {
                message = new StringBuilder("[" + e.getClass().getName() + "]");
            }
            if (tip != null && !tip.equals("")) {
                return tip + ":" + message;
            }
        }
        return message.toString();
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        try {
            ex.printStackTrace();
            final String msg = saveUncaughtException(ex);
            DialogService.dialog(AfApp.get(), "程序崩溃了", msg, AfDateGuid.NewID());
//            final Activity activity = $.pager().currentActivity();
//            if (activity != null && mIsShowDialog) {
//                AfDispatcher.dispatch(() -> doShowDialog(activity, "程序崩溃了", msg, msg1 -> {
////                    if (Looper.getMainLooper().getThread() == thread) {
////                        mDefaultHandler.uncaughtException(thread, ex);
////                    }
//                    return false;
//                }), 1000);
////            } else {
////                mDefaultHandler.uncaughtException(thread, ex);
//            }
//            if (Looper.getMainLooper().getThread() == thread) {
//                AfReflecter.setMemberByType(Looper.class, null, Looper.class);
//                ThreadLocal local = AfReflecter.getMemberByType(Looper.class, ThreadLocal.class);
//                //noinspection ConstantConditions,unchecked
//                local.set(null);
//                Looper.prepareMainLooper();
//                Looper.loop();
//            }
//            return;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    public void onHandleAttachException(Throwable ex, String remark, final String handlerId) {
        try {
            final String msg = saveHandleAttachException(ex, remark);
            final Activity activity = $.pager().currentActivity();
            if (activity != null && mIsShowDialog) {
                Dispatcher.dispatch(() -> doShowDialog(activity, "异常捕捉", msg, handlerId), 1000);
            }
        } catch (Throwable ignored) {
        }
    }

    public void onHandleException(Throwable ex, String remark, final String handlerId) {
        try {
            final String msg = saveHandleException(ex, remark);
            final Activity activity = $.pager().currentActivity();
            if (activity != null && mIsShowDialog) {
                Dispatcher.dispatch(() -> doShowDialog(activity, "异常捕捉", msg, handlerId));
            }
        } catch (Throwable ignored) {
        }
    }


    public String saveUncaughtException(Throwable ex) {
        try {
            final String msg = formatException(ex, "程序崩溃");
            return saveExceptionFile(msg, "error-");
        } catch (Throwable ignored) {
            return "";
        }
    }

    public String saveHandleException(Throwable ex, String remark) {
        try {
            final String msg = formatException(ex, remark);
            return saveExceptionFile(msg, "handler-");
        } catch (Throwable ignored) {
            return "";
        }
    }
    public String saveHandleAttachException(Throwable ex, String remark) {
        try {
            final String msg = formatException(ex, "Attach-" + remark);
            return saveExceptionFile(msg, "attach-");
        } catch (Throwable ignored) {
            return "";
        }
    }

    @NonNull
    protected String saveExceptionFile(String msg, String prefix) throws IOException {
        String path = AfDurableCacher.getPath();
        FileWriter writer = new FileWriter(path + "/" + prefix + AfDateFormat.format("y-M-d$HH-mm-ss", new Date()) + ".txt");
        writer.write(msg);
        writer.close();
        return msg;
    }

    @NonNull
    public String formatException(Throwable ex, String remark) {
        try {
            return "时间:" + AfDateFormat.FULL.format(new Date()) +
                            "\r\n\r\n备注:\r\n" + remark +
                            "\r\n\r\n异常:\r\n" + getExceptionName(ex) +
                            "\r\n\r\n信息:\r\n" + getExceptionMessage(ex) +
                            "\r\n\r\n快捷:\r\n" + getPackageStackTraceInfo(ex) +
                            "\r\n\r\n堆栈:\r\n" + getStackTraceInfo(ex);
        } catch (Throwable ignored) {
            return "";
        }
    }

    public static Exceptional getHandler(Throwable ex, String remark) {
        return getHandler(Thread.currentThread(), ex, remark);
    }

    public static Exceptional getHandler(Thread thread, Throwable ex, String remark) {

        Exceptional ehandler = new Exceptional();
        ehandler.Thread = "异常线程：" + thread;

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        ehandler.Remark = remark;
        ehandler.Name = getExceptionName(ex);
        ehandler.Message = getExceptionMessage(ex);
        ehandler.Stack = getAllStackTraceInfo(ex);
        ehandler.Version = AfApp.get().getVersion();
        ehandler.Device = android.os.Build.MODEL;
        ehandler.OpeateSystem = android.os.Build.VERSION.RELEASE;
        ehandler.ScreneWidth = metrics.widthPixels;
        ehandler.ScreneHeight = metrics.heightPixels;
        return ehandler;
    }

    public static String tip(Throwable e, CharSequence tip) {
        if (INSTANCE != null) {
            return INSTANCE.onHandleTip(e, tip);
        }
        return tip + "";
    }

    public static void handleAttach(Throwable ex, String remark) {
        if (INSTANCE != null && !(isToastException(ex))) {
            String handlerId;
            StackTraceElement[] stacks = ex.getStackTrace();
            if (stacks != null && stacks.length > 0) {
                handlerId = stacks[0].toString();
            } else {
                handlerId = AfDateGuid.NewID();
            }
            INSTANCE.onHandleAttachException(ex, remark, handlerId);
        }
    }

    public static void handle(String message, String remark) {
        if (INSTANCE != null) {
            String handlerId = "" + (message + remark).hashCode();
            Throwable ex = new Exception(message);
            ex.printStackTrace();
            INSTANCE.onHandleException(ex, remark, handlerId);
        }
    }

    public static void handle(Throwable ex, String remark) {
        if (INSTANCE != null && !isToastException(ex)) {
            ex.printStackTrace();
            String handlerId;
            StackTraceElement[] stacks = ex.getStackTrace();
            if (stacks != null && stacks.length > 0) {
                handlerId = stacks[0].toString();
            } else {
                handlerId = AfDateGuid.NewID();
            }
            INSTANCE.onHandleException(ex, remark, handlerId);
        }
    }

    private static boolean isToastException(Throwable ex) {
        int index = 0;
        while (ex != null && index < 10) {
            if (ex instanceof AfToastException) {
                return true;
            }
            ex = ex.getCause();
            index++;
        }
        return false;
    }

    public static String getAllStackTraceInfo(Throwable ex) {
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append("本地推栈:\r\n");
        sb.append(getPackageStackTraceInfo(ex));
        sb.append("\r\n全部堆栈:\r\n");
        sb.append(getStackTraceInfo(ex));
        return sb.toString();
    }

    public static String getExceptionMessage(Throwable ex) {
        String message = ex.getMessage();
        ex = ex.getCause();
        if (ex != null) {
            String format = "%s\r\n-------------------------->\r\n%s";
            message = String.format(format, getExceptionMessage(ex), message);
        }
        return message;
    }

    public static String getExceptionName(Throwable ex) {
        String name = ex.getClass().toString();
        ex = ex.getCause();
        if (ex != null) {
            String format = "%s -> %s";
            name = String.format(format, getExceptionName(ex), name);
        }
        return name;
    }

    public static String getPackageStackTraceInfo(Throwable ex) {
        String TraceInfo = "";
        String Package = AfApp.get().getPackageName() + ".";
        for (StackTraceElement stack : ex.getStackTrace()) {
            if (stack.getClassName().startsWith(Package)) {
                TraceInfo += stack.toString() + "\r\n";
            }
        }
        if (ex.getCause() != null) {
            String info = getPackageStackTraceInfo(ex.getCause());
            if (info != null && info.length() > 0) {
                String format = "%s\r\n-------------------------->\r\n%s";
                TraceInfo = String.format(format, info, TraceInfo);
            }
        }
        return TraceInfo;
    }

    public static String getStackTraceInfo(Throwable ex) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getClass().toString());
        builder.append("\r\n");
        for (StackTraceElement stack : ex.getStackTrace()) {
            builder.append(stack);
            builder.append("\r\n");
        }
        String TraceInfo = builder.toString();
        ex = ex.getCause();
        if (ex != null) {
            String format = "%s\r\n-------------------------->\r\n%s";
            TraceInfo = String.format(format, getStackTraceInfo(ex), TraceInfo);
        }
        return TraceInfo;
    }

    public static HashMap<String, String> mDialogMap = new HashMap<>();

//    public static synchronized void doShowDialog(Context activity, String title, String msg, Callback callback, Looper looper, String id) {
//        if (mDialogMap.containsKey(id)) {
//            return;
//        }
//        final String tid = id;
//        final String ttitle = title;
//        final String tmsg = msg;
//        final Callback tcallback = callback;
//        final Looper tLooper = looper;
//        final Builder dialog = new Builder(activity);
//        new Thread(() -> {
//            try {
//                Looper.prepare();
//                mDialogMap.put(tid, ttitle);
//                dialog.setTitle(ttitle);
//                dialog.setCancelable(false);
//                dialog.setMessage(tmsg);
//                dialog.setNeutralButton("我知道了",
//                        (dialog1, which) -> {
//                            dialog1.dismiss();
//                            mDialogMap.remove(tid);
//                            if (tLooper != null && tcallback != null) {
//                                new Handler(tLooper, tcallback).sendMessage(Message.obtain());
//                            } else if (tcallback != null) {
//                                tcallback.handleMessage(Message.obtain());
//                            }
//                            Handler handler = new Handler() {
//                                @Override
//                                public void handleMessage(Message msg1) {
//                                    Looper looper1 = Looper.myLooper();
//                                    if (looper1 != null) {
//                                        looper1.quit();
//                                    }
//                                }
//                            };
//                            handler.sendMessageDelayed(Message.obtain(), 300);
//                        });
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            mDialogMap.remove(tid);
//                        }
//                    });
//                }
//                dialog.show();
//                Looper.loop();
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }) {
//        }.start();
//    }

    public static void doShowDialog(Context activity, String title, String msg, String id) {
//        doShowDialog(activity, title, msg, null, null, id);
        DialogService.dialog(AfApp.get(), title, msg, id);
    }

//    public static void doShowDialog(Context activity, String title, String msg, Callback callback) {
//        doShowDialog(activity, title, msg, callback, null, AfDateGuid.NewID());
//    }

//    public static void doShowDialog(Context activity, String title, String msg, Callback callback, String id) {
//        doShowDialog(activity, title, msg, callback, null, id);
//    }
}
