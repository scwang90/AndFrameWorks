package com.andframe;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.api.Cacher;
import com.andframe.api.DialogBuilder;
import com.andframe.api.ModelConvertor;
import com.andframe.api.Toaster;
import com.andframe.api.event.EventManager;
import com.andframe.api.pager.PagerManager;
import com.andframe.api.query.ListQuery;
import com.andframe.api.service.UpdateService;
import com.andframe.api.task.TaskExecutor;
import com.andframe.api.viewer.ViewModuler;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.Viewer;
import com.andframe.application.AfApp;
import com.andframe.application.AfAppSettings;
import com.andframe.impl.viewer.ViewerWrapper;
import com.andframe.task.AfDispatcher;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 抄作简化
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public class AndFrame {

    private static InvocationHandler handler = new $$$();
    private static Api api = (Api) Proxy.newProxyInstance($$$.class.getClassLoader(), new Class[]{Api.class}, handler );
    private static Object[] lastWiths;

    interface $$ {
        <From,To> To[] convertArray(@NonNull From[] froms, @NonNull ModelConvertor<From, To> convertor);
        <From,To> List<To> convertList(@NonNull Iterable<From> froms, @NonNull ModelConvertor<From, To> convertor);
    }

    public interface Api extends $$,TaskExecutor, DialogBuilder, ViewQuery, Cacher, EventManager {
    }

    @SuppressWarnings("MethodNameSameAsClassName")
    @MainThread
    public static Api $(Object... withs) {
        return with(withs);
    }

    @MainThread
    public static Api with(Object... withs) {
        lastWiths = withs;
        return api;
    }

    //<editor-fold desc="获取实例">
    private static Toaster instanceToaster = null;
    private static Toaster getInstanceToast() {
        if (instanceToaster == null) {
            instanceToaster = AfApp.get().newToaster();
        }
        return instanceToaster;
    }
    private static Toaster getInstanceToast(Viewer viewer) {
        return AfApp.get().newToaster(viewer);
    }
    private static Cacher instanceDurableCacher = null;
    private static Cacher getInstanceDurableCacher() {
        if(instanceDurableCacher == null){
            instanceDurableCacher = AfApp.get().newDurableCacher();
        }
        return instanceDurableCacher;
    }
    private static SparseArray<Cacher> arrayDurable = new SparseArray<>();
    private static Cacher getInstanceDurableCacher(String name) {
        Cacher cacher = arrayDurable.get(name.hashCode());
        if(cacher == null){
            cacher = AfApp.get().newDurableCacher(name);
            arrayDurable.put(name.hashCode(), cacher);
        }
        return cacher;
    }

    private static Cacher instancePrivateCacher = null;
    private static Cacher getInstancePrivateCacher() {
        if(instancePrivateCacher == null){
            instancePrivateCacher = AfApp.get().newPrivateCacher();
        }
        return instancePrivateCacher;
    }
    private static SparseArray<Cacher> arrayPrivate = new SparseArray<>();
    private static Cacher getInstancePrivateCacher(String name) {
        Cacher cacher = arrayPrivate.get(name.hashCode());
        if(cacher == null){
            cacher = AfApp.get().newPrivateCacher(name);
            arrayPrivate.put(name.hashCode(), cacher);
        }
        return cacher;
    }
    private static TaskExecutor instanceExecutor;
    private static TaskExecutor getInstanceExecutor() {
        if (instanceExecutor == null) {
            instanceExecutor = AfApp.get().newTaskExecutor();
        }
        return instanceExecutor;
    }
    private static EventManager instanceEventManager;
    private static EventManager getInstanceEventManager() {
        if (instanceEventManager == null) {
            instanceEventManager = AfApp.get().newEventManager();
        }
        return instanceEventManager;
    }
    private static PagerManager instancePagerManager = null;
    private static PagerManager getInstancePagerManager() {
        if (instancePagerManager == null) {
            instancePagerManager = AfApp.get().newPagerManager();
        }
        return instancePagerManager;
    }

    private static UpdateService instanceUpdateService;
    private static UpdateService getInstanceUpdateService() {
        if (instanceUpdateService == null) {
            instanceUpdateService = AfApp.get().newUpdateService();
        }
        return instanceUpdateService;
    }
    //</editor-fold>

    //<editor-fold desc="快速分类">
    public static Toaster toast() {
        return getInstanceToast();
    }
    public static Toaster toast(Viewer viewer) {
        return getInstanceToast(viewer);
    }

    public static Cacher durable() {
        return getInstanceDurableCacher();
    }

    public static Cacher durable(String name) {
        return getInstanceDurableCacher(name);
    }

    public static Cacher cache() {
        return getInstancePrivateCacher();
    }

    public static Cacher cache(String name) {
        return getInstancePrivateCacher(name);
    }

    public static TaskExecutor task() {
        return getInstanceExecutor();
    }

    public static EventManager event() {
        return getInstanceEventManager();
    }

    public static UpdateService update() {
        return getInstanceUpdateService();
    }

    public static PagerManager pager(){
        return getInstancePagerManager();
    }

    public static AfAppSettings settings() {
        return AfAppSettings.getInstance();
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

    public static <T> ListQuery<T> query(Iterable<T> iterable) {
        return AfApp.get().newListQuery(iterable);
    }

    @SafeVarargs
    public static <T> ListQuery<T> query(T... arrays) {
        return AfApp.get().newListQuery(Arrays.asList(arrays));
    }

    @MainThread
    public static ViewQuery<? extends ViewQuery> query(Viewer viewer) {
        return AfApp.get().newViewQuery(viewer);
    }

    @MainThread
    public static ViewQuery<? extends ViewQuery> query(View view) {
        return AfApp.get().newViewQuery(new ViewerWrapper(view));
    }

    @MainThread
    public static ViewQuery<? extends ViewQuery> query(AfActivity activity) {
        return AfApp.get().newViewQuery(new ViewerWrapper(activity.getView()));
    }
    //</editor-fold>

    public static void dispatch(Runnable runnable){
        AfDispatcher.dispatch(runnable);
    }

    public static void dispatch(Runnable runnable, long delay){
        AfDispatcher.dispatch(runnable, delay);
    }

    public static void dispatch(long delay, Runnable runnable){
        AfDispatcher.dispatch(runnable, delay);
    }

    public static Api get() {
        return api;
    }

    private static Viewer getLastViewer() {
        if (lastWiths != null && lastWiths.length > 0) {
            if (lastWiths[0] instanceof ViewModuler) {
                return new ViewerWrapper(((ViewModuler) lastWiths[0]).getView());
            } else if (lastWiths[0] instanceof View) {
                return new ViewerWrapper(((View) lastWiths[0]));
            } else if (lastWiths[0] instanceof Viewer) {
                return ((Viewer) lastWiths[0]);
            } else if (lastWiths[0] instanceof Activity) {
                return new ViewerWrapper((Activity) lastWiths[0]);
            } else if (lastWiths[0] instanceof Fragment) {
                return new ViewerWrapper((Fragment) lastWiths[0]);
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

    private static String getLastString() {
        if (lastWiths != null && lastWiths.length > 0) {
            if (lastWiths[0] instanceof String) {
                return (String) lastWiths[0];
            }
        }
        return null;
    }

    private static class $$$ implements InvocationHandler,$$ {
        @Override
        public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
            try {
                if (method.getDeclaringClass().isAssignableFrom(TaskExecutor.class)) {
                    return method.invoke(getInstanceExecutor(), objects);
                } else if (method.getDeclaringClass().isAssignableFrom(DialogBuilder.class)) {
                    return method.invoke(AfApp.get().newDialogBuilder(getLastContext()), objects);
                } else if (method.getDeclaringClass().isAssignableFrom(ViewQuery.class)) {
                    return method.invoke(AfApp.get().newViewQuery(getLastViewer()), objects);
                } else if (method.getDeclaringClass().isAssignableFrom(Cacher.class)) {
                    return method.invoke(getInstancePrivateCacher(getLastString()), objects);
                } else if (method.getDeclaringClass().isAssignableFrom(EventManager.class)) {
                    return method.invoke(getInstanceEventManager(), objects);
                } else if (method.getDeclaringClass().isAssignableFrom($$.class)) {
                    return method.invoke(this, objects);
                }
                return null;
            } finally {
                lastWiths = null;
            }
        }

        @Override
        public <From, To> To[] convertArray(@NonNull From[] froms, @NonNull ModelConvertor<From, To> convertor) {
            Type type = ((ParameterizedType) convertor.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1];
            if (type instanceof ParameterizedType) {
                type = ((ParameterizedType) type).getRawType();
            }
            //noinspection unchecked
            To[] tos = (To[]) Array.newInstance((Class<?>) type, froms.length);
            for (int i = 0; i < froms.length; i++) {
                tos[i] = (convertor.convert(froms[i]));
            }
            return tos;
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
