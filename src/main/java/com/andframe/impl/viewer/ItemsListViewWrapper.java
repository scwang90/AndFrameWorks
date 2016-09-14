package com.andframe.impl.viewer;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.api.view.ItemsViewer;

/**
 * ListView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsListViewWrapper implements ItemsViewer<ListView> {

    protected ListView mListView;

    public ItemsListViewWrapper(ListView mListView) {
        this.mListView = mListView;
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mListView.setOnItemLongClickListener(listener);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    @Override
    public ListView getItemsView() {
        return mListView;
    }
}
