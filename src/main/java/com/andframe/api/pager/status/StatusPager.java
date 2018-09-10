package com.andframe.api.pager.status;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andframe.api.pager.load.LoadPager;

/**
 * 多状态页面支持 （自动添加【下拉刷新控件】和【多页状态控件】）
 * 【下拉刷新控件】可以实现下拉刷新内容视图的数据
 * 【多页状态控件】可以在没有数据或者加载失败或加载时隐藏内容视图切换到对应的【空数据页面】【错误页面】【正在加载页面】
 * Created by SCWANG on 2016/10/22.
 */

public interface StatusPager<T> extends LoadPager<T>, StatusHelper<T> {

    /**
     * 任务加载（异步线程，由框架自动发出执行）
     * @return 加载的数据
     */
    @Nullable
    T onTaskLoading() throws Exception;

    /**
     * 任务加载完成
     * @param model 加载的数据（不为空）
     */
    void onTaskLoaded(@NonNull T model);

}
