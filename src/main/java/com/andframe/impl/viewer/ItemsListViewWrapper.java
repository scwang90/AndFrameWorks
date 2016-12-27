package com.andframe.impl.viewer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * ListView
 * Created by SCWANG on 2016/9/14.
 */
public class ItemsListViewWrapper extends ItemsAbsListViewWrapper<ListView> {

    private int mDividerHeight;

    public ItemsListViewWrapper(ListView listView) {
        super(listView);
        mDividerHeight = listView.getDividerHeight();
    }

    @Override
    public boolean addHeaderView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && !(params instanceof ListView.LayoutParams)) {
            view.setLayoutParams(new ListView.LayoutParams(params));
        }
        mItemsView.addHeaderView(view);
        return true;
    }

    @Override
    public boolean addFooterView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && !(params instanceof ListView.LayoutParams)) {
            view.setLayoutParams(new ListView.LayoutParams(params));
        }
        mItemsView.addFooterView(view);
        return true;
    }

    @Override
    public void setDivisionEnable(boolean enable) {
        if (!enable) {
            mItemsView.setDividerHeight(0);
        } else {
            mItemsView.setDividerHeight(mDividerHeight);
        }
    }
}
