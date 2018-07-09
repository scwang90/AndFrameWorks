package com.andframe.impl.viewer;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.andframe.api.viewer.Viewer;

/**
 * Viewer 封装实现
 * Created by SCWANG on 2016/10/29.
 */

public class ViewerWrapper implements Viewer {

    protected View view;
    protected Viewer viewer;
    protected Activity activity;
    protected Fragment fragment;

    public ViewerWrapper(Viewer viewer) {
        this.viewer = viewer;
    }

    public ViewerWrapper(View view) {
        this.view = view;
    }

    public ViewerWrapper(Activity activity) {
        this.activity = activity;
    }

    public ViewerWrapper(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Context getContext() {
        if (viewer != null) {
            return viewer.getContext();
        }
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
        if (viewer != null) {
            return viewer.getView();
        }
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
    public <T extends View> T findViewById(int id) {
        if (viewer != null) {
            return viewer.findViewById(id);
        }
        if (view != null) {
            return view.findViewById(id);
        }
        if (fragment != null && fragment.getView() != null) {
            return fragment.getView().findViewById(id);
        }
        if (activity != null) {
            return activity.findViewById(id);
        }
        return null;
    }

    @Override
    public <T extends View> T findViewById(int id, Class<T> clazz) {
        View view = findViewById(id);
        return clazz.isInstance(view) ? clazz.cast(view) : null;
    }
}
