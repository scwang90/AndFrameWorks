package com.andframe.impl.viewer;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.api.view.ItemsViewer;
import com.andframe.feature.AfView;

/**
 * RecyclerView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsRecyclerViewWrapper extends AfView implements ItemsViewer<RecyclerView> {

    protected RecyclerView mItemsView;

    public ItemsRecyclerViewWrapper(RecyclerView recyclerView) {
        super(recyclerView);
        this.mItemsView = recyclerView;
    }

    @Override
    public void smoothScrollToPosition(int index) {
        mItemsView.smoothScrollToPosition(index);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
//        mItemsView.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
//        mItemsView.setOnItemLongClickListener(listener);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof RecyclerView.Adapter) {
            mItemsView.setAdapter(((RecyclerView.Adapter) adapter));
        }
    }

    @Override
    public RecyclerView getItemsView() {
        return mItemsView;
    }
}
