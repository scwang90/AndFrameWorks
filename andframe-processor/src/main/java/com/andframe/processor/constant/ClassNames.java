package com.andframe.processor.constant;

import com.squareup.javapoet.ClassName;

/**
 * 类名
 * Created by SCWANG on 2017/8/6.
 */

public class ClassNames {
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ACTIVITY = ClassName.get("android.app", "Activity");
    public static final ClassName ANDROID_CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName ANDROID_BUNDLE = ClassName.get("android.os", "Bundle");
    public static final ClassName ANDROID_RESOURCES = ClassName.get("android.content.res", "Resources");
    public static final ClassName ANDROID_INFLATER = ClassName.get("android.view", "LayoutInflater");
    public static final ClassName ANDROID_VIEWGROUP = ClassName.get("android.view", "ViewGroup");
    public static final ClassName ANDROID_R = ClassName.get("android", "R");
    public static final ClassName ANDFRAME_UTILS = ClassName.get("com.andframe.util", "Utils");
    public static final ClassName ANDFRAME_LISTENER = ClassName.get("com.andframe.listener", "SafeListener");
    public static final ClassName SUPPORT_FRAGMENT = ClassName.get("android.support.v4.app", "Fragment");
    public static final ClassName SUPPRESS_LINT = ClassName.get("android.annotation", "SuppressLint");
    public static final ClassName VIEWGROUP_LAYOUTPARAMS = ClassName.get("android.view.ViewGroup", "LayoutParams");

    public static final ClassName BITMAP_FACTORY = ClassName.get("android.graphics", "BitmapFactory");
    public static final ClassName CONTEXT_COMPAT =
            ClassName.get("android.support.v4.content", "ContextCompat");
    public static final ClassName ANIMATION_UTILS =
            ClassName.get("android.view.animation", "AnimationUtils");

    private ClassNames() {
    }
}
