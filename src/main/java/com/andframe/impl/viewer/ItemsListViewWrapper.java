package com.andframe.impl.viewer;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.api.view.ItemsViewer;
import com.andframe.feature.AfView;

/**
 * ListView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsListViewWrapper extends AfView implements ItemsViewer<ListView> {

    protected ListView mItemsView;

    public ItemsListViewWrapper(ListView listView) {
        super(listView);
        this.mItemsView = listView;
    }

    @Override
    public void smoothScrollToPosition(int index) {
        mItemsView.smoothScrollToPosition(index);
    }

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
    public ListView getItemsView() {
        return mItemsView;
    }

}
