package com.andpack.widget.refreshlayout.header;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.andpack.widget.refreshlayout.api.RefreshHeader;
import com.andpack.widget.refreshlayout.api.SizeDefinition;
import com.andpack.widget.refreshlayout.constant.RefreshState;
import com.andpack.widget.refreshlayout.constant.SpinnerStyle;
import com.andpack.widget.refreshlayout.util.DensityUtil;

/**
 *
 * Created by SCWANG on 2017/5/31.
 */

public class PhoenixHeader extends View implements RefreshHeader, SizeDefinition {

    private static final int ANIMATION_DURATION = 1000;
    private static final float SUN_INITIAL_ROTATE_GROWTH = 1.2f;
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private Bitmap mSky;
    private Bitmap mSun;
    private Bitmap mTown;
    private Matrix mMatrix;
    private float mPercent;
    private float mRotate;
    private int mHeaderHeight;
    private int mSunSize;
    private boolean isRefreshing;
    private Animation mAnimation;

    public PhoenixHeader(Context context) {
        this(context,null);
    }

    public PhoenixHeader(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PhoenixHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mMatrix = new Matrix();
        DensityUtil density = new DensityUtil();
        mSunSize = density.dip2px(40);
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                mRotate = (interpolatedTime);
                invalidate();
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DURATION);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createBitmaps();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycleBitmaps();
    }

    private void recycleBitmaps() {
        if (mSky != null && !mSky.isRecycled()) {
            mSky.recycle();
        }
        if (mTown != null && !mTown.isRecycled()) {
            mTown.recycle();
        }
        if (mSun != null && !mSun.isRecycled()) {
            mSun.recycle();
        }
    }

    private void createBitmaps() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inTargetDensity = Resources.getSystem().getDisplayMetrics().densityDpi;
        options.inScaled = true;

        Resources resources = getResources();
//        mSky = BitmapFactory.decodeResource(resources, R.drawable.sky, options);
//        mTown = BitmapFactory.decodeResource(resources, R.drawable.buildings, options);
//        mSun = BitmapFactory.decodeResource(resources, R.drawable.sun, options);
    }

    @Override
    public void onPullingDown(int offset, int headHeight, int extendHeight) {
        mRotate = mPercent = 1f * offset / headHeight;
        mHeaderHeight = headHeight;
    }

    @Override
    public void onReleasing(int offset, int headHeight, int extendHeight) {
        mRotate = mPercent = 1f * offset / headHeight;
        mHeaderHeight = headHeight;
    }

    @Override
    public void startAnimator(int headHeight, int extendHeight) {
        isRefreshing = true;
        startAnimation(mAnimation);
    }

    @Override
    public void onStateChanged(RefreshState state) {

    }

    @Override
    public void onFinish() {
        isRefreshing = false;
        clearAnimation();
    }

    @Override
    public void setPrimaryColor(int... colors) {

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
    }

    @Override
    public void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        drawSky(canvas, width, height);
        drawSun(canvas, width, height);
        drawTown(canvas, width, height);
    }


    private void drawSky(Canvas canvas, int width, int height) {
        Matrix matrix = mMatrix;
        matrix.reset();

        int bWidth = mSky.getWidth();
        int bHeight = mSky.getHeight();
        float townScale = 1f * width / bWidth;
        float offsetx = 0;
        float offsety = height / 2 - bHeight / 2;

        matrix.postScale(townScale, townScale);
        matrix.postTranslate(offsetx, offsety);

        canvas.drawBitmap(mSky, matrix, null);
    }

    private void drawTown(Canvas canvas, int width, int height) {
        Matrix matrix = mMatrix;
        matrix.reset();

        int bWidth = mTown.getWidth();
        int bHeight = mTown.getHeight();
        float townScale = 1f * width / bWidth;
        float amplification = (0.7f * Math.max(mPercent - 1, 0) + 1);
        float offsetx = width / 2 - (int) (width * amplification) / 2;
        float offsety = mHeaderHeight * 0.1f * mPercent;
        townScale = amplification * townScale;

        if (offsety + bHeight * townScale < height) {
            offsety = height - bHeight * townScale;
        }

        matrix.postScale(townScale, townScale);
        matrix.postTranslate(offsetx, offsety);

        canvas.drawBitmap(mTown, matrix, null);
    }

    private void drawSun(Canvas canvas, int width, int height) {
        Matrix matrix = mMatrix;
        matrix.reset();


        float mSunLeftOffset = 0.3f * (float) width;
        float mSunTopOffset = (mHeaderHeight * 0.1f);

        float sunRadius = (float) mSunSize / 2.0f;
        float offsetX = mSunLeftOffset + sunRadius;
        float offsetY = mSunTopOffset + (mHeaderHeight / 2) * (1.0f - Math.min(mPercent, 1)); // Move the sun up

        int bWidth = mSun.getWidth();
        float sunScale = 1f * mSunSize / bWidth;

        if (mPercent > 1) {
            sunScale = sunScale * (1.0f - 0.5f * (mPercent - 1));
            sunRadius = sunRadius * (1.0f - 0.5f * (mPercent - 1));
        }

        matrix.preScale(sunScale, sunScale);
        matrix.postRotate((isRefreshing ? -360 : 360) * mRotate * (isRefreshing ? 1 : SUN_INITIAL_ROTATE_GROWTH),
                sunRadius,
                sunRadius);

        canvas.save();
        canvas.translate(offsetX, offsetY);
        canvas.drawBitmap(mSun, matrix, null);
        canvas.restore();
    }

    @Override
    public int defineHeight() {
        return (int)(Resources.getSystem().getDisplayMetrics().widthPixels * 0.27);
    }

    @Override
    public int defineExtendHeight() {
        return (int) (defineHeight() * 0.8f);
    }
}
