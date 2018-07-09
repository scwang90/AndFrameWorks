package com.andframe.impl.helper;

import com.andframe.api.viewer.ViewQuery;
import com.andframe.api.viewer.ViewQueryHelper;
import com.andframe.api.viewer.Viewer;
import com.andframe.application.AfApp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ViewQuery 继承帮助类
 * Created by SCWANG on 2016/9/8.
 */
public class AfViewQueryHelper implements /*ViewQueryHelper, */InvocationHandler {


    protected Viewer viewer;
    protected ViewQuery<? extends ViewQuery> mViewQuery;

    protected AfViewQueryHelper(Viewer viewer) {
        this.viewer = viewer;
    }

    public static ViewQuery<? extends ViewQuery> newHelper(Viewer viewer) {
        AfViewQueryHelper helper = new AfViewQueryHelper(viewer);
        ClassLoader loader = ViewQueryHelper.class.getClassLoader();
        return (ViewQuery<? extends ViewQuery>) Proxy.newProxyInstance(loader, new Class[]{ViewQuery.class}, helper);
    }

//    protected ViewQuery<? extends ViewQuery> getQuery() {
//        if (mViewQuery == null || mViewQuery.rootViewer() != viewer) {
//            mViewQuery = AfApp.get().newViewQuery(viewer);
//        }
//        return mViewQuery;
//    }

//    /**
//     * 开始 ViewQuery 查询
//     * @param views 可选的多个 View
//     */
//    @SuppressWarnings("unused")
//    public ViewQuery<? extends ViewQuery> $(View... views) {
//        return getQuery().$(views);
//    }
//
//    @Override
//    public ViewQuery<? extends ViewQuery> $(Collection<View> views) {
//        return getQuery().$(views);
//    }
//
//    @Override
//    public ViewQuery<? extends ViewQuery> $(Class<? extends View>[] types) {
//        return getQuery().$(types);
//    }
//
//    @Override
//    public ViewQuery<? extends ViewQuery> $(Integer id, int... ids) {
//        return getQuery().$(id, ids);
//    }
//
//    @Override
//    public ViewQuery<? extends ViewQuery> $(String idValue, String... idValues) {
//        return getQuery().$(idValue, idValues);
//    }
//
//    @Override
//    public ViewQuery<? extends ViewQuery> $(Class<? extends View> type) {
//        return getQuery().$(type);
//    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (mViewQuery == null/* || mViewQuery.rootViewer() != viewer*/) {
            mViewQuery = AfApp.get().newViewQuery(viewer);
            mViewQuery.cacheIdEnable(true);
        }
        return method.invoke(mViewQuery, args);
//        return method.invoke(getQuery(), args);
    }
}
