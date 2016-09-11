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
    protected ViewQuery mViewQuery;

    public AfViewQueryHelper(Viewer viewer) {
        this.viewer = viewer;
    }

    protected ViewQuery getmLayout() {
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
    public ViewQuery $(int... id) {
        ViewQuery query = getmLayout();
        if (id == null || id.length == 0) {
            return query;
        }
        return query.id(id);
    }
    /**
     * 开始 ViewQuery 查询
     * @param view 至少一个 View
     * @param views 可选的多个 View
     */
    @SuppressWarnings("unused")
    public ViewQuery $(View view, View... views) {
        return getmLayout().$(view, views);
    }

}
