package com.andframe.api.pager.status;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.andframe.api.EmptyDecider;
import com.andframe.api.pager.load.LoadHelper;
import com.andframe.api.task.Task;

/**
 * 多状态页面帮助类接口（自动添加【下拉刷新控件】和【多页状态控件】）
 * 【下拉刷新控件@{@link RefreshLayoutManager}】
 *      可以实现下拉刷新内容视图的数据
 * 【多页状态控件@{@link StatusLayoutManager}】
 *      可以在没有数据或者加载失败或加载时隐藏内容视图切换到对应的【空数据页面】【错误页面】【正在加载页面】
 * Created by SCWANG on 2016/10/22.
 */

public interface StatusHelper<T> extends LoadHelper<T>, EmptyDecider<T> {

    //<editor-fold desc="初始方法">

    /**
     * 页面视图创建完成（内部会调用findContentView，initRefreshLayoutManager，initStatusLayoutManager）
     */
    void onViewCreated() ;

    /**
     * 设置是否在onViewCreated中执行加载任务
     * 在 onViewCreated 之前设置才有效
     * @param loadOrNot 执行加载任务或者不执行
     */
    void setLoadTaskOnViewCreated(boolean loadOrNot);

    /**
     * 查找内容视图
     * 默认会查找 以下控件
     * <br>1、 @{@link android.widget.AbsListView} 的所有子类
     * <br>    包括 @{@link android.widget.ListView} @{@link android.widget.GridView} 等等
     * <br>2、 @{@link android.support.v7.widget.RecyclerView} 以及其子类（自定义的子类）
     * <br>3、 @{@link android.widget.ScrollView} 以及其子类（自定义的子类）
     * <br>4、 或者整个布局作为内容视图
     * 内容视图将会被包裹一层【下拉刷新控件】和【多页状态控件】
     * @return 如果返回null将不会包裹【下拉刷新控件】和【多页状态控件】也会相应失去对应的功能
     */
    @Nullable
    View findContentView();
    /**
     * 初始化【下拉刷新控件】，内部会调用 newRefreshLayoutManager
     * @param content findContentView 返回的内容视图
     * @return 如果返回null将会失去下拉刷新功能
     */
    @Nullable
    RefreshLayoutManager initRefreshLayoutManager(View content);

    /**
     * 初始化【多页状态控件】，内部会调用 newStatusLayoutManager
     * @param content findContentView 返回的内容视图
     * @return 如果返回null将会失去下拉刷新功能
     */
    @Nullable
    StatusLayoutManager initStatusLayoutManager(View content);

    /**
     * 创建下拉刷新布局（如果要使用网上开源的下拉刷新控件可以重写这个方法）
     * @param context 上下文
     * @return 不能返回null，否则在initRefreshLayout中会出现空指针
     */
    @NonNull
    RefreshLayoutManager newRefreshLayoutManager(Context context);

    /**
     * 创建多页状态布局（如果要使用网上开源的下拉刷新控件可以重写这个方法）
     * @param context 上下文
     * @return 不能返回null，否则在initStatusLayout中会出现空指针
     */
    @NonNull
    StatusLayoutManager newStatusLayoutManager(Context context);

    /**
     * 初始化【下拉刷新控件和多页状态控件】，内部会调用 initRefreshLayoutManager 和 initStatusLayoutManager
     * @param refreshContent findContentView 返回的内容视图
     * @param statusContent findContentView 返回的内容视图
     */
    void initRefreshAndStatusManager(@NonNull View refreshContent, @NonNull View statusContent);

    /**
     * 注入 【下拉刷新控件和多页状态控件】 并排序：默认【下拉刷新控件】在【多页状态控件】内部
     */
    void initRefreshAndStatusManagerOrder(LayoutManager refresh, LayoutManager status, View content, ViewGroup parent, int index, LayoutParams lp);

    //</editor-fold>

    //<editor-fold desc="状态显示">

    /**
     * 状态显示中的方法在没有【多页状态控件】的条件下使用
     * 系统dialog或者Toast代替
     * 也可以自己实现
     */
    void showEmpty();
    void showEmpty(@NonNull String message);
    void showInvalidNet();
    void showProgress();
    void showContent();
    void showContent(@NonNull T model);
    void showProgress(@NonNull String progress);
    void showError(@NonNull String error);
    void showStatus(StatusLayoutManager.Status status, String... msg);
    //</editor-fold>

    //<editor-fold desc="任务加载">
    /**
     * 任务成功执行结束（如果model不为空会调用onTaskLoaded）
     * 会调用 isEmpty 来判断是否为空数据
     * @param model 任务加载的数据
     */
    void onTaskSucceed(@Nullable T model);

    /**
     * 任务执行失败
     * @param task 发生错误的任务对象
     */
    void onTaskFailed(@NonNull Task task);
    /**
     * 返回加载任务是否正在执行
     */
    boolean isLoading();
    //</editor-fold>

}
