package com.andframe.widget.select;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import com.andframe.api.pager.items.OnScrollToBottomListener;
import com.andframe.api.viewer.ItemsViewer;
import com.andframe.impl.viewer.ItemsViewerWrapper;
import com.andframe.module.SelectorBottomBarModule;
import com.andframe.module.SelectorTitleBarModule;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SelectItemsViewer<T extends ViewGroup> extends ItemsViewerWrapper<T> implements
        OnItemLongClickListener, OnItemClickListener, ItemsViewer<T> {

    protected OnItemClickListener mItemClickListener = null;
    protected OnItemLongClickListener mItemLongClickListener = null;
    protected SelectListItemAdapter<?> mAdapter = null;
    protected SelectorTitleBarModule mSelectorTitleBar = null;
    protected SelectorBottomBarModule mSelectorBottomBar = null;

    public SelectItemsViewer(ItemsViewer<T> itemsViewer) {
        super(itemsViewer);
        this.mItemsViewer.setOnItemClickListener(this);
        this.mItemsViewer.setOnItemLongClickListener(this);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }

    @Override
    public void setOnScrollToBottomListener(OnScrollToBottomListener listener) {

    }

    @Override
    public int getFirstVisiblePosition() {
        return mItemsViewer.getFirstVisiblePosition();
    }

    @Override
    public int getLastVisiblePosition() {
        return mItemsViewer.getLastVisiblePosition();
    }

    /**
     * Deprecated. Use {@link #setAdapter(SelectListItemAdapter adapter)} from
     * now on.
     * @deprecated
     */
    @Deprecated
    public void setAdapter(ListAdapter adapter) {
        mItemsViewer.setAdapter(adapter);
        bindAdapter(adapter);
    }

    public void setAdapter(SelectListItemAdapter<?> adapter) {
        mItemsViewer.setAdapter(adapter);
        bindAdapter(adapter);
    }

    public void bindAdapter(ListAdapter adapter) {
        while (adapter instanceof WrapperListAdapter && !(adapter instanceof SelectItemsViewer)) {
            adapter = ((WrapperListAdapter) adapter).getWrappedAdapter();
        }
        if (adapter instanceof SelectListItemAdapter) {
            mAdapter = (SelectListItemAdapter<?>) adapter;
            if (mSelectorTitleBar != null) {
                mSelectorTitleBar.setAdapter(mAdapter);
            }
            if (mSelectorBottomBar != null) {
                mSelectorBottomBar.setAdapter(mAdapter);
            }
        }
    }

    public void setSelector(SelectorTitleBarModule selector) {
        this.mSelectorTitleBar = selector;
        if (mAdapter != null && selector != null) {
            mSelectorTitleBar.setAdapter(mAdapter);
        }
    }

    public void setSelector(SelectorBottomBarModule selector) {
        this.mSelectorBottomBar = selector;
        if (mAdapter != null && selector != null) {
            mSelectorBottomBar.setAdapter(mAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adview, View view, int index, long id) {
        if (mAdapter != null && mAdapter.isSelectMode()) {
            if (getItemsView() instanceof ListView) {
                index = index - ((ListView) getItemsView()).getHeaderViewsCount();
                if (index < 0) {
                    return;
                }
            }
            mAdapter.onItemClick(index);
        } else if (mItemClickListener != null) {
            mItemClickListener.onItemClick(adview, view, index, id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adview, View view, int index,long id) {
        if (mAdapter != null && !mAdapter.isSelectMode()) {
            if (getItemsView() instanceof ListView) {
                index = index - ((ListView) getItemsView()).getHeaderViewsCount();
                if (index < 0) {
                    return false;
                }
            }
            return mAdapter.beginSelectMode(index);
        } else if (mItemLongClickListener != null) {
            return mItemLongClickListener.onItemLongClick(adview, view, index, id);
        }
        return false;
    }

    public boolean isMultiChoiceMode() {
        return mAdapter != null && mAdapter.isSelectMode();
    }

    public boolean beginMultiChoice() {
        return mAdapter != null && mAdapter.beginSelectMode();
    }

}
