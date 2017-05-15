package com.andframe.annotation.interpreter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andframe.annotation.lifecycle.OnAttach;
import com.andframe.annotation.lifecycle.OnCreate;
import com.andframe.annotation.lifecycle.OnCreateView;
import com.andframe.annotation.lifecycle.OnDestroy;
import com.andframe.annotation.lifecycle.OnDestroyView;
import com.andframe.annotation.lifecycle.OnDetach;
import com.andframe.annotation.lifecycle.OnNewIntent;
import com.andframe.annotation.lifecycle.OnPause;
import com.andframe.annotation.lifecycle.OnRestart;
import com.andframe.annotation.lifecycle.OnResume;
import com.andframe.annotation.lifecycle.OnStart;
import com.andframe.annotation.lifecycle.OnStop;
import com.andframe.exception.AfExceptionHandler;

import java.lang.annotation.Annotation;
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

    public static Object injectLifeCycle(Object handler, Class<? extends Annotation> annotation, Object... params) {
        for (Method method : ReflecterCacher.getMethods(handler)) {
            if (method.isAnnotationPresent(annotation)) {
                try {
                    Object retvalue = invokeMethod(handler, method, params);
                    if (retvalue != null) {
                        return retvalue;
                    }
                } catch (Throwable e) {
                    AfExceptionHandler.handle(e, TAG(handler, "injectOnCreate.invokeMethod.") + method.getName());
                }
            }
        }
        return null;
    }

    public static void injectOnCreate(Object handler, Bundle bundle) {
        injectLifeCycle(handler, OnCreate.class, bundle);
    }

    public static void injectOnStart(Object handler) {
        injectLifeCycle(handler, OnStart.class);
    }

    public static void injectOnRestart(Object handler) {
        injectLifeCycle(handler, OnRestart.class);
    }

    public static void injectOnResume(Object handler) {
        injectLifeCycle(handler, OnResume.class);
    }

    public static void injectOnPause(Object handler) {
        injectLifeCycle(handler, OnPause.class);
    }

    public static void injectOnStop(Object handler) {
        injectLifeCycle(handler, OnStop.class);
    }

    public static void injectOnDestroy(Object handler) {
        injectLifeCycle(handler, OnDestroy.class);
    }

    public static void injectOnAttach(Object handler, Context context) {
        injectLifeCycle(handler, OnAttach.class, context);
    }

    public static void injectOnDestroyView(Object handler) {
        injectLifeCycle(handler, OnDestroyView.class);
    }

    public static void injectonDetach(Object handler) {
        injectLifeCycle(handler, OnDetach.class);
    }

    public static void injectonNewIntent(Object handler, Intent intent) {
        injectLifeCycle(handler, OnNewIntent.class, intent);
    }

    public static View injectCreateView(Object handler, LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return (View)injectLifeCycle(handler, OnCreateView.class, inflater, container, bundle);
    }
}
