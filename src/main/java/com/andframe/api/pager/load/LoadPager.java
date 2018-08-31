package com.andframe.api.pager.load;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andframe.api.pager.Pager;

/**
 * 加载页面支持 （自动添加【下拉刷新控件】）
 * 【下拉刷新控件】可以实现下拉刷新内容视图的数据
 * Created by SCWANG on 2017/5/5.
 */

public interface LoadPager<T> extends Pager, LoadHelper<T> {

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
