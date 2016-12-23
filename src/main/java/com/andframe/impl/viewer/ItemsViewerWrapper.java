package com.andframe.impl.viewer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.api.multistatus.OnScrollToBottomListener;
import com.andframe.api.view.ItemsViewer;
import com.andframe.api.view.Viewer;

import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 对 多项控件（listview gridview recyclerview 或其他）  的适配实现
 * Created by SCWANG on 2016/12/23.
 */

public class ItemsViewerWrapper implements ItemsViewer<ViewGroup> {

    protected ItemsViewer<? extends ViewGroup> itemsViewer;

    public ItemsViewerWrapper(Viewer viewer) {
        this(defaultItemsViewer(searchItemsView(viewer)));
    }

    public ItemsViewerWrapper(View itemView) {
        this(defaultItemsViewer(itemView));
    }

    public ItemsViewerWrapper(ItemsViewer<? extends ViewGroup> itemsViewer) {
        this.itemsViewer = itemsViewer;
    }

    public static View searchItemsView(Viewer viewer) {
        View itemView = null,view;

        Queue<View> views = new LinkedBlockingQueue<>(Collections.singletonList(viewer.getView()));
        while (!views.isEmpty() && itemView == null) {
            view = views.poll();
            if (view != null) {
                if (isWrappeder(view)) {
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

    public static boolean isWrappeder(View view) {
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
    public ViewGroup getItemsView() {
        return itemsViewer.getItemsView();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        itemsViewer.setAdapter(adapter);
    }

    @Override
    public boolean addHeaderView(View view) {
        return itemsViewer.addHeaderView(view);
    }

    @Override
    public boolean addFooterView(View view) {
        return itemsViewer.addFooterView(view);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        itemsViewer.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        itemsViewer.setOnItemLongClickListener(listener);
    }

    @Override
    public void setOnScrollToBottomListener(OnScrollToBottomListener listener) {
        itemsViewer.setOnScrollToBottomListener(listener);
    }

    public boolean isWrapped() {
        return itemsViewer != null;
    }
}
