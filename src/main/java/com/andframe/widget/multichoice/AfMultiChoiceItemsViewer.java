package com.andframe.widget.multichoice;

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
import com.andframe.module.AfSelectorBottombar;
import com.andframe.module.AfSelectorTitlebar;

@SuppressWarnings("unused")
public class AfMultiChoiceItemsViewer<T extends ViewGroup> implements
        OnItemLongClickListener, OnItemClickListener, ItemsViewer<T> {

    protected OnItemClickListener mItemClickListener = null;
    protected OnItemLongClickListener mItemLongClickListener = null;
    protected AfMultiChoiceAdapter<?> mAdapter = null;
    protected AfSelectorTitlebar mSelectorTitlebar = null;
    protected AfSelectorBottombar mSelectorBottombar = null;
    protected ItemsViewer<T> mItemsViewer;

    public AfMultiChoiceItemsViewer(ItemsViewer<T> itemsViewer) {
        this.mItemsViewer = itemsViewer;
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
    public T getItemsView() {
        return mItemsViewer.getItemsView();
    }

    @Override
    public boolean addHeaderView(View view) {
        return mItemsViewer.addHeaderView(view);
    }

    @Override
    public boolean addFooterView(View view) {
        return mItemsViewer.addHeaderView(view);
    }

    @Override
    public void setDivisionEnable(boolean enable) {
        mItemsViewer.setDivisionEnable(enable);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enable) {
        mItemsViewer.setNestedScrollingEnabled(enable);
    }

    /**
     * Deprecated. Use {@link #setAdapter(AfMultiChoiceAdapter adapter)} from
     * now on.
     * @deprecated
     */
    @Deprecated
    public void setAdapter(ListAdapter adapter) {
        mItemsViewer.setAdapter(adapter);
        bindAdapter(adapter);
    }

    public void setAdapter(AfMultiChoiceAdapter<?> adapter) {
        mItemsViewer.setAdapter(adapter);
        bindAdapter(adapter);
    }

    public void bindAdapter(ListAdapter adapter) {
        while (adapter instanceof WrapperListAdapter && !(adapter instanceof AfMultiChoiceItemsViewer)) {
            adapter = ((WrapperListAdapter) adapter).getWrappedAdapter();
        }
        if (adapter instanceof AfMultiChoiceAdapter) {
            mAdapter = (AfMultiChoiceAdapter<?>) adapter;
            if (mSelectorTitlebar != null) {
                mSelectorTitlebar.setAdapter(mAdapter);
            }
            if (mSelectorBottombar != null) {
                mSelectorBottombar.setAdapter(mAdapter);
            }
        }
    }

    public void setSelector(AfSelectorTitlebar selector) {
        this.mSelectorTitlebar = selector;
        if (mAdapter != null && selector != null) {
            mSelectorTitlebar.setAdapter(mAdapter);
        }
    }

    public void setSelector(AfSelectorBottombar selector) {
        this.mSelectorBottombar = selector;
        if (mAdapter != null && selector != null) {
            mSelectorBottombar.setAdapter(mAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adview, View view, int index, long id) {
        if (mAdapter != null && mAdapter.isMultiChoiceMode()) {
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
        if (mAdapter != null && !mAdapter.isMultiChoiceMode()) {
            if (getItemsView() instanceof ListView) {
                index = index - ((ListView) getItemsView()).getHeaderViewsCount();
                if (index < 0) {
                    return false;
                }
            }
            mAdapter.beginMultiChoice(index);
            return true;
        } else if (mItemLongClickListener != null) {
            return mItemLongClickListener.onItemLongClick(adview, view, index, id);
        }
        return false;
    }

    public boolean isMultiChoiceMode() {
        return mAdapter != null && mAdapter.isMultiChoiceMode();
    }

    public boolean beginMultiChoice() {
        return mAdapter != null && mAdapter.beginMultiChoice();
    }

}
