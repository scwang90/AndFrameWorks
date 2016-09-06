package com.andframe.widget.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class AfPullHeaderLayout extends FrameLayout {
    protected static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

    protected ImageView mHeaderImage;
    protected ProgressBar mHeaderProgress;
    protected TextView mHeaderText;
    protected TextView mUpdateText;
    //protected View mHeaderDivider = null;

    protected String pullLabel;
    protected String refreshingLabel;
    protected String releaseLabel;

    protected SimpleDateFormat mSimpleDateFormat;

    protected Animation animation, animationreset;

    protected enum EnumString {
        header_pulldown, header_updatetime, header_loading, header_release
    }

    protected abstract String getString(Context context, EnumString string);

    protected enum EnumViewID {
        header_view, header_text, update_text,
        header_image, header_progress//,header_divider
    }

    protected abstract int getViewID(EnumViewID id);

    public AfPullHeaderLayout(Context context) {
        super(context);
        this.initailize(context);
        this.setLastUpdateTime(new Date());
        this.reset();
    }

    public AfPullHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initailize(context);
        this.setLastUpdateTime(new Date());
        this.reset();
    }

    private void initailize(Context context) {
        mSimpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.ENGLISH);

        View header = LayoutInflater.from(context).inflate(getViewID(EnumViewID.header_view), this);
        mHeaderText = (TextView) header.findViewById(getViewID(EnumViewID.header_text));
        mUpdateText = (TextView) header.findViewById(getViewID(EnumViewID.update_text));
        mHeaderImage = (ImageView) header.findViewById(getViewID(EnumViewID.header_image));
        mHeaderProgress = (ProgressBar) header.findViewById(getViewID(EnumViewID.header_progress));
        //mHeaderDivider = header.findViewById(getViewID(EnumViewID.header_divider));

        final Interpolator interpolator = new LinearInterpolator();
        animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(interpolator);
        animation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
        animation.setFillAfter(true);

        animationreset = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animationreset.setInterpolator(interpolator);
        animationreset.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
        animationreset.setFillAfter(true);

        pullLabel = getString(context, EnumString.header_pulldown);
        refreshingLabel = getString(context, EnumString.header_loading);
        releaseLabel = getString(context, EnumString.header_release);

        //mHeaderImage.setImageResource(R.drawable.refresh_list_pull_down);
    }


    public void reset() {
        mHeaderText.setText(pullLabel);
        mHeaderImage.setVisibility(View.VISIBLE);
        mHeaderProgress.setVisibility(View.GONE);
    }

    public void releaseToRefresh() {
        mHeaderText.setText(releaseLabel);
        mHeaderImage.clearAnimation();
        mHeaderImage.startAnimation(animation);
    }

    public void setLabelPull(String pullLabel) {
        this.pullLabel = pullLabel;
    }

    public void refreshing() {
        mHeaderText.setText(refreshingLabel);
        mHeaderImage.clearAnimation();
        mHeaderImage.setVisibility(View.INVISIBLE);
        mHeaderProgress.setVisibility(View.VISIBLE);
    }

    public void setLabelRefreshing(String refreshingLabel) {
        this.refreshingLabel = refreshingLabel;
    }

    public void setLabelRelease(String releaseLabel) {
        this.releaseLabel = releaseLabel;
    }

    public void pullToRefresh() {
        mHeaderText.setText(pullLabel);
        mHeaderImage.clearAnimation();
        mHeaderImage.startAnimation(animationreset);
    }

    public void setTextColor(int color) {
        mHeaderText.setTextColor(color);
    }

    public void setText(String text) {
        mHeaderText.setText(text);
    }

    /**
     * 通知 ListView 设置最后刷新时间
     */
    @SuppressLint("SetTextI18n")
    public void setLastUpdateTime(Date tDate) {
        if (mUpdateText != null && mSimpleDateFormat != null) {
            String UpdateText = getString(getContext(), EnumString.header_updatetime);
            mUpdateText.setText(UpdateText + " " + mSimpleDateFormat.format(tDate));
        }
    }
}
