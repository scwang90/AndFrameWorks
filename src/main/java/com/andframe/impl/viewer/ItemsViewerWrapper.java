package com.andframe.impl.viewer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.api.pager.items.OnScrollToBottomListener;
import com.andframe.api.viewer.ItemsViewer;
import com.andframe.api.viewer.Viewer;

import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 对 多项控件（ListView GridView RecyclerView 或其他）  的适配实现
 * Created by SCWANG on 2016/12/23.
 */

@SuppressWarnings("WeakerAccess")
public class ItemsViewerWrapper<T extends ViewGroup> implements ItemsViewer<T> {

    protected ItemsViewer<T> mItemsViewer;

    public ItemsViewerWrapper(Viewer viewer) {
        //noinspection unchecked
        this((ItemsViewer<T>)defaultItemsViewer(searchItemsView(viewer)));
    }

    public ItemsViewerWrapper(View itemView) {
        //noinspection unchecked
        this((ItemsViewer<T>)defaultItemsViewer(itemView));
    }

    public ItemsViewerWrapper(ItemsViewer<T> mItemsViewer) {
        this.mItemsViewer = mItemsViewer;
    }

    public static View searchItemsView(Viewer viewer) {
        View itemView = null,view;

        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(viewer.getView()));
        while (!views.isEmpty() && itemView == null) {
            view = views.poll();
            if (view != null) {
                if (isWrapped(view)) {
                    itemView = view;
                } else if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int j = 0; j < group.getChildCount(); j++) {
                        views.add(group.getChildAt(j));
                    }
                }
            }
        }
        return itemView;
    }

    public static boolean isWrapped(View view) {
        return view instanceof AbsListView || view instanceof RecyclerView;
    }

    public static ItemsViewer<? extends ViewGroup> defaultItemsViewer(View itemView) {
        if (itemView instanceof ListView) {
            return new ItemsListViewWrapper((ListView) itemView);
        } else if (itemView instanceof GridView) {
            return new ItemsGridViewWrapper((GridView) itemView);
        } else if (itemView instanceof RecyclerView) {
            return new ItemsRecyclerViewWrapper((RecyclerView) itemView);
        } else if (itemView instanceof AbsListView) {
            return new ItemsAbsListViewWrapper<>((AbsListView) itemView);
        }
        return null;
    }

    @Override
    public int getLastVisiblePosition() {
        return mItemsViewer.getLastVisiblePosition();
    }

    @Override
    public int getFirstVisiblePosition() {
        return mItemsViewer.getFirstVisiblePosition();
    }

    @Override
    public void setSelection(int index) {
        mItemsViewer.setSelection(index);
    }


    @Override
    public T getItemsView() {
        //noinspection unchecked
        return mItemsViewer.getItemsView();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        mItemsViewer.setAdapter(adapter);
    }

    @Override
    public boolean addHeaderView(View view) {
        return mItemsViewer.addHeaderView(view);
    }

    @Override
    public boolean addFooterView(View view) {
        return mItemsViewer.addFooterView(view);
    }

    @Override
    public void setDivisionEnable(boolean enable) {
        mItemsViewer.setDivisionEnable(enable);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enable) {
        mItemsViewer.setNestedScrollingEnabled(enable);
    }
    @Override
    public void setDrawEndDivider(boolean draw) {
        mItemsViewer.setDrawEndDivider(draw);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mItemsViewer.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mItemsViewer.setOnItemLongClickListener(listener);
    }

    @Override
    public void setOnScrollToBottomListener(OnScrollToBottomListener listener) {
        mItemsViewer.setOnScrollToBottomListener(listener);
    }

    public boolean isWrapped() {
        return mItemsViewer != null;
    }
}
