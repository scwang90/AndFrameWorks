package com.andframe.annotation.interpreter;

import android.os.Bundle;

import com.andframe.annotation.lifecycle.OnAttach;
import com.andframe.annotation.lifecycle.OnCreate;
import com.andframe.annotation.lifecycle.OnDestroy;
import com.andframe.annotation.lifecycle.OnDestroyView;
import com.andframe.annotation.lifecycle.OnPause;
import com.andframe.annotation.lifecycle.OnRestart;
import com.andframe.annotation.lifecycle.OnResume;
import com.andframe.annotation.lifecycle.OnStart;
import com.andframe.annotation.lifecycle.OnStop;
import com.andframe.exception.AfExceptionHandler;
import com.andframe.util.java.AfReflecter;

import java.lang.reflect.Method;

import static com.andframe.annotation.interpreter.SmartInvoke.invokeMethod;

/**
 * 生命周期解释器
 * Created by SCWANG on 2016/10/14.
 */

public class LifeCycleInjecter {

    protected static String TAG(Object obj, String tag) {
        if (obj == null) {
            return "LifeCycleInjecter." + tag;
        }
        return "LifeCycleInjecter(" + obj.getClass().getName() + ")." + tag;
    }

    public static void injectOnCreate(Object handler, Bundle bundle) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnCreate.class)) {
            try {
                invokeMethod(handler, method, bundle);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnCreate.invokeMethod.") + method.getName());
            }
        }
    }

    public static void injectOnStart(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnStart.class)) {
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnStart.invokeMethod.") + method.getName());
            }
        }
    }

    public static void injectOnRestart(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnRestart.class)) {
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnRestart.invokeMethod.") + method.getName());
            }
        }
    }

    public static void injectOnResume(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnResume.class)) {
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnResume.invokeMethod.") + method.getName());
            }
        }
    }

    public static void injectOnPause(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnPause.class)) {
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnPause.invokeMethod.") + method.getName());
            }
        }
    }

    public static void injectOnStop(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnStop.class)) {
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnStop.invokeMethod.") + method.getName());
            }
        }
    }

    public static void injectOnDestroy(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnDestroy.class)) {
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnDestroy.invokeMethod.") + method.getName());
            }
        }
    }

    public static void injectOnAttach(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnAttach.class)) {
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnAttach.invokeMethod.") + method.getName());
            }
        }
    }

    public static void injectOnDestroyView(Object handler) {
        for (Method method : AfReflecter.getMethodAnnotation(handler.getClass(), SmartInvoke.getStopType(handler), OnDestroyView.class)) {
            try {
                invokeMethod(handler, method);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG(handler, "injectOnDestroyView.invokeMethod.") + method.getName());
            }
        }
    }
}
