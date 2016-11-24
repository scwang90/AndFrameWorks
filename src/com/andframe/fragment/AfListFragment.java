package com.andframe.fragment;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.activity.framework.AfPageable;
import com.andframe.adapter.AfListAdapter;
import com.andframe.adapter.AfListAdapter.IListItem;
import com.andframe.annotation.interpreter.LayoutBinder;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.application.AfExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据列表框架 AfListFragment
 * @param <T> 列表数据实体类
 * @author 树朾
 */
public abstract class AfListFragment<T> extends AfTabFragment implements OnItemClickListener, OnItemLongClickListener {

    protected AbsListView mListView;
    protected AfListAdapter<T> mAdapter;

    /**
     * 自定义 View onCreateView(LayoutInflater, ViewGroup)
     */
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @BindAfterViews
    protected void onInitFrameWork() throws Exception {
        if (mAdapter == null) {
            mAdapter = newAdapter(getContext(), new ArrayList<T>());
        }
        mListView = findListView(this);
        if (mListView != null) {
            mListView.setOnItemClickListener(this);
            mListView.setOnItemLongClickListener(this);
            bindAdapter(mListView, mAdapter);
        }
    }

    /**
     * 绑定适配器
     * @param listView 列表
     * @param adapter 适配器
     */
    @SuppressWarnings("RedundantCast")
    protected void bindAdapter(AbsListView listView, ListAdapter adapter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            listView.setAdapter(adapter);
        } else if (listView instanceof ListView) {
            ((ListView) listView).setAdapter(adapter);
        } else if (listView instanceof GridView) {
            ((GridView) listView).setAdapter(adapter);
        }
    }

    /**
     * 获取setContentView的id
     *
     * @return id
     */
    protected int getLayoutId() {
        return LayoutBinder.getBindLayoutId(this, getContext(), AfListFragment.class);
    }

    /**
     *
     * 获取列表控件
     *
     * @param pageable 页面对象
     * @return pageable.findListViewById(id)
     */
    protected abstract AbsListView findListView(AfPageable pageable);

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
        if (mListView instanceof ListView) {
            index -= ((ListView) mListView).getHeaderViewsCount();
        }
        if (index >= 0) {
            T model = mAdapter.getItemAt(index);
            try {
                onItemClick(model, index);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG("onItemClick"));
            }
        }
    }

    /**
     * onItemClick 事件的 包装 一般情况下子类可以重写这个方法
     *
     * @param model 被点击的数据model
     * @param index 被点击的index
     */
    @SuppressWarnings("UnusedParameters")
    protected void onItemClick(T model, int index) {

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
        if (mListView instanceof ListView) {
            index -= ((ListView) mListView).getHeaderViewsCount();
        }
        if (index >= 0) {
            T model = mAdapter.getItemAt(index);
            try {
                return onItemLongClick(model, index);
            } catch (Throwable e) {
                AfExceptionHandler.handle(e, TAG("onItemLongClick"));
            }
        }
        return false;
    }

    /**
     * onItemLongClick 事件的 包装 一般情况下子类可以重写这个方法
     *
     * @param model 被点击的数据model
     * @param index 被点击的index
     */
    @SuppressWarnings("UnusedParameters")
    protected boolean onItemLongClick(T model, int index) {
        return false;
    }

    /**
     * 获取列表项布局Item
     * 如果重写 newAdapter 之后，本方法将无效
     *
     * @param data 对应的数据
     * @return 实现 布局接口 IListItem 的Item兑现
     * new LayoutItem implements IListItem<T>(){}
     */
    protected abstract IListItem<T> getListItem(T data);

    /**
     * 根据数据ltdata新建一个 适配器 重写这个方法之后getItemLayout方法将失效
     *
     * @param context Context对象
     * @param ltdata  完成加载数据
     * @return 新的适配器
     */
    protected AfListAdapter<T> newAdapter(Context context, List<T> ltdata) {
        return new AbListAdapter(context, ltdata);
    }

    /**
     * ListView数据适配器（事件已经转发getItemLayout，无实际处理代码）
     */
    protected class AbListAdapter extends AfListAdapter<T> {

        public AbListAdapter(Context context, List<T> ltdata) {
            super(context, ltdata);
        }

        /**
         * 转发事件到 AfListViewActivity.this.getItemLayout(data);
         */
        @Override
        protected IListItem<T> getListItem(T data) {
            return AfListFragment.this.getListItem(data);
        }
    }
}
