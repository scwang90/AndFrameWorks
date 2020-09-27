package com.andframe.impl.pager.items;

import android.content.DialogInterface.OnCancelListener;
import android.view.View;

import com.andframe.adapter.item.ListItemViewer;
import com.andframe.api.pager.items.MoreFooter;
import com.andframe.api.pager.items.OnMoreListener;
import com.andframe.listener.SafeListener;


/**
 * 默认更多加载
 * Created by SCWANG on 2016/10/21.
 */

public abstract class BaseMoreFooter extends ListItemViewer<Object> implements MoreFooter {

    protected OnMoreListener listener;
    protected boolean mEnabled = false;
    protected boolean mIsLoading = false;
    protected boolean mNoMoreData = false;

    @SuppressWarnings("unused")
    public BaseMoreFooter() {
    }

    public BaseMoreFooter(int layoutId) {
        super(layoutId);
    }

    @Override
    public void setOnMoreListener(OnMoreListener listener) {
        this.listener = listener;
    }

    @Override
    public void finishLoadMore() {
        onUpdateStatus(mIsLoading = false, mEnabled, mNoMoreData);
    }

    @Override
    public void setLoadMoreEnabled(boolean enabled) {
        onUpdateStatus(mIsLoading = false, mEnabled = enabled, mNoMoreData);
    }

    @Override
    public void setNoMoreData(boolean noMoreData) {
        onUpdateStatus(mIsLoading = false, mEnabled, mNoMoreData = noMoreData);
    }

    protected void setLoading() {
        onUpdateStatus(mIsLoading = true, mEnabled = true, false);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mLayout.setOnClickListener(new SafeListener((OnCancelListener) v -> triggerLoadMore()));
    }

    @Override
    public void triggerLoadMore() {
        if (mEnabled && !mIsLoading && listener != null && !mNoMoreData && listener.onMore()) {
            setLoading();
        }
    }

    //<editor-fold desc="终止接口">
    @Override
    public final void onUpdateStatus(View view, int index) {
        onUpdateStatus(mIsLoading, mEnabled, mNoMoreData);
    }

    @Override
    public final void onBinding(Object model, int index) {

    }

    @Override
    public final void onBinding(View view, Object model, int index) {
        super.onBinding(view, model, index);
    }
    //</editor-fold>

    /**
     * 更新控件状态
     * @param isLoading     是否正在加载
     * @param enable        是否所有数据都加载完
     */
    protected abstract void onUpdateStatus(boolean isLoading, boolean enable, boolean noMoreData);


}
