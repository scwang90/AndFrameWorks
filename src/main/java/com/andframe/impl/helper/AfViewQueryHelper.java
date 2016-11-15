package com.andframe.impl.helper;

import android.view.View;

import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.ViewQueryHelper;
import com.andframe.api.view.Viewer;
import com.andframe.application.AfApp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ViewQuery 继承帮助类
 * Created by SCWANG on 2016/9/8.
 */
public class AfViewQueryHelper implements ViewQueryHelper, InvocationHandler {


    protected Viewer viewer;
    protected ViewQuery<? extends ViewQuery> mViewQuery;

    public AfViewQueryHelper(Viewer viewer) {
        this.viewer = viewer;
    }

    public static ViewQuery<? extends ViewQuery> newHelper(Viewer viewer) {
        AfViewQueryHelper helper = new AfViewQueryHelper(viewer);
        ClassLoader loader = ViewQueryHelper.class.getClassLoader();
        return (ViewQuery<? extends ViewQuery>) Proxy.newProxyInstance(loader, new Class[]{ViewQuery.class}, helper);
    }

    protected ViewQuery<? extends ViewQuery> getQuery() {
        if (mViewQuery == null || mViewQuery.rootViewer() != viewer) {
            mViewQuery = AfApp.get().newViewQuery(viewer);
        }
        return mViewQuery;
    }

    /**
     * 开始 ViewQuery 查询
     * @param id 控件Id
     */
    @SuppressWarnings("unused")
    public ViewQuery<? extends ViewQuery> $(int id, int... ids) {
        return getQuery().$(id, ids);
    }
    /**
     * 开始 ViewQuery 查询
     * @param views 可选的多个 View
     */
    @SuppressWarnings("unused")
    public ViewQuery<? extends ViewQuery> $(View... views) {
        ViewQuery<? extends ViewQuery> query = getQuery();
        if (views == null || views.length == 0) {
            return query;
        }
        return query.$(views);
    }

    public ViewQuery<? extends ViewQuery> id(int... ids) {
        return getQuery().id(ids);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(getQuery(), args);
    }
}
