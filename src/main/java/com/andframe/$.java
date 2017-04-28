package com.andframe;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.activity.AfActivity;
import com.andframe.api.Cacher;
import com.andframe.api.DialogBuilder;
import com.andframe.api.ModelConvertor;
import com.andframe.api.Toaster;
import com.andframe.api.event.EventManager;
import com.andframe.api.pager.PagerManager;
import com.andframe.api.task.Task;
import com.andframe.api.task.TaskExecutor;
import com.andframe.api.task.handler.WorkingHandler;
import com.andframe.api.viewer.ViewModuler;
import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.Viewer;
import com.andframe.application.AfApp;
import com.andframe.caches.AfDurableCache;
import com.andframe.caches.AfPrivateCaches;
import com.andframe.fragment.AfFragment;
import com.andframe.impl.AfToaster;
import com.andframe.impl.pager.AfPagerManager;
import com.andframe.impl.viewer.ViewerWarpper;
import com.andframe.task.AfDispatcher;
import com.andframe.task.AfTaskExecutor;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
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

    public interface Api extends $$,TaskExecutor, DialogBuilder, ViewQuery, Cacher, EventManager {
    }

    public static final Toaster toast = new ToasterWrapper(AfToaster.getInstance());

    public static final PagerManager pager = new PagerManagerWrapper(AfPagerManager.getInstance());

    public static final TaskExecutor task = new TaskExecutorWrapper(AfTaskExecutor.getInstance());

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

    public static Toaster toast() {
        return AfToaster.getInstance();
    }

    public static Cacher durable() {
        return AfDurableCache.getInstance();
    }

    public static Cacher durable(String name) {
        return AfDurableCache.getInstance(name);
    }

    public static Cacher cache() {
        return AfPrivateCaches.getInstance();
    }

    public static Cacher cache(String name) {
        return AfPrivateCaches.getInstance(name);
    }

    public static PagerManager pager(){
        return AfPagerManager.getInstance();
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
        return AfTaskExecutor.getInstance();
    }

    public static EventManager event() {
        return AfApp.get().getEventManager();
    }

    public static void dispatch(Runnable runnable){
        AfDispatcher.dispatch(runnable);
    }

    public static void dispatch(Runnable runnable, long delay){
        AfDispatcher.dispatch(runnable, delay);
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
                    return method.invoke(AfTaskExecutor.getInstance(), objects);
                } else if (method.getDeclaringClass().isAssignableFrom(DialogBuilder.class)) {
                    return method.invoke(AfApp.get().newDialogBuilder(getLastContext()), objects);
                } else if (method.getDeclaringClass().isAssignableFrom(ViewQuery.class)) {
                    return method.invoke(AfApp.get().newViewQuery(getLastViewer()), objects);
                } else if (method.getDeclaringClass().isAssignableFrom(Cacher.class)) {
                    return method.invoke(AfPrivateCaches.getInstance(getLastString()), objects);
                } else if (method.getDeclaringClass().isAssignableFrom(EventManager.class)) {
                    return method.invoke(AfApp.get().getEventManager(), objects);
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

    //<editor-fold desc="Wrapper">
    protected static class ToasterWrapper implements Toaster {

        Toaster wrapped;

        public ToasterWrapper(Toaster wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void makeToastLong(int resid) {
            wrapped.makeToastLong(resid);
        }

        @Override
        public void makeToastShort(int resid) {
            wrapped.makeToastShort(resid);
        }

        @Override
        public void makeToastLong(CharSequence tip) {
            wrapped.makeToastLong(tip);
        }

        @Override
        public void makeToastLong(CharSequence tip, Throwable e) {
            wrapped.makeToastLong(tip, e);
        }

        @Override
        public void makeToastShort(CharSequence tip) {
            wrapped.makeToastShort(tip);
        }

        @Override
        public void makeToastShort(CharSequence tip, Throwable e) {
            wrapped.makeToastShort(tip, e);
        }
    }

    protected static class TaskExecutorWrapper implements TaskExecutor {

        TaskExecutor wrapped;

        public TaskExecutorWrapper(TaskExecutor wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        @MainThread
        public void execute(WorkingHandler runnable) {
            wrapped.execute(runnable);
        }

        @Override
        @MainThread
        public void execute(Task task) {
            wrapped.execute(task);
        }

        @Override
        public <T extends Task> T postTask(T task) {
            return wrapped.postTask(task);
        }
    }

    protected static class PagerManagerWrapper implements PagerManager {
        PagerManager wrapped;

        public PagerManagerWrapper(PagerManager wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void onActivityCreated(AfActivity activity) {
            wrapped.onActivityCreated(activity);
        }

        @Override
        public void onActivityDestroy(AfActivity activity) {
            wrapped.onActivityDestroy(activity);
        }

        @Override
        public void onActivityResume(AfActivity activity) {
            wrapped.onActivityResume(activity);
        }

        @Override
        public void onActivityPause(AfActivity activity) {
            wrapped.onActivityPause(activity);
        }

        @Override
        public void onFragmentAttach(AfFragment fragment, Context context) {
            wrapped.onFragmentAttach(fragment, context);
        }

        @Override
        public void onFragmentDetach(AfFragment fragment) {
            wrapped.onFragmentDetach(fragment);
        }

        @Override
        public void onFragmentResume(AfFragment fragment) {
            wrapped.onFragmentResume(fragment);
        }

        @Override
        public void onFragmentPause(AfFragment fragment) {
            wrapped.onFragmentPause(fragment);
        }

        @Override
        public boolean hasActivityRuning() {
            return wrapped.hasActivityRuning();
        }

        @Override
        public boolean hasActivity(Class<? extends AfActivity> clazz) {
            return wrapped.hasActivity(clazz);
        }

        @Override
        public AfActivity currentActivity() {
            return wrapped.currentActivity();
        }

        @Override
        public AfActivity getActivity(Class<? extends AfActivity> clazz) {
            return wrapped.getActivity(clazz);
        }

        @Override
        public void finishCurrentActivity() {
            wrapped.finishCurrentActivity();
        }

        @Override
        public void finishActivity(AfActivity activity) {
            wrapped.finishActivity(activity);
        }

        @Override
        public void finishAllActivity() {
            wrapped.finishAllActivity();
        }

        @Override
        public void startForeground() {
            wrapped.startForeground();
        }

        @Override
        public void startActivity(Class<? extends Activity> clazz, Object... args) {
            wrapped.startActivity(clazz, args);
        }

        @Override
        public void startFragment(Class<? extends Fragment> clazz, Object... args) {
            wrapped.startFragment(clazz, args);
        }

        @Override
        public void startActivityForResult(Class<? extends Activity> clazz, int request, Object... args) {
            wrapped.startActivityForResult(clazz, request, args);
        }

        @Override
        public void startFragmentForResult(Class<? extends Fragment> clazz, int request, Object... args) {
            wrapped.startFragmentForResult(clazz, request, args);
        }
    }


    //</editor-fold>

}
