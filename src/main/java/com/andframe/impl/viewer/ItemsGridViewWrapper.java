package com.andframe.impl.viewer;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.andframe.api.view.ItemsViewer;

/**
 * GridView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsGridViewWrapper implements ItemsViewer<GridView> {

    protected GridView mItemsView;

    public ItemsGridViewWrapper(GridView gridView) {
        this.mItemsView = gridView;
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
        return false;
    }

    @Override
    public boolean addFooterView(View view) {
        return false;
    }

    @Override
    public GridView getItemsView() {
        return mItemsView;
    }

}
