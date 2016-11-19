package com.andframe.impl.multistatus;

import com.andframe.adapter.AfHeaderFooterAdapter;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.multistatus.MoreLayouter;
import com.andframe.api.multistatus.OnMoreListener;
import com.andframe.api.view.ItemsViewer;

/**
 * 通过 MoreFooter 实现的 加载更多 Layouter
 * Created by SCWANG on 2016/11/19.
 */

public class MoreFooterLayouter<T> implements MoreLayouter{
    private MoreFooter mMoreFooter;
    private AfHeaderFooterAdapter<T> mAdapter;

    public MoreFooterLayouter(MoreFooter footer, AfHeaderFooterAdapter<T> adapter, ItemsViewer itemsViewer) {
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
        mAdapter.removeFooterView(mMoreFooter.getView());
        if (enable) {
            mAdapter.addFooter(new MoreFooterHolder<>(mMoreFooter));
        }
    }
    @Override
    public void finishLoadMore() {
        mMoreFooter.finishLoadMore();
    }
}
