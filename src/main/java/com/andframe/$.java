package com.andframe;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.api.DialogBuilder;
import com.andframe.api.ModelConvertor;
import com.andframe.api.TaskExecutor;
import com.andframe.api.view.ViewModuler;
import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.Viewer;
import com.andframe.application.AfApp;
import com.andframe.impl.viewer.ViewerWarpper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * 抄作简化
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public class $ {

    private static InvocationHandler handler = new $$$();
    private static Api api = (Api) Proxy.newProxyInstance($$$.class.getClassLoader(), new Class[]{Api.class}, handler );
    private static Object[] lastWiths;

    interface $$ {
        <From,To> To[] convertArray(@NonNull From[] froms, @NonNull ModelConvertor<From,To> convertor);
        <From,To> List<To> convertList(@NonNull Iterable<From> froms, @NonNull ModelConvertor<From,To> convertor);
    }

    public interface Api extends $$,TaskExecutor, DialogBuilder, ViewQuery {
    }

    @SuppressWarnings("MethodNameSameAsClassName")
    @MainThread
    public static Api $(Object... withs) {
        lastWiths = withs;
        return api;
    }

    @MainThread
    public static Api with(Object... withs) {
        lastWiths = withs;
        return api;
    }

    @MainThread
    public static DialogBuilder dialog(Context context) {
        return AfApp.get().newDialogBuilder(context);
    }

    @MainThread
    public static DialogBuilder dialog(Viewer viewer) {
        return AfApp.get().newDialogBuilder(viewer.getContext());
    }

    @MainThread
    public static DialogBuilder dialog(AfActivity activity) {
        return AfApp.get().newDialogBuilder(activity);
    }

    @MainThread
    public static ViewQuery<? extends ViewQuery> query(Viewer viewer) {
        return AfApp.get().newViewQuery(viewer);
    }

    @MainThread
    public static ViewQuery<? extends ViewQuery> query(View view) {
        return AfApp.get().newViewQuery(new ViewerWarpper(view));
    }

    @MainThread
    public static ViewQuery<? extends ViewQuery> query(AfActivity activity) {
        return AfApp.get().newViewQuery(new ViewerWarpper(activity.getView()));
    }

    public static TaskExecutor task() {
        return AfApp.get().newTaskExecutor();
    }

    public static Api get() {
        return api;
    }

    private static Viewer getLastViewer() {
        if (lastWiths != null && lastWiths.length > 0) {
            if (lastWiths[0] instanceof ViewModuler) {
                return new ViewerWarpper(((ViewModuler) lastWiths[0]).getView());
            } else if (lastWiths[0] instanceof View) {
                return new ViewerWarpper(((View) lastWiths[0]));
            } else if (lastWiths[0] instanceof Viewer) {
                return ((Viewer) lastWiths[0]);
            } else if (lastWiths[0] instanceof Activity) {
                return new ViewerWarpper((Activity) lastWiths[0]);
            } else if (lastWiths[0] instanceof Fragment) {
                return new ViewerWarpper((Fragment) lastWiths[0]);
            }
        }
        return null;
    }

    private static Context getLastContext() {
        if (lastWiths != null && lastWiths.length > 0) {
            if (lastWiths[0] instanceof Context) {
                return ((Context) lastWiths[0]);
            } else if (lastWiths[0] instanceof Viewer) {
                return ((Viewer) lastWiths[0]).getContext();
            } else if (lastWiths[0] instanceof Fragment) {
                return ((Fragment) lastWiths[0]).getContext();
            }
        }
        return null;
    }

    private static class $$$ implements InvocationHandler,$$ {
        @Override
        public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
            if (method.getDeclaringClass().isAssignableFrom(TaskExecutor.class)) {
                return method.invoke(AfApp.get().newTaskExecutor(), objects);
            } else if (method.getDeclaringClass().isAssignableFrom(DialogBuilder.class)) {
                return method.invoke(AfApp.get().newDialogBuilder(getLastContext()), objects);
            } else if (method.getDeclaringClass().isAssignableFrom(ViewQuery.class)) {
                return method.invoke(AfApp.get().newViewQuery(getLastViewer()), objects);
            } else if (method.getDeclaringClass().isAssignableFrom($$.class)) {
                return method.invoke(this, objects);
            }
            return null;
        }

        @Override
        public <From, To> To[] convertArray(@NonNull From[] froms, @NonNull ModelConvertor<From, To> convertor) {
            Object[] tos = new Object[froms.length];
            for (int i = 0; i < froms.length; i++) {
                tos[i] = convertor.convert(froms[i]);
            }
            //noinspection unchecked
            return (To[])tos;
        }

        @Override
        public <From, To> List<To> convertList(@NonNull Iterable<From> froms, @NonNull ModelConvertor<From, To> convertor) {
            List<To> tos = new ArrayList<>();
            for (From from : froms) {
                tos.add(convertor.convert(from));
            }
            return tos;
        }

    }
}
