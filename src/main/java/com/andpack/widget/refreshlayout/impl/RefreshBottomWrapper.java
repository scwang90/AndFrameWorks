package com.andpack.widget.refreshlayout.impl;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.andpack.widget.refreshlayout.api.RefreshFooter;
import com.andpack.widget.refreshlayout.constant.RefreshState;
import com.andpack.widget.refreshlayout.constant.SpinnerStyle;

/**
 * 刷新底部包装
 * Created by SCWANG on 2017/5/26.
 */

public class RefreshBottomWrapper implements RefreshFooter {
    private View mWrapperView;
    private SpinnerStyle mSpinnerStyle;

    public RefreshBottomWrapper(View wrapper) {
        this.mWrapperView = wrapper;
    }

    @NonNull
    public View getView() {
        return mWrapperView;
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void setPrimaryColor(int... colors) {

    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        if (mSpinnerStyle != null) {
            return mSpinnerStyle;
        }
        ViewGroup.LayoutParams params = mWrapperView.getLayoutParams();
        if (params != null) {
            if (params.height == 0) {
                return mSpinnerStyle = SpinnerStyle.Scale;
            }
        }
        return mSpinnerStyle = SpinnerStyle.Translate;
    }

    @Override
    public void onPullingUp(int offset, int bottomHeight, int extendHeight) {

    }

    @Override
    public void onPullReleasing(int offset, int bottomHeight, int extendHeight) {

    }

    @Override
    public void startAnimator(int bottomHeight, int extendHeight) {

    }

    @Override
    public void onStateChanged(RefreshState state) {

    }
}
