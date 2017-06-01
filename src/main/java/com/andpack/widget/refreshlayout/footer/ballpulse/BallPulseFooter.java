package com.andpack.widget.refreshlayout.footer.ballpulse;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.andpack.widget.refreshlayout.api.RefreshFooter;
import com.andpack.widget.refreshlayout.constant.RefreshState;
import com.andpack.widget.refreshlayout.constant.SpinnerStyle;
import com.andpack.widget.refreshlayout.util.DensityUtil;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 球脉冲底部加载组件
 * Created by SCWANG on 2017/5/30.
 */

public class BallPulseFooter extends FrameLayout implements RefreshFooter {

    private BallPulseView mBallPulseView;

    public BallPulseFooter(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public BallPulseFooter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BallPulseFooter(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mBallPulseView = new BallPulseView(context);
        addView(mBallPulseView, WRAP_CONTENT, WRAP_CONTENT);
        setMinimumHeight(DensityUtil.dp2px(60));
    }

    @Override
    public void onPullingUp(int offset, int bottomHeight, int extendHeight) {
        mBallPulseView.onPullingUp(offset,bottomHeight,extendHeight);
    }

    @Override
    public void onPullReleasing(int offset, int bottomHeight, int extendHeight) {
        mBallPulseView.onPullReleasing(offset,bottomHeight,extendHeight);
    }

    @Override
    public void startAnimator(int bottomHeight, int extendHeight) {
        mBallPulseView.startAnimator(bottomHeight, extendHeight);
    }

    @Override
    public void onStateChanged(RefreshState state) {
        mBallPulseView.onStateChanged(state);
    }

    @Override
    public void onFinish() {
        mBallPulseView.onFinish();
    }

    @Override
    public void setPrimaryColor(int... colors) {
        mBallPulseView.setPrimaryColor(colors);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }
}
