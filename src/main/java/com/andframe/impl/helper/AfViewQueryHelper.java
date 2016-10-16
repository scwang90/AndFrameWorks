package com.andframe.impl.helper;

import android.view.View;

import com.andframe.api.view.ViewQuery;
import com.andframe.api.view.ViewQueryHelper;
import com.andframe.api.view.Viewer;
import com.andframe.application.AfApp;

/**
 * ViewQuery 继承帮助类
 * Created by SCWANG on 2016/9/8.
 */
public class AfViewQueryHelper implements ViewQueryHelper {

    protected Viewer viewer;
    protected ViewQuery<? extends ViewQuery> mViewQuery;

    public AfViewQueryHelper(Viewer viewer) {
        this.viewer = viewer;
    }

    protected ViewQuery<? extends ViewQuery> getQuery() {
        if (mViewQuery == null || mViewQuery.$().view() != viewer.getView()) {
            mViewQuery = AfApp.get().newViewQuery(viewer.getView());
        }
        return mViewQuery;
    }

    /**
     * 开始 ViewQuery 查询
     * @param id 控件Id
     */
    @SuppressWarnings("unused")
    public ViewQuery $(int id, int... ids) {
        return getQuery().$(id, ids);
    }
    /**
     * 开始 ViewQuery 查询
     * @param views 可选的多个 View
     */
    @SuppressWarnings("unused")
    public ViewQuery $(View... views) {
        ViewQuery<? extends ViewQuery> query = getQuery();
        if (views == null || views.length == 0) {
            return query;
        }
        return query.$(views);
    }

}
