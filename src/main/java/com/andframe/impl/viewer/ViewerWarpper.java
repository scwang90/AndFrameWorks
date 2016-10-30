package com.andframe.impl.viewer;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.api.view.Viewer;

/**
 * Viewer 封装实现
 * Created by SCWANG on 2016/10/29.
 */

public class ViewerWarpper implements Viewer {

    protected View view;
    protected Activity activity;
    protected Fragment fragment;

    public ViewerWarpper(View view) {
        this.view = view;
    }

    public ViewerWarpper(Activity activity) {
        this.activity = activity;
    }

    public ViewerWarpper(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Context getContext() {
        if (activity != null) {
            return activity;
        }
        if (view != null) {
            return view.getContext();
        }
        if (fragment != null) {
            return fragment.getContext();
        }
        return null;
    }

    @Override
    public View getView() {
        if (view != null) {
            return view;
        }
        if (activity != null) {
            return activity.getWindow().getDecorView().findViewById(android.R.id.content);
        }
        if (fragment != null) {
            return fragment.getView();
        }
        return null;
    }

    @Override
    public View findViewById(int id) {
        if (view != null) {
            return view.findViewById(id);
        }
        if (activity != null) {
            return activity.findViewById(id);
        }
        if (fragment != null && fragment.getView() != null) {
            return fragment.getView().findViewById(id);
        }
        return null;
    }

    @Override
    public <T extends View> T findViewByID(int id) {
        //noinspection unchecked
        return (T)findViewById(id);
    }

    @Override
    public <T extends View> T findViewById(int id, Class<T> clazz) {
        View view = findViewById(id);
        return clazz.isInstance(view) ? clazz.cast(view) : null;
    }
}
