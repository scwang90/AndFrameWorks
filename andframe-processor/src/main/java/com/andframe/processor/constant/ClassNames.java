package com.andframe.processor.constant;

import com.squareup.javapoet.ClassName;

/**
 * 类名
 * Created by SCWANG on 2017/8/6.
 */

public class ClassNames {
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_DIALOG = ClassName.get("android.view", "Dialog");
    public static final ClassName ANDROID_ACTIVITY = ClassName.get("android.view", "Activity");
    public static final ClassName ANDROID_CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName ANDROID_RESOURCES = ClassName.get("android.content.res", "Resources");
    public static final ClassName ANDROID_R = ClassName.get("android", "R");
    public static final ClassName SUPPRESS_LINT = ClassName.get("android.annotation", "SuppressLint");
    public static final ClassName ANDFRAME_UTILS = ClassName.get("com.andframe.util", "Utils");

    private ClassNames() {
    }
}
