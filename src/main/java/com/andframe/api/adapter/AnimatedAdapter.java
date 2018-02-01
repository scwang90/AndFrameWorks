package com.andframe.api.adapter;

/**
 * 带有动画的适配器
 * Created by SCWANG on 2018/2/1.
 */
@SuppressWarnings("SameParameterValue")
public interface AnimatedAdapter<T> extends ItemsViewerAdapter<T> {

    void setEnableAnimated(boolean enabled);
}
