package com.andframe.api.pager.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.annotation.mark.MarkCache;
import com.andframe.api.Paging;
import com.andframe.api.adapter.AnimatedAdapter;
import com.andframe.api.adapter.HeaderFooterAdapter;
import com.andframe.api.adapter.ItemsViewerAdapter;
import com.andframe.api.pager.status.RefreshLayoutManager;
import com.andframe.api.pager.status.StatusHelper;
import com.andframe.api.task.TaskWithPaging;
import com.andframe.api.viewer.ItemsViewer;

import java.util.Date;
import java.util.List;

/**
 * 基本单列表页帮助类接口
 * 具备【数据缓存】和【数据分页】以及 StatusHelper 中的功能
 * Created by SCWANG on 2016/9/7.
 */
public interface ItemsHelper<T> extends StatusHelper<List<T>>, OnItemClickListener, OnItemLongClickListener, OnMoreListener/*, ViewQueryHelper*/ {

    //<editor-fold desc="初始方法">

    /**
     * 在页面视图中查找【列表控件】
     * 默认会查找 以下控件
     * <br>1、 @{@link android.widget.AbsListView} 的所有子类
     * <br>    包括 @{@link android.widget.ListView} @{@link android.widget.GridView} 等等
     * <br>2、 @{@link android.support.v7.widget.RecyclerView} 以及其子类（自定义的子类）
     * @param contentView @{@link com.andframe.api.pager.status.StatusPager} 中的内容视图
     * @return 被抽象的 @{@link ItemsViewer} 对象
     */
    @NonNull
    ItemsViewer findItemsViewer(View contentView);

    /**
     * 如果@newRefreshLayouter中返回的下拉刷新控件】@{@link RefreshLayoutManager}
     * 没有实现【加载更多接口】 @{@link MoreLayoutManager} 则会使用滚动底部自动加载更多
     * @return 在列表底部显示正在加载更多的布局 @{@link MoreFooter}
     */
    MoreFooter newMoreFooter();

    /**
     * 初始化适配器（内部会调用@newAdapter，子类最好不要改变返回值，否则会有功能丢失）
     * @return 默认返回的适配器并不是@newAdapter中返回的适配器，而是被包装过的适配器对象
     */
    @NonNull
    ItemsViewerAdapter<T> initAdapter();

    /**
     * 创建适配器（子类在自定义适配器时最好继承@{@link com.andframe.adapter.AfListAdapter}）
     * @param context 上下文
     * @param list 初始数据
     */
    @NonNull
    ItemsViewerAdapter<T> newAdapter(@NonNull Context context, @NonNull List<T> list);

    /**
     * 把适配绑定到列表控件
     * @param itemsViewer 被抽象成@{@link ItemsViewer}的列表控件
     * @param adapter 适配器@initAdapter中返回的适配器
     */
    void bindAdapter(@NonNull ItemsViewer itemsViewer, @NonNull ListAdapter adapter);

    /**
     * 如果页面列表需要改变列表动画属性可以重写本方法添加
     * @param adapter 带有动画的适配器
     */
    void initItemsAnimated(AnimatedAdapter<T> adapter);

    /**
     * 如果页面列表需要头部或者尾部可以重写本方法添加
     * @param adapter 带有头部和尾部的适配器
     */
    void initHeaderAndFooter(@NonNull HeaderFooterAdapter<T> adapter);

    /**
     * 获取列表ItemView 的类型总数
     * @return 默认1
     */
    int getViewTypeCount();

    /**
     * 获取指定位置的ItemView 的类型值
     * @param position 位置索引
     * @return 默认0
     */
    int getItemViewType(int position);

    /**
     * 数据改变
     */
    void onDataChanged();

    //</editor-fold>

    //<editor-fold desc="缓存相关">
    /**
     * 初始化缓存（默认并不会打开缓存功能，需要使用@{@link MarkCache}才会开启缓存功能）
     * 内部会调用 @getCacheKey
     */
    void initCache();
    /**
     * 清空本页面的缓存数据
     */
    void clearCache();
    /**
     * 手动调用更新缓存（使用Adapter中的数据）
     */
    void putCache();
    /**
     * 手动调用更新缓存
     * @param list 要缓存的数据
     */
    void putCache(@NonNull List<T> list);
    /**
     * 获取缓存的Key保证各个页面的缓存数据不冲突
     * 如果数据和用户相关，子类需要重写本方法并在key中添加用户唯一特性
     * @param mark @{@link MarkCache} 缓存标记
     */
    @NonNull
    String getCacheKey(MarkCache mark);
    /**
     * 获取缓存时间，用于检测缓存过期
     * @return 如果没有缓存返回new Date(0)
     */
    @NonNull
    Date getCacheTime();
    /**
     * 任务执行添加缓存（会覆盖）
     * @param list 缓存数据
     */
    void onTaskPutCache(List<T> list);
    /**
     * 任务执行追加缓存（不会覆盖，分页加载时候会调用）
     * @param list 缓存数据
     */
    void onTaskPushCache(List<T> list);

    /**
     * 获取缓存中的数据
     * @param isCheckExpired 是否需要检查缓存过期
     */
    @Nullable
    List<T> onTaskLoadCache(boolean isCheckExpired);

    /**
     * 任务加载缓存结束
     * 内部默认会调用 @onTaskLoadedRefresh方法 实现页面显示
     * @param task 加载缓存的任务对象
     * @param list 任务加载返回的缓存数据
     */
    void onTaskLoadedCache(@NonNull TaskWithPaging task, @Nullable List<T> list);
    //</editor-fold>

    //<editor-fold desc="任务相关">

    /**
     * 刷新任务执行结束
     * 内部会调用@finishRefresh @finishRefreshFail @setMoreShow 方法控制页面显示
     * @param task 执行刷新的任务对象
     * @param list 刷新任务返回的数据
     */
    void onTaskLoadedRefresh(@NonNull TaskWithPaging task, @Nullable List<T> list);

    /**
     * 加载更多数据结束
     * 内部会调用 @setMoreShow 如果返回false 会默认提示【所有数据加载完毕】
     * @param task 加载更多的任务对象
     * @param list 加载更多返回的数据
     */
    void onTaskLoadedMore(@NonNull TaskWithPaging task, @Nullable List<T> list);

    //</editor-fold>

    //<editor-fold desc="页面状态">

//    /**
//     * 刷新任务执行成功之后显示数据
//     */
//    void finishRefresh();
//
//    /**
//     * 刷新任务加载失败后更新显示失败的状态
//     */
//    void finishRefreshFail();

    /**
     * 刷新任务结束
     * @param success 刷新是否成功
     */
    void finishRefresh(boolean success);

    /**
     * 数据加载完毕之后控制页面是否显示加载更多的状态 内部会调用 @setLoadMoreEnabled
     * @param task 加载数据的任务
     * @param list 任务加载返回的数据
     * @return 返回 false 并且【非第一次加载跟多】时候 会默认提示 所有数据加载完毕
     */
    boolean setMoreShow(@NonNull TaskWithPaging task, @Nullable List<T> list);

    /**
     * 手动设置页面是否显示加载更多的状态
     * @param enabled true 可以加载更多，false 关闭分页
     */
    void setLoadMoreEnabled(boolean enabled);

    /**
     * 创建分页对象
     * @param size 分页大小
     * @param start 开始位置
     * @return null 表示不需要分頁
     */
    @Nullable
    Paging newPaging(int size, int start);


//    /**
//     * 包装 ViewQuery
//     * @param $$ 源
//     * @return 包装之后的 ViewQuery
//     */
//    ViewQuery<? extends ViewQuery> wrap(ViewQuery<? extends ViewQuery> $$);

    //</editor-fold>

}
