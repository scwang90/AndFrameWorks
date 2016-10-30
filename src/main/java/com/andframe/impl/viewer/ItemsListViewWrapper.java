package com.andframe.impl.viewer;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.api.view.ItemsViewer;

/**
 * ListView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsListViewWrapper implements ItemsViewer<ListView> {

    protected ListView mItemsView;

    public ItemsListViewWrapper(ListView listView) {
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
    public void setAdapter(ListAdapter adapter) {
        mItemsView.setAdapter(adapter);
    }

    @Override
    public boolean addHeaderView(View view) {
        mItemsView.addHeaderView(view);
        return true;
    }

    @Override
    public boolean addFooterView(View view) {
        mItemsView.addFooterView(view);
        return true;
    }

    @Override
    public ListView getItemsView() {
        return mItemsView;
    }

}
