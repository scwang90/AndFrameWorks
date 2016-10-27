package com.andframe.impl.multistatus;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.annotation.view.BindView;
import com.andframe.api.multistatus.MoreFooter;


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
            $(mProgressBar).visible().$(mTextView).visible().text("正在加载...");
        } else if (mAllLoadFinish) {
            $(mProgressBar, mTextView).gone();
        } else {
            $(mProgressBar).gone().$(mTextView).visible().text("点击查看更多");
        }
    }
}
