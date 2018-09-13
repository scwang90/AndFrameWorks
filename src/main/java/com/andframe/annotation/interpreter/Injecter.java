package com.andframe.annotation.interpreter;

import android.accounts.AccountManager;
import android.app.Activity;
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
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
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
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.service.wallpaper.WallpaperService;
import android.support.v4.app.Fragment;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;

import com.andframe.activity.AfActivity;
import com.andframe.annotation.inject.Inject;
import com.andframe.annotation.inject.InjectDelayed;
import com.andframe.annotation.inject.InjectExtra;
import com.andframe.annotation.inject.InjectExtraInvalid;
import com.andframe.annotation.inject.InjectInit;
import com.andframe.annotation.inject.InjectLayout;
import com.andframe.annotation.inject.InjectRes;
import com.andframe.annotation.inject.InjectSystem;
import com.andframe.annotation.inject.idname.InjectRes$;
import com.andframe.api.Cacher;
import com.andframe.api.Extrater;
import com.andframe.application.AfApp;
import com.andframe.application.AfAppSettings;
import com.andframe.caches.AfDurableCacher;
import com.andframe.caches.AfJsonCache;
import com.andframe.caches.AfPrivateCacher;
import com.andframe.caches.AfSharedPreference;
import com.andframe.exception.AfException;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfDialogBuilder;
import com.andframe.feature.AfIntent;
import com.andframe.impl.wrapper.ViewWrapper;
import com.andframe.task.AfDispatcher;
import com.andframe.util.android.AfDensity;
import com.andframe.util.java.AfReflecter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Random;

import static com.andframe.annotation.interpreter.ReflecterCacher.getFields;
import static com.andframe.annotation.interpreter.ReflecterCacher.getMethods;

/**
 * annotation.inject 解释器
 * @author 树朾
 */
@SuppressWarnings("unused")
public class Injecter {

    protected static String TAG(Object obj, String tag) {
        if (obj == null) {
            return "Injecter." + tag;
        }
        return "Injecter(" + obj.getClass().getName() + ")." + tag;
    }

    public static void doInject(Context context) {
        doInject(context, context);
    }

    public static void doInjectExtra(Object handler) {

        Field[] fields = getFields(handler);
        for (Field field : fields) {
            injectExtra(field, handler, null);
        }
    }

    public static void doInject(Object handler, Context context) {

        Field[] fields = getFields(handler);
        for (Field field : fields) {
            inject(field, handler, context);
            injectRes(field, handler, context);
            injectRes$(field, handler, context);
            injectSystem(field, handler, context);
            injectExtra(field, handler, context);
        }

        injectLayout(handler, context);

        Method[] methods = getMethods(handler);
        for (Method method : methods) {
            injectInit(method, handler, context);
            injectDelayed(method, handler, context);
        }
    }

    private static void inject(Field field, Object handler, Context context) {
        if (field.isAnnotationPresent(Inject.class)) {
            try {
                Object value = null;
                Class<?> clazz = field.getType();
                if (clazz.equals(Resources.class)) {
                    value = context.getResources();
                } else if (clazz.equals(Random.class)) {
                    value = new Random();
                } else if (clazz.equals(AfDialogBuilder.class)) {
                    value = new AfDialogBuilder(context);
                } else if (clazz.equals(AfDensity.class)) {
                    value = new AfDensity();
                } else if (clazz.equals(AfReflecter.class)) {
                    value = new AfReflecter();
//                } else if (clazz.equals(AfDesHelper.class)) {
//                    value = new AfDesHelper();
//                } else if (clazz.equals(AfDeviceInfo.class)) {
//                    value = new AfDeviceInfo(context);
//                } else if (clazz.equals(AfDistance.class)) {
//                    value = new AfDistance();
//                } else if (clazz.equals(AfGifHelper.class)) {
//                    value = new AfGifHelper();
//                } else if (clazz.equals(AfImageHelper.class)) {
//                    value = new AfImageHelper();
                } else if (clazz.equals(AfSharedPreference.class)) {
                    value = new AfSharedPreference(context, field.getAnnotation(Inject.class).value());
                } else if (clazz.equals(Cacher.class)) {
                    value = new AfDurableCacher(field.getAnnotation(Inject.class).value());
                } else if (clazz.equals(AfDurableCacher.class)) {
                    value = new AfDurableCacher(field.getAnnotation(Inject.class).value());
                } else if (clazz.equals(AfPrivateCacher.class)) {
                    value = new AfPrivateCacher(field.getAnnotation(Inject.class).value());
                } else if (clazz.equals(AfJsonCache.class)) {
                    value = new AfJsonCache(context, field.getAnnotation(Inject.class).value());
//                } else if (clazz.equals(AfImageCaches.class)) {
//                    value = AfImageCaches.getInstance();
                } else if (AfApp.class.isAssignableFrom(clazz)) {
                    value = AfApp.get();
                } else if (AfAppSettings.class.isAssignableFrom(clazz)) {
                    value = AfApp.get().newAppSetting();
//                } else if (AfEntityDao.class.isAssignableFrom(clazz)) {
//                    value = clazz.getConstructor(Context.class).newInstance(context);
                }
                if (value != null) {
                    field.setAccessible(true);
                    field.set(handler, value);
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doInject.Inject") + field.getName());
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void injectRes(Field field, Object handler, Context context) {
        if (field.isAnnotationPresent(InjectRes.class)) {
            try {
                Object value = null;
                Class<?> clazz = field.getType();
                InjectRes inject = field.getAnnotation(InjectRes.class);
                if (clazz.equals(String.class)) {
                    value = context.getResources().getString(inject.value());
                } else if (clazz.equals(String[].class)) {
                    value = context.getResources().getStringArray(inject.value());
                } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                    value = context.getResources().getDimension(inject.value());
                } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                    value = context.getResources().getBoolean(inject.value());
                } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                    value = context.getResources().getColor(inject.value());
                } else if (clazz.equals(Integer[].class) || clazz.equals(int[].class)) {
                    value = context.getResources().getIntArray(inject.value());
                } else if (clazz.equals(Drawable.class)) {
                    value = context.getResources().getDrawable(inject.value());
                } else if (clazz.equals(Movie.class)) {
                    value = context.getResources().getMovie(inject.value());
                } else if (clazz.equals(XmlResourceParser.class)) {
                    value = context.getResources().getXml(inject.value());
                } else if (clazz.equals(ColorStateList.class)) {
                    value = context.getResources().getColorStateList(inject.value());
                }
                if (value != null) {
                    field.setAccessible(true);
                    field.set(handler, value);
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doInject.injectRes") + field.getName());
            }
        }
    }

    private static void injectRes$(Field field, Object handler, Context context) {
        if (field.isAnnotationPresent(InjectRes$.class)) {
            try {
                Object value = null;
                Class<?> clazz = field.getType();
                InjectRes$ inject = field.getAnnotation(InjectRes$.class);
                Resources resources = context.getResources();
                if (clazz.equals(String.class)) {
                    value = resources.getString(resources.getIdentifier(inject.value(), "string", context.getPackageName()));
                } else if (clazz.equals(String[].class)) {
                    value = resources.getStringArray(resources.getIdentifier(inject.value(), "array", context.getPackageName()));
                } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                    value = resources.getDimension(resources.getIdentifier(inject.value(), "dimens", context.getPackageName()));
                } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                    value = resources.getBoolean(resources.getIdentifier(inject.value(), "value", context.getPackageName()));
                } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                    value = resources.getColor(resources.getIdentifier(inject.value(), "color", context.getPackageName()));
                } else if (clazz.equals(Integer[].class) || clazz.equals(int[].class)) {
                    value = resources.getIntArray(resources.getIdentifier(inject.value(), "int", context.getPackageName()));
                } else if (clazz.equals(Drawable.class)) {
                    value = resources.getDrawable(resources.getIdentifier(inject.value(), "drawable", context.getPackageName()));
                } else if (clazz.equals(Movie.class)) {
                    value = resources.getMovie(resources.getIdentifier(inject.value(), "movie", context.getPackageName()));
                } else if (clazz.equals(XmlResourceParser.class)) {
                    value = resources.getXml(resources.getIdentifier(inject.value(), "xml", context.getPackageName()));
                } else if (clazz.equals(ColorStateList.class)) {
                    value = resources.getColorStateList(resources.getIdentifier(inject.value(), "color", context.getPackageName()));
                }
                if (value != null) {
                    field.setAccessible(true);
                    field.set(handler, value);
                }
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doInject.injectRes") + field.getName());
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void injectSystem(Field field, Object handler, Context context) {
        if (field.isAnnotationPresent(InjectSystem.class)) {
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
                AfExceptionHandler.handle(e, TAG(handler, "doInject.injectSystem") + field.getName());
            }
        }
    }

    private static void injectDelayed(Method method, final Object handler, Context context) {
        if (method.isAnnotationPresent(InjectDelayed.class)) {
            try {
                InjectDelayed bind = method.getAnnotation(InjectDelayed.class);
                AfDispatcher.dispatch(new Runnable() {
                    {
                        this.mMethod = method;
                    }
                    final Method mMethod;
                    @Override
                    public void run() {
                        try {
                            mMethod.setAccessible(true);
                            mMethod.invoke(handler);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }, bind.value());
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "doInjectDelayed.") + method.getName());
            }
        }
    }

    private static void injectLayout(Object handler, Context context) {
        InjectLayout layout = AfReflecter.getAnnotation(handler.getClass(), getStopType(handler), InjectLayout.class);
        if (handler instanceof AfActivity && layout != null) {
            try {
                ((AfActivity) handler).setContentView(layout.value());
            } catch (Throwable e) {
                e.printStackTrace();
                AfExceptionHandler.handle(e, TAG(handler, "doInjectLayout.setContentView"));
            }
        }
    }

    private static void injectInit(Method method, Object handler, Context context) {
        if (method.isAnnotationPresent(InjectInit.class)) {
            InjectInit init = method.getAnnotation(InjectInit.class);
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                e.printStackTrace();
                if (init.value()) {
                    throw new RuntimeException("调用初始化失败", e);
                }
                AfExceptionHandler.handle(e, TAG(handler, "doInjectInit.invokeMethod.") + method.getName());
            }
        }
    }

    private static void injectExtra(Field field, Object handler, Context context) {
        InjectExtraInvalid invalid = AfReflecter.getAnnotation(handler.getClass(), InjectExtraInvalid.class);
        if (invalid != null && invalid.value().length == 0) {
            return;
        }
        if (field.isAnnotationPresent(InjectExtra.class)) {
            InjectExtra inject = field.getAnnotation(InjectExtra.class);
            if (invalid != null) {
                boolean find = false;
                for (String extra : invalid.value()) {
                    if (TextUtils.equals(extra, inject.value())) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    return;
                }
            }
            try {
                Extrater intent = new AfIntent();
                if (handler instanceof Activity) {
                    intent = new AfIntent(((Activity) handler).getIntent());
                } else if (handler instanceof Fragment) {
                    final Fragment fragment = (Fragment) handler;
                    Bundle bundle = fragment.getArguments();
                    if (bundle != null) {
                        intent = new AfBundle(fragment.getArguments()) {
                            @Override
                            public <T> T get(String _key, Class<T> clazz) {
                                T t = super.get(_key, clazz);
                                if (t == null && fragment.getActivity() != null) {
                                    return new AfIntent(fragment.getActivity().getIntent()).get(_key, clazz);
                                }
                                return t;
                            }

                            @Override
                            public <T> List<T> getList(String _key, Class<T> clazz) {
                                List<T> list = super.getList(_key, clazz);
                                if (list == null || list.size() == 0) {
                                    return new AfIntent(fragment.getActivity().getIntent()).getList(_key, clazz);
                                }
                                return list;
                            }
                        };
                    } else {
                        if (context instanceof Activity && fragment.getActivity() == null) {
                            intent = new AfIntent(((Activity)context).getIntent());
                        } else {
                            intent = new AfIntent(fragment.getActivity().getIntent());
                        }
                    }
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
                    ParameterizedType parameterizedType = (ParameterizedType) generic;
                    Type typeArgument = parameterizedType.getActualTypeArguments()[0];
                    if (typeArgument instanceof Class) {
                        type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    } else if (typeArgument instanceof WildcardType) {
                        type = (Class<?>) ((WildcardType) typeArgument).getUpperBounds()[0];
                    }
                    value = intent.getList(inject.value(), type);
                } else {
                    value = intent.get(inject.value(), type);
                }
                if (value != null) {
                    field.setAccessible(true);
                    field.set(handler, value);
                } else if (inject.necessary()) {
                    if (inject.remark().length() > 0) {
                        throw new AfException("缺少必须参数:" + inject.remark());
                    } else {
                        throw new AfException("缺少必须参数:" + inject.value());
                    }
                }
            } catch (AfException e) {
                throw e;
            } catch (Throwable e) {
                if (inject.necessary()) {
                    throw new RuntimeException("缺少必须参数", e);
                }
                AfExceptionHandler.handle(e, TAG(handler, "doInject.InjectExtra.") + field.getName());
            }
        }
    }

    private static Object invokeMethod(Object handler, Method method, Object... params) throws Exception {
        if (handler != null && method != null) {
            method.setAccessible(true);
            return method.invoke(handler, params);
        }
        return null;
    }

    private static Class<?> getStopType(Object handler) {
        if (handler instanceof ViewWrapper) {
            return ViewWrapper.class;
        }
        return Object.class;
    }
}
