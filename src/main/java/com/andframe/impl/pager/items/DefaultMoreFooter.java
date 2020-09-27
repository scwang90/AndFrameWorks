package com.andframe.impl.pager.items;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.annotation.view.BindView;
import com.andframe.api.pager.items.MoreFooter;


/**
 * 默认更多加载
 * Created by SCWANG on 2016/10/21.
 */

public class DefaultMoreFooter extends BaseMoreFooter implements MoreFooter {

    @BindView
    protected TextView mTextView;
    @BindView
    protected ProgressBar mProgressBar;

    public DefaultMoreFooter() {
        super(R.layout.af_refresh_list_footer);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        $().clicked(v -> triggerLoadMore());
    }

    @Override
    protected void onUpdateStatus(boolean isLoading, boolean enable, boolean noMoreData) {
        if (!enable) {
            $(mProgressBar, mTextView).gone();
        } else if (isLoading) {
            $(mProgressBar).visible().with(mTextView).visible().text(R.string.refresh_footer_loading);
        } else if (noMoreData) {
            $(mProgressBar).gone().with(mTextView).visible().text(R.string.items_loading_all);
        } else {
            $(mProgressBar).gone().with(mTextView).visible().text(R.string.refresh_footer_more);
        }
    }
}
