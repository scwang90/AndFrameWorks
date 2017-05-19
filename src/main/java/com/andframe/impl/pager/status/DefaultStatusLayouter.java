package com.andframe.impl.pager.status;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.api.pager.status.OnRefreshListener;
import com.andframe.api.pager.status.StatusLayouter;
import com.andframe.module.AfFrameSelector;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 可切换状态页面的布局
 * Created by SCWANG on 2016/10/20.
 */

public class DefaultStatusLayouter implements StatusLayouter {

    protected final FrameLayout mFrameLayout;
    protected final AfFrameSelector mFrameSelector;
    protected View mContentView;
    protected View mEmptyLayout;
    protected View mErrorLayout;
    protected View mProgressLayout;
    protected View mInvalidnetLayout;
    protected TextView mErrorTextView;
    protected TextView mEmptyTextView;
    protected TextView mProgressTextView;
    protected TextView mInvalidnetTextView;
    protected OnRefreshListener mOnRefreshListener;
    protected View.OnClickListener mOnRefreshClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnRefreshListener != null && mOnRefreshListener.onRefresh()) {
                showProgress();
            }
        }
    };

//    private AfModuleNodata mModuleEmpty;
//    private AfModuleNodata mModuleError;
//    private AfModuleNodata mModuleProgress;

    public DefaultStatusLayouter(Context content) {
        this(new FrameLayout(content));
    }

    public DefaultStatusLayouter(FrameLayout frameLayout) {
        mFrameLayout = frameLayout;
        mFrameSelector = new AfFrameSelector(frameLayout);
        if (mFrameLayout.getChildCount() > 0) {
            mContentView = mFrameLayout.getChildAt(0);
        }
    }

    @Override
    public ViewGroup getLayout() {
        return mFrameLayout;
    }

    //<editor-fold desc="视图初始化">
    @Override
    public void setContenView(View content) {
        if (mContentView != null) {
            mFrameLayout.removeView(mContentView);
        }
        ViewGroup.LayoutParams params = content.getLayoutParams();
        int height = params == null ? MATCH_PARENT : params.height;
        mFrameLayout.addView(mContentView = content, MATCH_PARENT, height == 0 ? MATCH_PARENT : height);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    @Override
    public void setProgressLayout(int layoutId) {
        setProgressLayout(layoutId, 0);
    }

    @Override
    public void setProgressLayout(int layoutId,int msgId) {
        if (mProgressLayout != null) {
            mFrameLayout.removeView(mProgressLayout);
        }
        mProgressLayout = LayoutInflater.from(mFrameLayout.getContext()).inflate(layoutId, null);
        mProgressTextView = (TextView) mProgressLayout.findViewById(msgId);
        mFrameLayout.addView(mProgressLayout);
    }

    @Override
    public void setEmptyLayout(int layoutId) {
        setEmptyLayout(layoutId, 0, 0);
    }

    @Override
    public void setEmptyLayout(int layoutId, int msgId) {
        setEmptyLayout(layoutId, msgId, 0);
    }

    @Override
    public void setEmptyLayout(int layoutId, int msgId, int btnId) {
        setEmptyLayout(layoutId, msgId, btnId, "");
    }

    @Override
    public void setEmptyLayout(int layoutId, int msgId, int btnId, String message) {
        if (mEmptyLayout != null) {
            mFrameLayout.removeView(mEmptyLayout);
        }
        mEmptyLayout = LayoutInflater.from(mFrameLayout.getContext()).inflate(layoutId, null);
        mEmptyTextView = (TextView) mEmptyLayout.findViewById(msgId);
        if (mEmptyTextView != null && !TextUtils.isEmpty(message)) {
            mEmptyTextView.setText(message);
        }
        View btn = mEmptyLayout.findViewById(btnId);
        btn = btn == null ? mEmptyLayout : btn;
        btn.setOnClickListener(mOnRefreshClickListener);
        mFrameLayout.addView(mEmptyLayout);
    }

    @Override
    public void setErrorLayout(int layoutId, int msgId) {
        setErrorLayout(layoutId, msgId, 0);
    }

    @Override
    public void setErrorLayout(int layoutId, int msgId, int btnId) {
        if (mErrorLayout != null) {
            mFrameLayout.removeView(mErrorLayout);
        }
        mErrorLayout = LayoutInflater.from(mFrameLayout.getContext()).inflate(layoutId, null);
        mErrorTextView = (TextView) mErrorLayout.findViewById(msgId);
        View btn = mErrorLayout.findViewById(btnId);
        btn = btn == null ? mErrorLayout : btn;
        btn.setOnClickListener(mOnRefreshClickListener);
        mFrameLayout.addView(mErrorLayout);
    }

    @Override
    public void setInvalidnetLayout(int layoutId) {
        setInvalidnetLayout(layoutId, 0, 0);
    }

    @Override
    public void setInvalidnetLayout(int layoutId, int msgId) {
        setInvalidnetLayout(layoutId, msgId, 0);
    }

    @Override
    public void setInvalidnetLayout(int layoutId, int msgId, int btnId) {
        if (mInvalidnetLayout != null) {
            mFrameLayout.removeView(mInvalidnetLayout);
        }
        mInvalidnetLayout = LayoutInflater.from(mFrameLayout.getContext()).inflate(layoutId, null);
        mInvalidnetTextView = (TextView) mInvalidnetLayout.findViewById(msgId);
        View btn = mInvalidnetLayout.findViewById(btnId);
        btn = btn == null ? mInvalidnetLayout : btn;
        btn.setOnClickListener(mOnRefreshClickListener);
    }

    @Override
    public void autoCompletedLayout() {
        if (mEmptyLayout == null) {
            setEmptyLayout(R.layout.af_module_nodata, R.id.module_nodata_description);
        }
        if (mErrorLayout == null) {
            setErrorLayout(R.layout.af_module_nodata, R.id.module_nodata_description);
        }
        if (mProgressLayout == null) {
            setProgressLayout(R.layout.af_module_progress, R.id.module_progress_loadinfo);
        }
        if (mInvalidnetLayout == null) {
            setInvalidnetLayout(R.layout.af_module_nodata, R.id.module_nodata_description);
        }
    }
    //</editor-fold>

    //<editor-fold desc="视图切换">
    @Override
    public void showEmpty() {
        if (mEmptyLayout != null) {
            mFrameSelector.selectFrame(mEmptyLayout);
        }
    }

    @Override
    public void showEmpty(String empty) {
        if (mEmptyLayout != null) {
            mFrameSelector.selectFrame(mEmptyLayout);
            if (mEmptyTextView != null) {
                mEmptyTextView.setText(empty);
            }
        }
    }

    @Override
    public void showContent() {
        if (mContentView != null) {
            mFrameSelector.selectFrame(mContentView);
        }
    }

    @Override
    public void showProgress() {
        if (mProgressLayout != null) {
            mFrameSelector.selectFrame(mProgressLayout);
        }
    }

    @Override
    public void showProgress(String message) {
        if (mProgressLayout != null) {
            mFrameSelector.selectFrame(mProgressLayout);
            if (mProgressTextView != null) {
                mProgressTextView.setText(message);
            }
        }
    }

    @Override
    public void showInvalidnet() {
        if (mInvalidnetLayout != null) {
            mFrameSelector.selectFrame(mInvalidnetLayout);
        }
    }

    @Override
    public void showInvalidnet(String message) {
        if (mInvalidnetLayout != null) {
            mFrameSelector.selectFrame(mInvalidnetLayout);
            if (mInvalidnetTextView != null) {
                mInvalidnetTextView.setText(message);
            }
        }
    }

    @Override
    public void showError(String error) {
        if (mErrorLayout != null) {
            mFrameSelector.selectFrame(mErrorLayout);
            if (mErrorTextView != null) {
                mErrorTextView.setText(error);
            }
        }
    }

    @Override
    public boolean isProgress() {
        return mProgressLayout != null && mFrameSelector.isCurrent(mProgressLayout);
    }

    @Override
    public boolean isContent() {
        return mContentView != null && mFrameSelector.isCurrent(mContentView);
    }

    @Override
    public boolean isEmpty() {
        return mEmptyLayout != null && mFrameSelector.isCurrent(mEmptyLayout);
    }

    //</editor-fold>
}
