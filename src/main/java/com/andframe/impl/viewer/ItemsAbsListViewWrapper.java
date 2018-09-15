package com.andframe.impl.viewer;

import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.api.pager.items.OnScrollToBottomListener;
import com.andframe.api.viewer.ItemsViewer;

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

                long lastTime = 0;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if ((mItemsView != null && mItemsView.getAdapter() != null)
                            && mItemsView.getLastVisiblePosition() == (mItemsView.getAdapter().getCount() - 1)) {
                        long time = System.currentTimeMillis();
                        if (time - lastTime > 1000) {
                            listener.onScrollToBottom();
                            lastTime = time;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void setDrawEndDivider(boolean draw) {

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
    public void setDivisionEnable(boolean enable) {

    }

    @Override
    public void setNestedScrollingEnabled(boolean enable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mItemsView.setNestedScrollingEnabled(enable);
        }
    }

    @Override
    public int getFirstVisiblePosition() {
        return mItemsView.getFirstVisiblePosition();
    }

    @Override
    public void setSelection(int index) {
        mItemsView.setSelection(index);
    }

    @Override
    public int getLastVisiblePosition() {
        return mItemsView.getLastVisiblePosition();
    }

    @Override
    public T getItemsView() {
        return mItemsView;
    }

}
