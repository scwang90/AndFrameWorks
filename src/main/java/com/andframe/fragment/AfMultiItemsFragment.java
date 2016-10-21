package com.andframe.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.api.ListItem;
import com.andframe.api.ListItemAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.page.ItemsPager;
import com.andframe.api.page.ItemsPagerHelper;
import com.andframe.api.view.ItemsViewer;
import com.andframe.impl.helper.AfItemsPagerHelper;
import com.andframe.model.Page;
import com.andframe.task.AfHandlerTask;

import java.util.List;

/**
 * 多项数据页面
 * Created by SCWANG on 2016/10/21.
 */

public abstract class AfMultiItemsFragment<T> extends AfMultiStatusFragment<List<T>> implements ItemsPager<T> {

    protected ItemsViewer mItemsViewer;
    protected ListItemAdapter<T> mAdapter;
    protected ItemsPagerHelper<T> mItemsHelper = new AfItemsPagerHelper<>(this);

    //<editor-fold desc="初始化">
    /**
     * 初始化页面
     */
    @Override
    protected void onViewCreated() throws Exception {
        mLoadOnViewCreated = false;
        mItemsViewer = mItemsHelper.onViewCreated();
        super.onViewCreated();
    }

    @Override
    protected View findContentView() {
        return mItemsViewer.getItemsView();
    }

    //</editor-fold>

    //<editor-fold desc="子类实现">
    /**
     *
     * 获取列表控件
     *
     * @param pager 页面对象
     * @return pager.findListViewById(id)
     */
    @Override
    public abstract ItemsViewer findItemsViewer(ItemsPager<T> pager);


    /**
     * 获取列表项布局Item
     * 如果重写 newAdapter 之后，本方法将无效
     *
     * @return 实现 布局接口 ListItem 的Item兑现
     * new LayoutItem implements ListItem<T>(){}
     */
    public abstract ListItem<T> newListItem();
    //</editor-fold>

    //<editor-fold desc="原始事件">
    /**
     * 数据列表点击事件
     *
     * @param parent 列表控件
     * @param view   被点击的视图
     * @param index  被点击的index
     * @param id     被点击的视图ID
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
        mItemsHelper.onItemClick(parent, view, index, id);
    }
    /**
     * 数据列表点击事件
     *
     * @param parent 列表控件
     * @param view   被点击的视图
     * @param index  被点击的index
     * @param id     被点击的视图ID
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int index, long id) {
        return mItemsHelper.onItemLongClick(parent, view, index, id);
    }

    @Override
    public boolean onMore() {
        return mItemsHelper.onMore();
    }

    //</editor-fold>

    //<editor-fold desc="子类重写">

    /**
     * 创建加载更多的视图
     */
    @Override
    public MoreFooter<T> newMoreFooter() {
        return mItemsHelper.newMoreFooter();
    }

    @Override
    public boolean setMoreShow(AfHandlerTask task, List<T> list) {
        return mItemsHelper.setMoreShow(task, list);
    }

    /**
     * onItemClick 事件的 包装 一般情况下子类可以重写这个方法
     *
     * @param model 被点击的数据model
     * @param index 被点击的index
     */
    @Override
    public void onItemClick(T model, int index) {

    }

    /**
     * onItemLongClick 事件的 包装 一般情况下子类可以重写这个方法
     *
     * @param model 被点击的数据model
     * @param index 被点击的index
     */
    @Override
    public boolean onItemLongClick(T model, int index) {
        return false;
    }
    /**
     * 根据数据ltdata新建一个 适配器 重写这个方法之后getItemLayout方法将失效
     *
     * @param context Context对象
     * @param list  完成加载数据
     * @return 新的适配器
     */
    @Override
    public ListItemAdapter<T> newAdapter(Context context, List<T> list) {
        return mAdapter = mItemsHelper.newAdapter(context, list);
    }

    /**
     * 为列表添加 Header 和 Footer
     * （在bindAdapter之前执行）
     */
    @Override
    public void bindListHeaderAndFooter() {

    }

    /**
     * 绑定适配器
     * @param listView 列表
     * @param adapter 适配器
     */
    @Override
    public void bindAdapter(ItemsViewer listView, ListAdapter adapter) {
        mItemsHelper.bindAdapter(listView, adapter);
    }

    @Override
    public void onTaskLoaded(AfHandlerTask task, List<T> list) {
        mItemsHelper.onTaskLoaded(task, list);
    }

    @Override
    public void onTaskMoreLoaded(AfHandlerTask task, List<T> list) {
        mItemsHelper.onTaskMoreLoaded(task, list);
    }

    @Override
    public List<T> onTaskLoadList(Page page) throws Exception {
        return mItemsHelper.onTaskLoadList(page);
    }
    //</editor-fold>

}
