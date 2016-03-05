package com.andframe.annotation.interpreter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Message;

import com.andframe.R;
import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.annotation.inject.Inject;
import com.andframe.annotation.inject.InjectDelayed;
import com.andframe.annotation.inject.InjectExtra;
import com.andframe.annotation.inject.InjectInit;
import com.andframe.annotation.inject.InjectLayout;
import com.andframe.annotation.inject.InjectQueryChanged;
import com.andframe.application.AfApplication;
import com.andframe.application.AfExceptionHandler;
import com.andframe.caches.AfDurableCache;
import com.andframe.caches.AfImageCaches;
import com.andframe.caches.AfJsonCache;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.caches.AfSharedPreference;
import com.andframe.feature.AfBundle;
import com.andframe.feature.AfDailog;
import com.andframe.feature.AfDensity;
import com.andframe.feature.AfDistance;
import com.andframe.feature.AfGifPlayer;
import com.andframe.feature.AfIntent;
import com.andframe.feature.AfSoftInputer;
import com.andframe.feature.framework.AfExtrater;
import com.andframe.fragment.AfFragment;
import com.andframe.helper.android.AfDesHelper;
import com.andframe.helper.android.AfDeviceInfo;
import com.andframe.helper.android.AfGifHelper;
import com.andframe.helper.android.AfImageHelper;
import com.andframe.helper.java.AfSQLHelper;
import com.andframe.layoutbind.framework.AfViewDelegate;
import com.andframe.network.AfImageService;
import com.andframe.thread.AfHandlerTimerTask;
import com.andframe.util.android.AfMeasure;
import com.andframe.util.java.AfReflecter;
import com.andframe.util.java.AfStackTrace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        injectExtra(handler, context);
        injectLayout(handler, context);
        injectSystem(handler, context);
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
                        };
                    }
                    Class<?> clazz = field.getType();
                    Object value = intent.get(inject.value(), clazz);
                    field.setAccessible(true);
                    field.set(handler, value);
                }
            } catch (Throwable e) {
                if (inject.necessary()){
                    throw new RuntimeException("缺少必须参数",e);
                }
                AfExceptionHandler.handler(e,TAG(handler, "doInject.InjectExtra.")+ field.getName());
            }
        }
    }

    private static void injectSystem(Object handler, Context context) {
        for (Field field : AfReflecter.getFieldAnnotation(handler.getClass(),Inject.class)) {
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
                } else if (clazz.equals(AfApplication.class)) {
                    value = AfApplication.getApp();
                }
                field.setAccessible(true);
                field.set(handler, value);
            } catch (Throwable e) {
                AfExceptionHandler.handler(e,TAG(handler, "doInject.Inject")+ field.getName());
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
