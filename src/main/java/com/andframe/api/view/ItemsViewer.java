package com.andframe.api.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.api.multistatus.OnScrollToBottomListener;


/**
 * 对 多项控件（listview gridview recyclerview 或其他）  的抽象接口
 * @param <T> 多项控件 的类
 * Created by SCWANG on 2016/9/14.
 */
public interface ItemsViewer<T extends ViewGroup> {
//    void smoothScrollToPosition(int index);

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
}
