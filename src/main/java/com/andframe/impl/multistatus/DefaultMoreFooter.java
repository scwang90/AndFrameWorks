package com.andframe.impl.multistatus;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.adapter.listitem.AfListItem;
import com.andframe.annotation.view.BindView;
import com.andframe.api.multistatus.MoreFooter;
import com.andframe.api.multistatus.OnMoreListener;
import com.andframe.listener.SafeOnClickListener;


/**
 * 默认更多加载
 * Created by SCWANG on 2016/10/21.
 */

public class DefaultMoreFooter<T> extends AfListItem<T> implements MoreFooter<T> {

    private OnMoreListener listener;

    @BindView
    protected TextView mTextView;
    @BindView
    protected ProgressBar mProgressBar;
    protected boolean mAllLoadFinish = false;
    protected boolean mIsLoading = false;

    public DefaultMoreFooter() {
        super(R.layout.af_refresh_list_footer);
    }

    @Override
    public void setOnMoreListener(OnMoreListener listener) {
        this.listener = listener;
    }

    @Override
    public void finishLoadMore() {
        mIsLoading = false;
        onBinding(null, 0);
    }

    @Override
    public void setAllLoadFinish(boolean finish) {
        mAllLoadFinish = finish;
        mIsLoading = false;
        onBinding(null, 0);
    }

    protected void setLoading() {
        mIsLoading = true;
        mAllLoadFinish = false;
        onBinding(null, 0);
    }

    @Override
    protected void onHandle(View view) {
        super.onHandle(view);
        finishLoadMore();
        view.setOnClickListener(new SafeOnClickListener(v -> {
            if (!mAllLoadFinish && listener != null && listener.onMore()) {
                setLoading();
            }
        }));
    }

    @Override
    public void onBinding(T model, int index) {
        if (mIsLoading) {
            $(mProgressBar).visible().$(mTextView).text("正在加载...");
        } else if (mAllLoadFinish) {
            $(mProgressBar, mTextView).gone();
        } else {
            $(mProgressBar).gone().$(mTextView).text("点击查看更多");
        }
    }
}
