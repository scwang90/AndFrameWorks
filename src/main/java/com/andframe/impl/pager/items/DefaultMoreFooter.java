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
    protected void onUpdateStatus(boolean isLoading, boolean allLoadFinish) {
        if (mIsLoading) {
            $(mProgressBar).visible().with(mTextView).visible().text("正在加载...");
        } else if (mEnabled) {
            $(mProgressBar).gone().with(mTextView).visible().text("点击查看更多");
        } else {
            $(mProgressBar, mTextView).gone();
        }
    }
}
