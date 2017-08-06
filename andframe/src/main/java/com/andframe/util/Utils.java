package com.andframe.util;

import android.support.annotation.IdRes;
import android.view.View;

import java.lang.reflect.Array;

/**
 * 工具集
 * Created by SCWANG on 2017/8/6.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Utils {

    public static <T> T findOptionalViewAsType(View source, @IdRes int id, String who, Class<T> cls) {
        View view = source.findViewById(id);
        return castView(view, id, who, cls);
    }

    public static <T> T findRequiredViewAsType(View source, @IdRes int id, String who, Class<T> cls) {
        View view = findRequiredView(source, id, who);
        return castView(view, id, who, cls);
    }

    public static <T> T castView(View view, @IdRes int id, String who, Class<T> cls) {
        try {
            return cls.cast(view);
        } catch (ClassCastException e) {
            String name = getResourceEntryName(view, id);
            throw new IllegalStateException("View '"
                    + name
                    + "' with ID "
                    + id
                    + " for "
                    + who
                    + " was of the wrong type. See cause for more info.", e);
        }
    }

    public static View findRequiredView(View source, @IdRes int id, String who) {
        View view = source.findViewById(id);
        if (view != null) {
            return view;
        }
        String name = getResourceEntryName(source, id);
        throw new IllegalStateException("Required view '"
                + name
                + "' with ID "
                + id
                + " for "
                + who
                + " was not found. If this view is optional add '@Nullable' (fields) or '@Optional'"
                + " (methods) annotation.");
    }

    private static String getResourceEntryName(View view, @IdRes int id) {
        if (view.isInEditMode()) {
            return "<unavailable while editing>";
        }
        return view.getContext().getResources().getResourceEntryName(id);
    }


    @SafeVarargs
    public static <T> T[] arrayOf(T... views) {
        return filterNull(views);
    }

    private static <T> T[] filterNull(T[] views) {
        int end = 0;
        int length = views.length;
        for (int i = 0; i < length; i++) {
            T view = views[i];
            if (view != null) {
                views[end++] = view;
            }
        }
        if (end == length) {
            return views;
        }
        //noinspection unchecked
        T[] newViews = (T[]) Array.newInstance(views.getClass().getComponentType(), end);
        System.arraycopy(views, 0, newViews, 0, end);
        return newViews;
    }
}
