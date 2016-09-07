package com.andframe;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.view.View;

import com.andframe.adapter.listitem.AfListItem;
import com.andframe.api.DialogBuilder;
import com.andframe.api.ModelConvertor;
import com.andframe.api.TaskExecutor;
import com.andframe.api.ViewModuler;
import com.andframe.api.ViewQuery;
import com.andframe.api.page.Pager;
import com.andframe.application.AfApp;
import com.andframe.module.AfViewModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by SCWANG on 2016/9/2.
 */
@SuppressWarnings("unused")
public class $ {

    private static InvocationHandler handler = new $$$();
    private static Api api = (Api) Proxy.newProxyInstance($$$.class.getClassLoader(), new Class[]{Api.class}, handler );
    private static Object[] lastWiths;

    public interface $$ {
        <From,To> List<To> convertList(@NonNull Iterable<From> froms, @NonNull ModelConvertor<From,To> convertor);
    }


    public interface Api extends $$,TaskExecutor, DialogBuilder, ViewQuery {
    }

    @MainThread
    public static Api with(Object... withs) {
        lastWiths = withs;
        return api;
    }

    public static Api get() {
        return api;
    }

    protected static View getLastView() {
        if (lastWiths != null && lastWiths.length > 0) {
            if (lastWiths[0] instanceof ViewModuler) {
                return ((ViewModuler) lastWiths[0]).getView();
            } else if (lastWiths[0] instanceof View) {
                return ((View) lastWiths[0]);
            } else if (lastWiths[0] instanceof Pager) {
                return ((Pager) lastWiths[0]).getView();
            } else if (lastWiths[0] instanceof AfListItem) {
                return ((AfListItem) lastWiths[0]).getLayout();
            } else if (lastWiths[0] instanceof Activity) {
                return ((Activity) lastWiths[0]).getWindow().getDecorView().findViewById(android.R.id.content);
            }
        }
        return null;
    }

    protected static Context getLastContext() {
        if (lastWiths != null && lastWiths.length > 0) {
            if (lastWiths[0] instanceof Context) {
                return ((Context) lastWiths[0]);
            } else if (lastWiths[0] instanceof Pager) {
                return ((Pager) lastWiths[0]).getContext();
            } else if (lastWiths[0] instanceof AfListItem) {
                return ((AfListItem) lastWiths[0]).getLayout().getContext();
            } else if (lastWiths[0] instanceof AfViewModule) {
                return ((AfViewModule) lastWiths[0]).getView().getContext();
            }
        }
        return null;
    }

    protected static class $$$ implements InvocationHandler,$$ {
        @Override
        public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
            if (method.getDeclaringClass().isAssignableFrom(TaskExecutor.class)) {
                return method.invoke(AfApp.get().newTaskExecutor(), objects);
            } else if (method.getDeclaringClass().isAssignableFrom(DialogBuilder.class)) {
                return method.invoke(AfApp.get().newDialogBuilder(getLastContext()), objects);
            } else if (method.getDeclaringClass().isAssignableFrom(ViewQuery.class)) {
                return method.invoke(AfApp.get().newViewQuery(getLastView()), objects);
            } else if (method.getDeclaringClass().isAssignableFrom($$.class)) {
                return method.invoke(this, objects);
            }
            return null;
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
