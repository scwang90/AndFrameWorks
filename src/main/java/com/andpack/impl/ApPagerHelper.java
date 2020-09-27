package com.andpack.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.andframe.$;
import com.andframe.activity.AfActivity;
import com.andframe.annotation.interpreter.ReflecterCacher;
import com.andframe.api.pager.Pager;
import com.andframe.api.query.ViewQuery;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfFragment;
import com.andframe.listener.SafeListener;
import com.andframe.util.java.AfReflecter;
import com.andpack.R;
import com.andpack.activity.ApActivity;
import com.andpack.activity.ApFragmentActivity;
import com.andpack.annotation.BackgroundTranslucent;
import com.andpack.annotation.BindTitle;
import com.andpack.annotation.RegisterEventBus;
import com.andpack.annotation.interpreter.StatusBarInterpreter;
import com.andpack.api.ApPager;
import com.andpack.application.ApApp;
import com.andpack.fragment.ApFragment;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import permissions.dispatcher.PermissionRequest;

import static android.app.Activity.RESULT_OK;
import static androidx.core.graphics.ColorUtils.setAlphaComponent;
import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 * 页面基类帮助类
 * Created by SCWANG on 2016/9/3.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ApPagerHelper {

    private static final int REQUEST_INSTALL_PACKAGES = 1280;

    protected Pager pager;

    protected RegisterEventBus mEventBus;

    //<editor-fold desc="滑动关闭">
    private SwipeBackActivityHelper mSwipeBackHelper;
    protected boolean mIsUsingSwipeBack = false;
    protected Runnable doStorageRunnable;
    protected Runnable doCameraRunnable;
    protected Runnable doLocationRunnable;
    protected Runnable doInstallRunnable;
    protected Runnable doRecordAudioRunnable;
    protected Runnable doDeviceRunnable;
    //</editor-fold>

    public ApPagerHelper(Pager pager) {
        this.pager = pager;
        initRegisterEventBus();
    }

    protected static Map<Class, Method[]> mEventBusMap = new HashMap<>();

    private void initRegisterEventBus() {
        Method[] methods = mEventBusMap.get(pager.getClass());
        if (methods == null) {
            methods = ReflecterCacher.getMethodAnnotation(pager, Subscribe.class);
            //methods = AfReflecter.getMethodAnnotation(pager.getClass(), getStopClass(), Subscribe.class);
            mEventBusMap.put(pager.getClass(), methods);
        }
        if (methods.length > 0) {
            for (Method method : methods) {
                int modifiers = method.getModifiers();
                if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
                    pager.toast("被标记[Subscribe]的方法必须是public并且不能是static");
                    return;
                }
            }
            mEventBus = new RegisterEventBus() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return RegisterEventBus.class;
                }
            };
        }
    }

    @SuppressWarnings("WeakerAccess")
    @NonNull
    protected Class<?> getStopClass() {
        return pager instanceof Activity ? AfActivity.class : AfFragment.class;
    }

    public void setTheme(@StyleRes int resId) {
        mIsUsingSwipeBack = resId == R.style.AppTheme_SwipeBack;
    }

    public void setUsingSwipeBack(boolean using) {
        mIsUsingSwipeBack = using;
        if (mSwipeBackHelper != null) {
            SwipeBackLayout layout = mSwipeBackHelper.getSwipeBackLayout();
            if (layout != null) {
                layout.setEnableGesture(using);
            }
        }
        Activity activity = pager.getActivity();
        if (pager instanceof Fragment && activity instanceof ApFragmentActivity) {
            ((ApFragmentActivity) activity).mApHelper.setUsingSwipeBack(using);
        }
    }

    public void onCreate() {
        if (mEventBus != null) {
            EventBus.getDefault().register(pager);
        }
        if (mIsUsingSwipeBack) {
            mSwipeBackHelper = new SwipeBackActivityHelper(pager.getActivity());
            mSwipeBackHelper.onActivityCreate();
        }
    }

    public void onDestroy() {
        doStorageRunnable = null;
        doLocationRunnable = null;
        doCameraRunnable = null;
        if (mEventBus != null) {
            EventBus.getDefault().unregister(pager);
        }

        if (ApApp.getApp().isDebug() && pager instanceof Activity) {
            RefWatcher watcher = ApApp.getApp().getRefWatcher();
            if (watcher != null) {
                watcher.watch(pager);
            }
        }
    }

    public void onDestroyView() {
    }

    public void onViewCreated() {
        try {
            StatusBarInterpreter.interpreter(pager);
            if (pager.getView() != null) {
                $.query(pager.getView())
                        .query(Toolbar.class)
                        .foreach(Toolbar.class, (ViewQuery.ViewIterator<Toolbar>)
                                view -> {
                                    view.setNavigationOnClickListener(new SafeListener(
                                            (View.OnClickListener) v -> pager.finish()));

                                    Class<?> stop = pager instanceof Activity ? ApActivity.class : ApFragment.class;
                                    BindTitle title = AfReflecter.getAnnotation(pager.getClass(), stop, BindTitle.class);
                                    if (title != null) {
                                        if (title.value() != View.NO_ID) {
                                            view.setTitle(title.value());
                                        } else {
                                            view.setTitle(title.title());
                                        }
                                    }
                                });
            }
        } catch (Throwable e) {
            $.error().handle(e, ("ApPagerHelper.onViewCreated 失败"));
        }
    }

    public <T extends View> T findViewById(int id) {
        if (mSwipeBackHelper != null) {
            try {
                //noinspection unchecked
                return (T)mSwipeBackHelper.findViewById(id);
            } catch (Throwable e) {
                $.error().handle(e, ("SwipeBackActivityHelper.findViewById 失败"));
            }
        }
        return null;
    }

    public void onPostCreate(Bundle bundle) {
        try {
            if (mIsUsingSwipeBack && mSwipeBackHelper == null) {
                mSwipeBackHelper = new SwipeBackActivityHelper(pager.getActivity());
                mSwipeBackHelper.onActivityCreate();
            }
            if (mSwipeBackHelper != null) {
                mSwipeBackHelper.onPostCreate();
                SwipeBackLayout layout = mSwipeBackHelper.getSwipeBackLayout();
                performBackgroundTranslucent(layout);
                ViewGroup root = (ViewGroup)pager.getActivity().getWindow().getDecorView();
                FrameLayout frame = new FrameLayout(pager.getContext());
                for (int i = 0; i < root.getChildCount(); i++) {
                    View child = root.getChildAt(i);
                    if (child != layout) {
                        root.removeViewAt(i--);
                        frame.addView(child);
                    }
                }
                if (frame.getChildCount() > 0) {
                    View view = layout.getChildAt(0);
                    layout.removeViewAt(0);
                    layout.addView(frame);
                    frame.addView(view, 0);
                    AfReflecter.setPreciseMemberByType(layout, frame, View.class, SwipeBackLayout.class);
                }
            }
        } catch (Throwable e) {
            $.error().handle(e, ("SwipeBackActivityHelper.onPostCreate 失败"));
        }
    }

    @SuppressLint("Range")
    private void performBackgroundTranslucent(SwipeBackLayout layout) {
        BackgroundTranslucent translucent;
        if (pager instanceof ApFragmentActivity) {
            Class<?> fragment = ((ApFragmentActivity) pager).getFragmentClazz();
            translucent = getAnnotation(fragment, Fragment.class, BackgroundTranslucent.class);
        } else {
            translucent = getAnnotation(pager.getClass(), getStopClass(), BackgroundTranslucent.class);
        }
        if (translucent != null && layout.getChildCount() > 0) {
            View view = layout.getChildAt(0);
            if (view != null) {
                view.setBackgroundColor(0);
            }
            int color = ContextCompat.getColor(layout.getContext(), translucent.color());
            layout.setBackgroundColor(setAlphaComponent(color, (int) (255 * translucent.value())));
        }
    }

    public boolean finish() {
        try {
            if (mSwipeBackHelper != null) {
//                mSwipeBackHelper.getSwipeBackLayout().scrollToFinishActivity();
                mSwipeBackHelper = null;
//                return true;
            }
        } catch (Throwable e) {
            $.error().handle(e, ("SwipeBackActivityHelper.scrollToFinishActivity 失败"));
        }
        return false;
    }

    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    public void showRationaleForDevice(PermissionRequest request) {
        $.dialog(pager).builder()
                .title("获取设备权限失败")
                .message("获取设备权限失败，您有可能不能正确的接收到属于自己的消息，请授权设备权限")
                .button("取消", (dialog, which) -> request.cancel())
                .button("授权", (dialog, which) -> request.proceed())
                .show();
    }

    public void showRationaleForLocation(PermissionRequest request) {
        $.dialog(pager).builder()
                .title("获取定位权限失败")
                .message("获取定位权限失败，这将导致部分功能无法使用，请授权定位权限")
                .button("取消", (dialog, which) -> request.cancel())
                .button("授权", (dialog, which) -> request.proceed())
                .show();
    }

    public void showRationaleForStorage(PermissionRequest request) {
        $.dialog(pager).builder()
                .title("获取相册，媒体文件权限失败")
                .message("获取相册，媒体文件权限失败，这将导致图片文件资源选择，头像上传等功能无法使用，请授权权限")
                .button("取消", (dialog, which) -> request.cancel())
                .button("授权", (dialog, which) -> request.proceed())
                .show();
    }

    public void showRationaleForCamera(PermissionRequest request) {
        $.dialog(pager).builder()
                .title("请求相机权限")
                .message("请求相机权限失败，这将导致扫描二维码，拍照等功能无法使用，请授权权限。")
                .button("取消", (dialog, button) -> request.cancel())
                .button("授权", (dialog, button) -> request.proceed()).show();
    }

    public void showRationaleForInstall(PermissionRequest request) {
        $.dialog(pager).builder()
                .title("自动更新安装权限")
                .message("请求安装权限失败，这将导致自动更新等功能无法使用，请授权权限。")
                .button("取消", (dialog, button) -> request.cancel())
                .button("授权", (dialog, button) -> request.proceed()).show();
    }

    public void showRationaleForRecordAudio(PermissionRequest request) {
        $.dialog(pager).builder()
                .title("录音权限")
                .message("请求录音权限失败，这将导致语音发送等功能无法使用，请授权权限。")
                .button("取消", (dialog, button) -> request.cancel())
                .button("授权", (dialog, button) -> request.proceed()).show();
    }

    public void showDeniedForCamera() {
        this.doCameraRunnable = null;
        $.toaster(pager).toast("获取相机权限失败");
    }

    public void showNeverAskForCamera() {
        this.doCameraRunnable = null;
        $.toaster(pager).toast("相机权限被禁止，不再询问");
    }

    public void showDeniedForLocation() {
        this.doLocationRunnable = null;
        $.toaster(pager).toast("获取定位权限失败");
    }

    public void showNeverAskForLocation() {
        this.doLocationRunnable = null;
        $.toaster(pager).toast("定位权限被禁止，部分功能将无法使用");
    }

    public void showDeniedForDevice() {
        this.doDeviceRunnable = null;
        $.toaster(pager).toast("获取设备权限失败");
    }

    public void showNeverAskForDevice() {
        this.doDeviceRunnable = null;
        $.toaster(pager).toast("获取设备权限失败，部分功能将无法使用");
    }

    public void showDeniedForStorage() {
        this.doStorageRunnable = null;
        $.toaster(pager).toast("获取相册，媒体文件权限失败");
    }

    public void showNeverAskForStorage() {
        this.doStorageRunnable = null;
        $.toaster(pager).toast("相册，媒体文件权限被禁止，部分功能将无法使用");
    }

    public void showDeniedForInstall() {
        this.doInstallRunnable = null;
        $.toaster(pager).toast("获取安装权限失败，这将导致自动更新等功能无法使用");
    }

    public void showNeverAskForInstall() {
        $.toaster(pager).toast("安装权限失败被禁止，自动更新等功能无法使用");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Uri packageUri = Uri.parse("package:" + ApApp.getApp().getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
            pager.startActivity(intent);
        }
    }

    public void showDeniedForRecordAudio() {
        this.doRecordAudioRunnable = null;
        $.toaster(pager).toast("获取录音权限失败，这将导致语音发送等功能无法使用");
    }

    public void showNeverAskForRecordAudio() {
        this.doStorageRunnable = null;
        $.toaster(pager).toast("录音权限被禁止，语音发送等功能将无法使用");
    }

    public void doStorageWithPermissionCheck(Runnable runnable) {
        this.doStorageRunnable = runnable;
    }

    public void doStorage() {
        if (this.doStorageRunnable != null) {
            this.doStorageRunnable.run();
            this.doStorageRunnable = null;
        }
    }

    public void doCameraWithPermissionCheck(Runnable runnable) {
        this.doCameraRunnable = runnable;
    }

    public void doCamera() {
        if (this.doCameraRunnable != null) {
            this.doCameraRunnable.run();
            this.doCameraRunnable = null;
        }
    }

    public void doLocationWithPermissionCheck(Runnable runnable) {
        this.doLocationRunnable = runnable;
    }

    public void doLocation() {
        if (this.doLocationRunnable != null) {
            this.doLocationRunnable.run();
            this.doLocationRunnable = null;
        }
    }

    public void doDeviceWithPermissionCheck(Runnable runnable) {
        this.doDeviceRunnable = runnable;
    }

    public void doDevice() {
        if (this.doDeviceRunnable != null) {
            this.doDeviceRunnable.run();
            this.doDeviceRunnable = null;
        }
    }

    public void doInstallWithPermissionCheck(Runnable runnable) {
        this.doInstallRunnable = runnable;
    }

    public void doInstallWithPermissionCheck() {
        if (pager instanceof ApPager && pager.getActivity() != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                if (!pager.getActivity().getPackageManager().canRequestPackageInstalls()) {
                    $.dialog(pager).builder()
                            .title("权限获取")
                            .message("自动更新需要打开安装未知来源应用权限，请去设置中开启权限")
                            .button("取消更新",null)
                            .button("开启权限", (n,m)-> {
                                Uri packageUri = Uri.parse("package:"+ ApApp.getApp().getPackageName());
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                                ((ApPager) pager).startActivityForResult(intent, REQUEST_INSTALL_PACKAGES);
                            })
                            .show();
                    return;
                }
            }
            ((ApPager) pager).doInstall();
        }

    }

    public void onActivityResult(AfIntent intent, int requestCode, int resultCode) {
        if (pager instanceof ApPager && pager.getActivity() != null) {
            if (requestCode == REQUEST_INSTALL_PACKAGES && resultCode == RESULT_OK) {
                ((ApPager) pager).doInstall();
            }
        }
    }

    public void doInstall() {
        if (this.doInstallRunnable != null) {
            this.doInstallRunnable.run();
            this.doInstallRunnable = null;
        }
    }

    public void doRecordAudioWithPermissionCheck(Runnable runnable) {
        this.doRecordAudioRunnable = runnable;
    }

    public void doRecordAudio() {
        if (this.doRecordAudioRunnable != null) {
            this.doRecordAudioRunnable.run();
            this.doRecordAudioRunnable = null;
        }
    }
}
