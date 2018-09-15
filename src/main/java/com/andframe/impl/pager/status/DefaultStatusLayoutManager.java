package com.andframe.impl.pager.status;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.api.pager.status.OnRefreshListener;
import com.andframe.api.pager.status.StatusLayoutManager;
import com.andframe.application.AfApp;
import com.andframe.module.AfFrameSelector;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 可切换状态页面的布局
 * Created by SCWANG on 2016/10/20.
 */

@SuppressWarnings({"WeakerAccess", "RedundantCast"})
public class DefaultStatusLayoutManager implements StatusLayoutManager {

    protected final FrameLayout mFrameLayout;
    protected final AfFrameSelector mFrameSelector;
    protected View mContentView;
    protected View mEmptyLayout;
    protected View mErrorLayout;
    protected View mProgressLayout;
    protected View mInvalidNetLayout;
    protected TextView mErrorTextView;
    protected TextView mEmptyTextView;
    protected TextView mProgressTextView;
    protected TextView mInvalidNetTextView;
    protected OnRefreshListener mOnRefreshListener;
    protected View.OnClickListener mOnRefreshClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnRefreshListener != null && mOnRefreshListener.onRefresh()) {
                showProgress();
            }
        }
    };

    public DefaultStatusLayoutManager(Context content) {
        this(new FrameLayout(content));
    }

    public DefaultStatusLayoutManager(FrameLayout frameLayout) {
        mFrameLayout = frameLayout;
        mFrameSelector = new AfFrameSelector(frameLayout);
        if (mFrameLayout.getChildCount() > 0) {
            mContentView = mFrameLayout.getChildAt(0);
        }
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mFrameLayout;
    }

    //<editor-fold desc="视图初始化">
    @Override
    public void setContentView(View content) {
        if (mContentView != null) {
            mFrameLayout.removeView(mContentView);
        }
        ViewGroup.LayoutParams params = content.getLayoutParams();
        int height = params == null ? MATCH_PARENT : params.height;
        mFrameLayout.addView(mContentView = content, MATCH_PARENT, height == 0 ? MATCH_PARENT : height);
    }

    @Override
    public void wrapper(View content) {
        ViewParent parent = content.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            ViewGroup.LayoutParams params = content.getLayoutParams();
            int index = group.indexOfChild(content);
            group.removeViewAt(index);
            setContentView(content);
            group.addView(getLayout(),index,params);
        }
    }

    @LayoutRes
    @Override
    public int defaultLayoutEmptyId() {
        return R.layout.af_status_empty;
    }

    @IdRes
    @Override
    public int defaultLayoutEmptyTextId() {
        return R.id.module_empty_description;
    }

    @IdRes
    @Override
    public int defaultLayoutEmptyButtonId() {
        return 0;//module_empty_layout 不指定将会使用整个布局作为按钮
    }

    @StringRes
    @Override
    public int defaultLayoutEmptyMessageId() {
        return R.string.status_empty_data;
    }

    @LayoutRes
    @Override
    public int defaultLayoutErrorId() {
        return R.layout.af_status_empty;
    }

    @IdRes
    @Override
    public int defaultLayoutErrorButtonId() {
        return 0;//module_empty_layout 不指定将会使用整个布局作为按钮
    }

    @IdRes
    @Override
    public int defaultLayoutErrorTextId() {
        return R.id.module_empty_description;
    }

    @LayoutRes
    @Override
    public int defaultLayoutInvalidNetId() {
        return R.layout.af_status_empty;
    }

    @IdRes
    @Override
    public int defaultLayoutInvalidNetButtonId() {
        return 0;//module_empty_layout 不指定将会使用整个布局作为按钮
    }

    @IdRes
    @Override
    public int defaultLayoutInvalidNetTextId() {
        return R.id.module_empty_description;
    }

    @LayoutRes
    @Override
    public int defaultLayoutProgressId() {
        return R.layout.af_status_progress;
    }

    @IdRes
    @Override
    public int defaultLayoutProgressTextId() {
        return R.id.status_progress_text;
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
    public void setProgressLayout(int layoutId,int textId) {
        if (mProgressLayout != null) {
            mFrameLayout.removeView(mProgressLayout);
        }
        mProgressLayout = View.inflate(mFrameLayout.getContext(), layoutId, null);
        mProgressTextView = (TextView) mProgressLayout.findViewById(textId);
        mFrameLayout.addView(mProgressLayout);
    }

    @Override
    public void setEmptyLayout(int layoutId) {
        setEmptyLayout(layoutId, 0, 0);
    }

    @Override
    public void setEmptyLayout(int layoutId, int textId) {
        setEmptyLayout(layoutId, textId, 0);
    }

    @Override
    public void setEmptyLayout(int layoutId, int textId, int btnId) {
        setEmptyLayout(layoutId, textId, btnId, "");
    }

    @Override
    public void setEmptyLayout(int layoutId, int textId, int btnId, String message) {
        if (mEmptyLayout != null) {
            mFrameLayout.removeView(mEmptyLayout);
        }
        mEmptyLayout = View.inflate(mFrameLayout.getContext(), layoutId, null);
        mEmptyTextView = (TextView) mEmptyLayout.findViewById(textId);
        if (mEmptyTextView != null && !TextUtils.isEmpty(message)) {
            mEmptyTextView.setText(message);
        }
        View btn = mEmptyLayout.findViewById(btnId);
        btn = btn == null ? mEmptyLayout : btn;
        btn.setOnClickListener(mOnRefreshClickListener);
        mFrameLayout.addView(mEmptyLayout);
    }

    @Override
    public void setErrorLayout(int layoutId, int textId) {
        setErrorLayout(layoutId, textId, 0);
    }

    @Override
    public void setErrorLayout(int layoutId, int textId, int btnId) {
        if (mErrorLayout != null) {
            mFrameLayout.removeView(mErrorLayout);
        }
        mErrorLayout = View.inflate(mFrameLayout.getContext(), layoutId, null);
        mErrorTextView = (TextView) mErrorLayout.findViewById(textId);
        View btn = mErrorLayout.findViewById(btnId);
        btn = btn == null ? mErrorLayout : btn;
        btn.setOnClickListener(mOnRefreshClickListener);
        mFrameLayout.addView(mErrorLayout);
    }

    @Override
    public void setInvalidNetLayout(int layoutId) {
        setInvalidNetLayout(layoutId, 0, 0);
    }

    @Override
    public void setInvalidNetLayout(int layoutId, int textId) {
        setInvalidNetLayout(layoutId, textId, 0);
    }

    @Override
    public void setInvalidNetLayout(int layoutId, int textId, int btnId) {
        if (mInvalidNetLayout != null) {
            mFrameLayout.removeView(mInvalidNetLayout);
        }
        mInvalidNetLayout = View.inflate(mFrameLayout.getContext(), layoutId, null);
        mInvalidNetTextView = (TextView) mInvalidNetLayout.findViewById(textId);
        View btn = mInvalidNetLayout.findViewById(btnId);
        btn = btn == null ? mInvalidNetLayout : btn;
        btn.setOnClickListener(mOnRefreshClickListener);
        mFrameLayout.addView(mInvalidNetLayout);
    }

    @Override
    public void autoCompletedLayout() {
        if (mEmptyLayout == null) {
            setEmptyLayout(defaultLayoutEmptyId(), defaultLayoutEmptyTextId(), defaultLayoutEmptyButtonId());
        }
        if (mErrorLayout == null) {
            setErrorLayout(defaultLayoutErrorId(), defaultLayoutErrorTextId(), defaultLayoutErrorButtonId());
        }
        if (mInvalidNetLayout == null) {
            setInvalidNetLayout(defaultLayoutInvalidNetId(), defaultLayoutInvalidNetTextId(), defaultLayoutInvalidNetButtonId());
        }
        if (mProgressLayout == null) {
            setProgressLayout(defaultLayoutProgressId(), defaultLayoutProgressTextId());
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
    public void showInvalidNet() {
        if (mInvalidNetLayout != null) {
            mFrameSelector.selectFrame(mInvalidNetLayout);
        } else {
            showError(AfApp.get().getString(R.string.status_invalid_net));
        }
    }

    @Override
    public void showInvalidNet(String message) {
        if (mInvalidNetLayout != null) {
            mFrameSelector.selectFrame(mInvalidNetLayout);
            if (mInvalidNetTextView != null) {
                mInvalidNetTextView.setText(message);
            }
        } else {
            showError(message);
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
