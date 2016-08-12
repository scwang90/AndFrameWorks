package com.andframe.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.andframe.activity.framework.AfActivity;
import com.andframe.activity.framework.AfPageable;
import com.andframe.adapter.AfListAdapter;
import com.andframe.annotation.view.BindAfterViews;
import com.andframe.annotation.view.BindLayout;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfIntent;
import com.andframe.fragment.AfListFragment;

import java.util.ArrayList;
import java.util.List;

import static com.andframe.util.java.AfReflecter.getAnnotation;

/**
 * 数据列表框架 Activity
 * @param <T> 列表数据实体类
 * @author 树朾
 */
public abstract class AfListActivity<T> extends AfActivity implements OnItemClickListener, OnItemLongClickListener {

    protected AbsListView mListView;
    protected AfListAdapter<T> mAdapter;

    /**
     * 创建方法
     *
     * @param bundle 源Bundle
     * @param intent 框架AfIntent
     */
    @Override
    protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
        super.onCreate(bundle, intent);
        if (mRootView == null) {
            setContentView(getLayoutId());
        }
    }

    /**
     * 初始化页面
     */
    @BindAfterViews
    protected void onInitFrameWork() throws Exception {
        if (mAdapter == null) {
            mAdapter = newAdapter(this, new ArrayList<T>());
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
    protected void bindAdapter(AbsListView listView, AfListAdapter<T> adapter) {
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
        BindLayout layout = getAnnotation(this.getClass(), AfListActivity.class, BindLayout.class);
        if (layout != null) {
            return layout.value();
        }
        return 0;
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
    protected abstract AfListAdapter.IListItem<T> getListItem(T data);

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
            return AfListActivity.this.getListItem(data);
        }
    }

}
