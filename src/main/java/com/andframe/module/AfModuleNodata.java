package com.andframe.module;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.andframe.api.view.Viewer;
import com.andframe.exception.AfExceptionHandler;

@SuppressWarnings("unused")
public abstract class AfModuleNodata extends AfViewModuler implements OnClickListener {

    protected View mButton = null;
    protected TextView mTvButton = null;
    protected TextView mTvDescription = null;
    protected OnClickListener mListener = null;

    public AfModuleNodata(Viewer view, int id) {
        super(view, id);
        initializeComponent(view);
    }

    public AfModuleNodata(Viewer view) {
        super(view);
        initializeComponent(view);
    }

    @Override
    protected void onCreated(Viewer viewable, View view) {
        super.onCreated(viewable, view);
        initView(viewable);
    }

    private void initView(Viewer view) {
        if (isValid()) {
            mButton = findRefreshButton(view);
            mTvDescription = findDescription(view);
            if (mButton instanceof TextView) {
                mTvButton = TextView.class.cast(mButton);
                setButtonText("点击刷新");
            }
            mButton.setOnClickListener(this);
        }
    }

    /**
     * 获取刷新按钮 建议是 TextView
     */
    protected abstract View findRefreshButton(Viewer view);

    /**
     * 获取信息提示 TextView
     */
    protected abstract TextView findDescription(Viewer view);

    public void setDescription(String description) {
        if (isValid()) {
            mTvDescription.setText(description);
        }
    }

    public void setDescription(int id) {
        if (isValid()) {
            mTvDescription.setText(id);
        }
    }

    public void setButtonText(String text) {
        if (isValid()) {
            if (mTvButton != null) {
                mTvButton.setText(text);
            }
        }
    }

    @SuppressWarnings("unused")
    public void setButtonText(int id) {
        if (isValid()) {
            mButton.setId(id);
            if (mTvButton != null) {
                mTvButton.setText(id);
            }
        }
    }

    public void setOnRefreshListener(OnClickListener listener) {
        if (isValid()) {
            mListener = listener;
            if (listener != null) {
                mButton.setVisibility(View.VISIBLE);
            } else {
                mButton.setVisibility(View.GONE);
            }
        }
    }

    @SuppressWarnings("unused")
    public int getButtonId() {
        if (!isValid()) {
            return 0;
        }
        return mButton.getId();
    }

    public View getButton() {
        return mButton;
    }

    @Override
    public void onClick(View v) {
        try {
            if (mListener != null) {
                mListener.onClick(v);
            }
        } catch (Throwable ex) {
            AfExceptionHandler.handle(ex, "AfModuleNodata.onClick");
        }
    }
}
