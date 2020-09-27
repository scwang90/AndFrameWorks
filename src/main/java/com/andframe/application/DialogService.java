package com.andframe.application;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.andframe.$;
import com.andframe.api.dialog.DialogBuilder;
import com.andframe.api.pager.Pager;
import com.andframe.impl.dialog.DefaultDialogBuilder;
import com.andframe.impl.dialog.SystemDialogFactory;

import java.lang.ref.WeakReference;

public class DialogService extends Service {

    public static void run(Context context) {
        if (context != null) {
            try {
                GetRunningPager.start(context);
                context.startService(new Intent(context, DialogService.class));
            } catch (Exception e) {
                $.error().handle(e, "AfDialogService.startService");
            }
        }
    }

    public static void dialog(Context context, String title, String message) {
        if (context != null) {
            if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context))) {
                Intent intent = new Intent(context, DialogService.class);
                intent.putExtra(Intent.EXTRA_TITLE, title);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                context.startService(intent);
            } else {
                Pager pager = $.pager().currentPager();
                if (pager != null && !pager.isRecycled()) {
                    new DefaultDialogBuilder(pager.getContext(), new SystemDialogFactory(), false)
                            .title(title)
                            .message(message)
                            .show();
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public static void dialog(Context context, String title, String message, String dialogId) {
        dialog(context, title, message);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent != null && flags == 0) {
                String title = intent.getStringExtra(Intent.EXTRA_TITLE);
                String message = intent.getStringExtra(Intent.EXTRA_TEXT);

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
                    DialogBuilder builder = new DefaultDialogBuilder(this, new SystemDialogFactory(), false);
                    Dialog dialog = builder.title(title).message(message).create();
                    Window window = dialog.getWindow();
                    if (window != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                        } else {
                            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        }
                    }
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected static abstract class ActivityLifecycleCallbacksAdapter implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    /**
     * 运行在主进程中
     */
    protected static class GetRunningPager extends ActivityLifecycleCallbacksAdapter implements Runnable {
        int runDelay = 4000;
        Handler handler = new Handler();
        WeakReference<Activity> weakReference = null;

        public static void start(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                AfApp.get().registerActivityLifecycleCallbacks(new GetRunningPager());
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (weakReference != null) {
                handler.removeCallbacksAndMessages(null);
            }
            weakReference = new WeakReference<>(activity);
            handler.postDelayed(this, runDelay);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (weakReference != null) {
                handler.removeCallbacksAndMessages(null);
                weakReference = null;
            }
        }

        @Override
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void run() {
            if (weakReference != null && !Settings.canDrawOverlays(AfApp.get())) {
                Activity activity = weakReference.get();
                if (activity != null) {
                    tipDialog(activity);
                    AfApp.get().unregisterActivityLifecycleCallbacks(this);
                } else {
                    weakReference = null;
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void tipDialog(Activity activity) {
            $.dialog(activity).builder()
                    .title("调试对话框 - 仅用于调试模式")
                    .message("AndFrame框架的异常管理模块，可以在APP异常或者崩溃的时候弹出调试对话框，这个对话框时全局的，需要获取全局对话框权限")
                    .doNotAsk()
                    .button("算了")
                    .button("立刻开启", (__,___) -> $.dispatch(new GetOverlayPermission()))
                    .show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected static class GetOverlayPermission implements Runnable {
        @Override
        public void run() {
            Pager pager = $.pager().currentPager();
            if (pager != null && !pager.isRecycled()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + pager.getContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pager.startActivity(intent);
            }
        }
    }

}
