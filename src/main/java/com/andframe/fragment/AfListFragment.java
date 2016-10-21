package com.andframe.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.annotation.view.BindViewCreated;
import com.andframe.api.ListItem;
import com.andframe.api.ListItemAdapter;
import com.andframe.api.page.ListPager;
import com.andframe.api.page.ListPagerHelper;
import com.andframe.api.view.ItemsViewer;
import com.andframe.impl.helper.AfListPagerHelper;
import com.andframe.task.AfHandlerTask;

import java.util.List;

/**
 * 基本列表数据页
 * 1.持久的适配器（AfListAdapter）
 * 2.动态的列表项（ListItem）
 * 3.异步加载数据（task）
 * Created by SCWANG on 2016/9/7.
 */
public abstract class AfListFragment<T> extends AfTabFragment implements ListPager<T> {

//    protected AbsListView mListView;
//    protected AfListAdapter<T> mAdapter;

    protected ListPagerHelper<T> mListHelper = newListPagerHelper();
    protected ListItemAdapter<T> mAdapter;

    //<editor-fold desc="初始化">
    @NonNull
    protected ListPagerHelper<T> newListPagerHelper() {
        return new AfListPagerHelper<>(this);
    }

    /**
     * 自定义 View onCreateView(LayoutInflater, ViewGroup)
     */
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    /**
     * 初始化页面
     */
    @BindViewCreated
    protected void onViewCreated() throws Exception {
        mListHelper.onViewCreated();
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
    public abstract ItemsViewer findItemsViewer(ListPager<T> pager);


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
        mListHelper.onItemClick(parent, view, index, id);
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
        return mListHelper.onItemLongClick(parent, view, index, id);
    }
    //</editor-fold>

    //<editor-fold desc="子类重写">
    /**
     * 获取setContentView的id
     *
     * @return id
     */
    protected int getLayoutId() {
        return mListHelper.getLayoutId();
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
        return mAdapter = mListHelper.newAdapter(context, list);
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
        mListHelper.bindAdapter(listView, adapter);
    }

    @Override
    public void onTaskLoaded(AfHandlerTask task, List<T> list) {
        mListHelper.onTaskLoaded(task, list);
    }

    @Override
    public List<T> onTaskLoadList() throws Exception {
        return mListHelper.onTaskLoadList();
    }
    //</editor-fold>

}
