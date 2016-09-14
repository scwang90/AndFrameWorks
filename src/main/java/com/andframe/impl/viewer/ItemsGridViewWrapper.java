package com.andframe.impl.viewer;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.GridView;

import com.andframe.api.view.ItemsViewer;

/**
 * GridView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsGridViewWrapper implements ItemsViewer<GridView> {

    protected GridView mGridView;

    public ItemsGridViewWrapper(GridView mGridView) {
        this.mGridView = mGridView;
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mGridView.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mGridView.setOnItemLongClickListener(listener);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        mGridView.setAdapter(adapter);
    }

    @Override
    public GridView getItemsView() {
        return mGridView;
    }
}
