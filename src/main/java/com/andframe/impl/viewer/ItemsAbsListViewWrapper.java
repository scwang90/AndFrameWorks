package com.andframe.impl.viewer;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.api.multistatus.OnScrollToBottomListener;
import com.andframe.api.view.ItemsViewer;

/**
 * ListView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsAbsListViewWrapper<T extends AbsListView> implements ItemsViewer<T> {

    protected T mItemsView;

    public ItemsAbsListViewWrapper(T listView) {
        this.mItemsView = listView;
    }

//    @Override
//    public void smoothScrollToPosition(int index) {
//        mItemsView.smoothScrollToPosition(index);
//    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mItemsView.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mItemsView.setOnItemLongClickListener(listener);
    }

    @Override
    public void setOnScrollToBottomListener(OnScrollToBottomListener listener) {
        if (listener == null) {
            mItemsView.setOnScrollListener(null);
        } else {
            mItemsView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if ((mItemsView != null && mItemsView.getAdapter() != null)
                            && mItemsView.getLastVisiblePosition() == (mItemsView.getAdapter().getCount() - 1)) {
                        listener.onScrollToBottom();
                    }
                }
            });
        }
    }
    
    @Override
    public void setAdapter(ListAdapter adapter) {
        mItemsView.setAdapter(adapter);
    }

    @Override
    public boolean addHeaderView(View view) {
        return false;
    }

    @Override
    public boolean addFooterView(View view) {
        return false;
    }

    @Override
    public T getItemsView() {
        return mItemsView;
    }

}
