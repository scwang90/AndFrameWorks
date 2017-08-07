package com.andframe.processor.constant;

import com.squareup.javapoet.ClassName;

/**
 * 组件类型
 * Created by SCWANG on 2017/8/8.
 */

public enum  ComponentType {
    Activity(ClassNames.ANDROID_ACTIVITY),
    Fragment(ClassNames.SUPPRESS_FRAGMENT),;

    public ClassName className;

    ComponentType(ClassName className) {
        this.className = className;
    }
}
