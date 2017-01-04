package com.andframe.helper.android;

import android.view.View;

import com.andframe.activity.framework.AfViewable;
import com.andframe.activity.framework.IViewQuery;
import com.andframe.application.AfApp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * IViewQuery 继承帮助类
 * Created by SCWANG on 2016/9/8.
 */
public class AfViewQueryHelper implements InvocationHandler {


    protected AfViewable viewer;
    protected IViewQuery<? extends IViewQuery> mViewQuery;

    protected AfViewQueryHelper(AfViewable viewer) {
        this.viewer = viewer;
    }

    public static IViewQuery<? extends IViewQuery> newHelper(AfViewable viewer) {
        AfViewQueryHelper helper = new AfViewQueryHelper(viewer);
        ClassLoader loader = AfViewQueryHelper.class.getClassLoader();
        return (IViewQuery<? extends IViewQuery>) Proxy.newProxyInstance(loader, new Class[]{IViewQuery.class}, helper);
    }

    protected IViewQuery<? extends IViewQuery> getQuery() {
        if (mViewQuery == null || mViewQuery.rootViewer() != viewer) {
            mViewQuery = AfApp.get().getViewQuery(viewer.getView());
        }
        return mViewQuery;
    }

    /**
     * 开始 IViewQuery 查询
     * @param views 可选的多个 View
     */
    @SuppressWarnings("unused")
    public IViewQuery<? extends IViewQuery> $(View... views) {
        return getQuery().$(views);
    }

    public IViewQuery<? extends IViewQuery> $(Class<? extends View> type, Class<? extends View>... types) {
        return getQuery().$(type, types);
    }

    public IViewQuery<? extends IViewQuery> $(Integer id, int... ids) {
        return getQuery().$(id, ids);
    }

    public IViewQuery<? extends IViewQuery> $(String idvalue, String... idvalues) {
        return getQuery().$(idvalue, idvalues);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(getQuery(), args);
    }
}
