package com.andframe.impl.viewer;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.andframe.api.view.ItemsViewer;

/**
 * RecyclerView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsRecyclerViewWrapper implements ItemsViewer<RecyclerView> {

    protected RecyclerView mRecyclerView;

    public ItemsRecyclerViewWrapper(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
//        mRecyclerView.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
//        mRecyclerView.setOnItemLongClickListener(listener);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof RecyclerView.Adapter) {
            mRecyclerView.setAdapter(((RecyclerView.Adapter) adapter));
        }
    }

    @Override
    public RecyclerView getItemsView() {
        return mRecyclerView;
    }
}
