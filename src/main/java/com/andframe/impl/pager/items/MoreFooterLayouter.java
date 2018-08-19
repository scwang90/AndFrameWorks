package com.andframe.impl.pager.items;

import com.andframe.api.adapter.HeaderFooterAdapter;
import com.andframe.api.pager.items.MoreFooter;
import com.andframe.api.pager.items.MoreLayouter;
import com.andframe.api.pager.items.OnMoreListener;
import com.andframe.api.viewer.ItemsViewer;

/**
 * 通过 MoreFooter 实现的 加载更多 Layouter
 * Created by SCWANG on 2016/11/19.
 */

public class MoreFooterLayouter<T> implements MoreLayouter {

    private MoreFooter mMoreFooter;
    private HeaderFooterAdapter<T> mAdapter;

    public MoreFooterLayouter(MoreFooter footer, HeaderFooterAdapter<T> adapter, ItemsViewer itemsViewer) {
        this.mMoreFooter = footer;
        this.mAdapter = adapter;
        this.mMoreFooter.onCreateView(itemsViewer.getItemsView().getContext(), null);
        itemsViewer.setOnScrollToBottomListener(mMoreFooter::triggerLoadMore);
    }

    @Override
    public void setOnMoreListener(OnMoreListener listener) {
        mMoreFooter.setOnMoreListener(listener);
    }
    @Override
    public void setLoadMoreEnabled(boolean enable) {
        mMoreFooter.setLoadMoreEnabled(enable);
        boolean hasFooter = mAdapter.hasFooterView(mMoreFooter.getView());
        if (hasFooter && !enable) {
            mAdapter.removeFooterView(mMoreFooter.getView());
        } else if (!hasFooter && enable) {
            mAdapter.addFooter(new MoreFooterHolder<>(mMoreFooter));
        }
    }
    @Override
    public void finishLoadMore() {
        mMoreFooter.finishLoadMore();
    }
}
