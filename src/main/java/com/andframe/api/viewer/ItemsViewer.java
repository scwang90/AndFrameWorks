package com.andframe.api.viewer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.api.pager.items.OnScrollToBottomListener;

/**
 * 对 多项控件（ListView GridView RecyclerView 或其他）  的抽象接口
 * @param <T> 多项控件 的类
 * Created by SCWANG on 2016/9/14.
 */
public interface ItemsViewer<T extends ViewGroup> {
//    void smoothScrollToPosition(int index);

    int getLastVisiblePosition();

    T getItemsView();

    void setAdapter(ListAdapter adapter);

    boolean addHeaderView(View view);
    boolean addFooterView(View view);
    void setOnItemClickListener(AdapterView.OnItemClickListener listener);
    void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener);

    /**
     * 设置滚动到底部的监听器，如果实现了这个方法，将可以实现滚动到底部自动加载更多功能
     */
    void setOnScrollToBottomListener(OnScrollToBottomListener listener);

    void setDrawEndDivider(boolean draw);

    /**
     * 设置分割线的显示
     * super.onViewCreated() 或者 setAdapter() 之前设置才有效
     */
    void setDivisionEnable(boolean enable);

    /**
     * 设置是否开启嵌套滚动
     */
    void setNestedScrollingEnabled(boolean enable);

    int getFirstVisiblePosition();

    void setSelection(int index);
}
