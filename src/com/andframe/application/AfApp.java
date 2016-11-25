package com.andframe.application;

import android.view.View;

import com.andframe.activity.framework.AfView;
import com.andframe.activity.framework.IViewQuery;

/**
 *
 * Created by SCWANG on 2016/8/17.
 */
@SuppressWarnings("unused")
public abstract class AfApp extends AfApplication {

    public static AfApp get() {
        return (AfApp) getApp();
    }

    /**
     * 获取 ViewQuery
     * @return handle
     */
    public IViewQuery<? extends IViewQuery> getViewQuery(View view) {
        return new AfView(view);
    }

}
