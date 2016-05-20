package com.andframe.annotation.interpreter;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobScheduler;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.RestrictionsManager;
import android.content.pm.LauncherApps;
import android.content.res.Resources;
import android.hardware.ConsumerIrManager;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.midi.MidiManager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.Message;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.service.wallpaper.WallpaperService;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;

import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.annotation.inject.Inject;
import com.andframe.annotation.inject.InjectDelayed;
import com.andframe.annotation.inject.InjectExtra;
import com.andframe.annotation.inject.InjectInit;
import com.andframe.annotation.inject.InjectLayout;
import com.andframe.annotation.inject.InjectQueryChanged;
import com.andframe.annotation.inject.InjectSystem;
import com.andframe.application.AfAppSettings;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfDurableCache;
import com.andframe.caches.AfImageCaches;
import com.andframe.caches.AfJsonCache;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.caches.AfSharedPreference;
import com.andframe.dao.AfEntityDao;
import com.andframe.exception.AfToastException;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfDailog;
import com.andframe.feature.AfDensity;
import com.andframe.feature.AfDistance;
import com.andframe.feature.AfIntent;
import com.andframe.feature.AfSoftInputer;
import com.andframe.feature.framework.AfExtrater;
import com.andframe.fragment.AfFragment;
import com.andframe.helper.android.AfDesHelper;
import com.andframe.helper.android.AfDeviceInfo;
import com.andframe.helper.android.AfGifHelper;
import com.andframe.helper.android.AfImageHelper;
import com.andframe.layoutbind.framework.AfViewDelegate;
import com.andframe.thread.AfHandlerTimerTask;
import com.andframe.util.java.AfReflecter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * annotation.inject 解释器
 * @author 树朾
 */
public class Injecter {

    protected static String TAG(Object obj,String tag) {
        if (obj == null) {
            return "Injecter." + tag;
        }
        return "Injecter(" + obj.getClass().getName() + ")." + tag;
    }

    public static void doInject(Context context) {
        doInject(context, context);
    }

    public static void doInject(Object handler, Context context) {
        inject(handler, context);
        injectSystem(handler, context);
        injectExtra(handler, context);
        injectLayout(handler, context);
        injectInit(handler, context);
        injectDelayed(handler, context);
    }

    public static void doInjectQueryChanged(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), InjectQueryChanged.class)) {
            InjectQueryChanged init = method.getAnnotation(InjectQueryChanged.class);
            try {
                invokeMethod(handler,method);
            }catch(Throwable e){
                e.printStackTrace();
                if (init.value()){
                    throw new RuntimeException("调用查询失败",e);
                }
                AfExceptionHandler.handler(e, TAG(handler, "doInjectQueryChanged.invokeMethod.")+method.getName());
            }
        }
    }

    private static void inject(Object handler, Context context) {
        for (Field field : AfReflecter.getFieldAnnotation(handler.getClass(), Inject.class)) {
            try {
                Object value = null;
                Class<?> clazz = field.getType();
                if (clazz.equals(Resources.class)) {
                    value = context.getResources();
                } else if (clazz.equals(Random.class)) {
                    value = new Random();
                } else if (clazz.equals(AfSoftInputer.class)) {
                    value = new AfSoftInputer(context);
                } else if (clazz.equals(AfDailog.class)) {
                    value = new AfDailog(context);
                } else if (clazz.equals(AfDensity.class)) {
                    value = new AfDensity(context);
                } else if (clazz.equals(AfReflecter.class)) {
                    value = new AfReflecter();
                } else if (clazz.equals(AfDesHelper.class)) {
                    value = new AfDesHelper();
                } else if (clazz.equals(AfDeviceInfo.class)) {
                    value = new AfDeviceInfo(context);
                } else if (clazz.equals(AfDistance.class)) {
                    value = new AfDistance();
                } else if (clazz.equals(AfGifHelper.class)) {
                    value = new AfGifHelper();
                } else if (clazz.equals(AfImageHelper.class)) {
                    value = new AfImageHelper();
                } else if (clazz.equals(AfSharedPreference.class)) {
                    value = new AfSharedPreference(context,field.getAnnotation(Inject.class).value());
                } else if (clazz.equals(AfDurableCache.class)) {
                    value = AfDurableCache.getInstance(field.getAnnotation(Inject.class).value());
                } else if (clazz.equals(AfPrivateCaches.class)) {
                    value = AfPrivateCaches.getInstance(field.getAnnotation(Inject.class).value());
                } else if (clazz.equals(AfJsonCache.class)) {
                    value = new AfJsonCache(context,field.getAnnotation(Inject.class).value());
                } else if (clazz.equals(AfImageCaches.class)) {
                    value = AfImageCaches.getInstance();
                } else if (AfApplication.class.isAssignableFrom(clazz)) {
                    value = AfApplication.getApp();
                } else if (AfAppSettings.class.isAssignableFrom(clazz)) {
                    value = AfApplication.getApp().getAppSetting();
                } else if (AfEntityDao.class.isAssignableFrom(clazz)) {
                    value = clazz.getConstructor(Context.class).newInstance(context);
                }
                if (value != null) {
                    field.setAccessible(true);
                    field.set(handler, value);
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e,TAG(handler, "doInject.Inject")+ field.getName());
            }
        }
    }

    private static void injectSystem(Object handler, Context context) {
        for (Field field : AfReflecter.getFieldAnnotation(handler.getClass(), InjectSystem.class)) {
            try {
                Object value = null;
                Class<?> clazz = field.getType();
                InjectSystem inject = field.getAnnotation(InjectSystem.class);
                if (clazz.equals(Resources.class)) {
                    value = context.getResources();
                } else if (clazz.equals(Random.class)) {
                    value = new Random();
                } else if (!"".equals(inject.value())) {
                    value = AfReflecter.doMethod(context, "getSystemService", new Class[]{String.class}, inject.value());
                } else if (clazz.equals(PowerManager.class)) {
                    value = context.getSystemService(Context.POWER_SERVICE);
                } else if (clazz.equals(WindowManager.class)) {
                    value = context.getSystemService(Context.WINDOW_SERVICE);
                } else if (clazz.equals(LayoutInflater.class)) {
                    value = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                } else if (clazz.equals(AccountManager.class)) {
                    value = context.getSystemService(Context.ACCOUNT_SERVICE);
                } else if (clazz.equals(ActivityManager.class)) {
                    value = context.getSystemService(Context.ACTIVITY_SERVICE);
                } else if (clazz.equals(AlarmManager.class)) {
                    value = context.getSystemService(Context.ALARM_SERVICE);
                } else if (clazz.equals(NotificationManager.class)) {
                    value = context.getSystemService(Context.NOTIFICATION_SERVICE);
                } else if (clazz.equals(AccessibilityManager.class)) {
                    value = context.getSystemService(Context.ACCESSIBILITY_SERVICE);
                } else if (clazz.equals(KeyguardManager.class)) {
                    value = context.getSystemService(Context.KEYGUARD_SERVICE);
                } else if (clazz.equals(LocationManager.class)) {
                    value = context.getSystemService(Context.LOCATION_SERVICE);
                } else if (clazz.equals(SearchManager.class)) {
                    value = context.getSystemService(Context.SEARCH_SERVICE);
                } else if (clazz.equals(SensorManager.class)) {
                    value = context.getSystemService(Context.SENSOR_SERVICE);
                } else if (clazz.equals(WallpaperService.class)) {
                    value = context.getSystemService(Context.WALLPAPER_SERVICE);
                } else if (clazz.equals(Vibrator.class)) {
                    value = context.getSystemService(Context.VIBRATOR_SERVICE);
                } else if (clazz.equals(ConnectivityManager.class)) {
                    value = context.getSystemService(Context.CONNECTIVITY_SERVICE);
                } else if (clazz.equals(WifiManager.class)) {
                    value = context.getSystemService(Context.WIFI_SERVICE);
                } else if (clazz.equals(AudioManager.class)) {
                    value = context.getSystemService(Context.AUDIO_SERVICE);
                } else if (clazz.equals(TelephonyManager.class)) {
                    value = context.getSystemService(Context.TELEPHONY_SERVICE);
                } else if (clazz.equals(ClipboardManager.class)) {
                    value = context.getSystemService(Context.CLIPBOARD_SERVICE);
                } else if (clazz.equals(android.content.ClipboardManager.class)) {
                    value = context.getSystemService(Context.CLIPBOARD_SERVICE);
                } else if (clazz.equals(InputMethodManager.class)) {
                    value = context.getSystemService(Context.INPUT_METHOD_SERVICE);
                } else if (clazz.equals(DropBoxManager.class)) {
                    value = context.getSystemService(Context.DROPBOX_SERVICE);
                } else if (clazz.equals(DevicePolicyManager.class)) {
                    value = context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                } else if (clazz.equals(UiModeManager.class)) {
                    value = context.getSystemService(Context.UI_MODE_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 9 && clazz.equals(StorageManager.class)) {
                    value = context.getSystemService(Context.STORAGE_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 9 && clazz.equals(DownloadManager.class)) {
                    value = context.getSystemService(Context.DOWNLOAD_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 10 && clazz.equals(NfcManager.class)) {
                    value = context.getSystemService(Context.NFC_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 12 && clazz.equals(UsbManager.class)) {
                    value = context.getSystemService(Context.USB_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 14 && clazz.equals(WifiP2pManager.class)) {
                    value = context.getSystemService(Context.WIFI_P2P_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 14 && clazz.equals(TextServicesManager.class)) {
                    value = context.getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 16 && clazz.equals(MediaRouter.class)) {
                    value = context.getSystemService(Context.MEDIA_ROUTER_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 16 && clazz.equals(NsdManager.class)) {
                    value = context.getSystemService(Context.NSD_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 16 && clazz.equals(InputManager.class)) {
                    value = context.getSystemService(Context.INPUT_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 17 && clazz.equals(DisplayManager.class)) {
                    value = context.getSystemService(Context.DISPLAY_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 17 && clazz.equals(UserManager.class)) {
                    value = context.getSystemService(Context.USER_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 18 && clazz.equals(BluetoothManager.class)) {
                    value = context.getSystemService(Context.BLUETOOTH_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 19 && clazz.equals(CaptioningManager.class)) {
                    value = context.getSystemService(Context.CAPTIONING_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 19 && clazz.equals(AppOpsManager.class)) {
                    value = context.getSystemService(Context.APP_OPS_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 19 && clazz.equals(PrintManager.class)) {
                    value = context.getSystemService(Context.PRINT_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 19 && clazz.equals(ConsumerIrManager.class)) {
                    value = context.getSystemService(Context.CONSUMER_IR_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(AppWidgetManager.class)) {
                    value = context.getSystemService(Context.APPWIDGET_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(TelecomManager.class)) {
                    value = context.getSystemService(Context.TELECOM_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(MediaSessionManager.class)) {
                    value = context.getSystemService(Context.MEDIA_SESSION_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(BatteryManager.class)) {
                    value = context.getSystemService(Context.BATTERY_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(LauncherApps.class)) {
                    value = context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(RestrictionsManager.class)) {
                    value = context.getSystemService(Context.RESTRICTIONS_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(CameraManager.class)) {
                    value = context.getSystemService(Context.CAMERA_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(TvInputManager.class)) {
                    value = context.getSystemService(Context.TV_INPUT_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(JobScheduler.class)) {
                    value = context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 21 && clazz.equals(MediaProjectionManager.class)) {
                    value = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 22 && clazz.equals(SubscriptionManager.class)) {
                    value = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 22 && clazz.equals(UsageStatsManager.class)) {
                    value = context.getSystemService(Context.USAGE_STATS_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 23 && clazz.equals(CarrierConfigManager.class)) {
                    value = context.getSystemService(Context.CARRIER_CONFIG_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 23 && clazz.equals(FingerprintManager.class)) {
                    value = context.getSystemService(Context.FINGERPRINT_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 23 && clazz.equals(MidiManager.class)) {
                    value = context.getSystemService(Context.MIDI_SERVICE);
                } else if (Build.VERSION.SDK_INT >= 23 && clazz.equals(NetworkStatsManager.class)) {
                    value = context.getSystemService(Context.NETWORK_STATS_SERVICE);
                }
                if (value != null) {
                    field.setAccessible(true);
                    field.set(handler, value);
                }
            } catch (Throwable e) {
                AfExceptionHandler.handler(e,TAG(handler, "doInject.injectSystem")+ field.getName());
            }
        }
    }

    private static void injectDelayed(final Object handler, Context context) {
        for (final Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), InjectDelayed.class)) {
            try {
                InjectDelayed bind = method.getAnnotation(InjectDelayed.class);
                new Timer().schedule(new AfHandlerTimerTask() {
                    {
                        this.mMethod = method;
                    }

                    public final Method mMethod;

                    @Override
                    protected boolean onHandleTimer(Message msg) {
                        try {
                            mMethod.setAccessible(true);
                            mMethod.invoke(handler);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }, bind.value());
            } catch (Throwable e) {
                AfExceptionHandler.handler(e, TAG(handler, "doInjectDelayed.") + method.getName());
            }
        }
    }

    private static void injectLayout(Object handler, Context context) {
        InjectLayout layout = AfReflecter.getAnnotation(handler.getClass(), getStopType(handler), InjectLayout.class);
        if (handler instanceof AfActivity && layout != null) {
            try {
                ((AfActivity) handler).setContentView(layout.value());
            }catch(Throwable e){
                e.printStackTrace();
                AfExceptionHandler.handler(e,TAG(handler, "doInjectLayout.setContentView"));
            }
        }
    }

    private static void injectInit(Object handler, Context context) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), getStopType(handler), InjectInit.class)) {
            InjectInit init = method.getAnnotation(InjectInit.class);
            try {
                invokeMethod(handler,method);
            }catch(Throwable e){
                e.printStackTrace();
                if (init.value()){
                    throw new RuntimeException("调用初始化失败",e);
                }
                AfExceptionHandler.handler(e,TAG(handler, "doInjectInit.invokeMethod.")+method.getName());
            }
        }
    }

    private static void injectExtra(Object handler, Context context) {
        for (Field field : AfReflecter.getFieldAnnotation(handler.getClass(),InjectExtra.class)) {
            InjectExtra inject = field.getAnnotation(InjectExtra.class);
            try {
                if (handler instanceof AfPageable){
                    AfExtrater intent = new AfIntent();
                    if (handler instanceof AfActivity){
                        intent = new AfIntent(((AfActivity) handler).getIntent());
                    } else if (handler instanceof AfFragment){
                        final AfFragment fragment = (AfFragment) handler;
                        intent = new AfBundle(fragment.getArguments()){
                            @Override
                            public <T> T get(String _key, Class<T> clazz) {
                                T t = super.get(_key, clazz);
                                if (t == null && fragment.getActivity() != null) {
                                    return new AfIntent(fragment.getActivity().getIntent()).get(_key,clazz);
                                }
                                return t;
                            }

                            @Override
                            public <T> List<T> getList(String _key, Class<T> clazz) {
                                List<T> list = super.getList(_key, clazz);
                                if (list == null || list.size() == 0) {
                                    return new AfIntent(fragment.getActivity().getIntent()).getList(_key,clazz);
                                }
                                return list;
                            }
                        };
                    }
                    Object value;
                    Class<?> type = field.getType();
                    Type generic = field.getGenericType();
                    if (type.isArray()) {
                        type = type.getComponentType();
                        List<?> list = intent.getList(inject.value(), type);
                        value = Array.newInstance(type, list.size());
                        value = list.toArray((Object[]) value);
                    } else if (List.class.equals(type)) {
                        ParameterizedType parameterized = (ParameterizedType) generic;
                        type = (Class<?>) parameterized.getActualTypeArguments()[0];
                        value = intent.getList(inject.value(), type);
                    } else {
                        value = intent.get(inject.value(), type);
                    }
                    if (value != null) {
                        field.setAccessible(true);
                        field.set(handler, value);
                    } else if (inject.necessary()) {
                        if (inject.remark() != null && inject.remark().length() > 0) {
                            throw new AfToastException("缺少必须参数" + inject.remark());
                        } else {
                            throw new AfToastException("缺少必须参数" + inject.value());
                        }
                    }
                }
            } catch (Throwable e) {
                if (inject.necessary()){
                    throw new RuntimeException("缺少必须参数",e);
                }
                AfExceptionHandler.handler(e,TAG(handler, "doInject.InjectExtra.")+ field.getName());
            }
        }
    }

    private static Object invokeMethod(Object handler, Method method, Object... params) throws Exception {
        if (handler != null && method != null){
            method.setAccessible(true);
            return method.invoke(handler, params);
        }
        return null;
    }

    private static Class<?> getStopType(Object handler){
        if (handler instanceof AfViewDelegate){
            return AfViewDelegate.class;
        }
        return Object.class;
    }
}
