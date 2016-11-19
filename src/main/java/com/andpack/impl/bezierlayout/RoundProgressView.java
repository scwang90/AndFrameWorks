package com.andpack.impl.bezierlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.andframe.util.android.AfDensity;

/**
 * Created by Administrator on 2015/8/27.
 */
public class RoundProgressView extends View {
    private Paint mPath;
    private Paint mPantR;
    private float r= AfDensity.dp2px(20);
    private int stratAngle =270 ;
    private int endAngle = 0;
    private int outCir_value = AfDensity.dp2px(7);

    public void setCir_x(int cir_x) {
        this.cir_x = cir_x;
    }

    private int cir_x;

    public RoundProgressView(Context context) {
        this(context, null, 0);
    }

    public RoundProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    ValueAnimator va;

    private void init() {
        mPath = new Paint();
        mPantR = new Paint();
        mPantR.setColor(Color.WHITE);
        mPantR.setAntiAlias(true);
        mPath.setAntiAlias(true);
        mPath.setColor(Color.rgb(114, 114, 114));

        va = ValueAnimator.ofInt(0,360);
        va.setDuration(720);
        va.addUpdateListener(animation -> {
            endAngle = (int) animation.getAnimatedValue();
            postInvalidate();
        });
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void setBackColor(int color) {
//        mPath.setColor(color);
    }

    public void setFrontColor(int color) {
        mPath.setColor(color&0x00FFFFFF|0x55000000);
        mPantR.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mPath.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, r, mPath);
        canvas.save();
        mPath.setStyle(Paint.Style.STROKE);//设置为空心
        mPath.setStrokeWidth(AfDensity.dp2px(3));
        canvas.drawCircle(width / 2, height / 2, r + outCir_value, mPath);
        canvas.restore();

        mPantR.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(width/2-r, height/2-r, width/2+r, height/2+r);// 设置个新的长方形，扫描测量
        canvas.drawArc(oval, stratAngle, endAngle, true, mPantR);
        canvas.save();
        mPantR.setStrokeWidth(AfDensity.dp2px(3));
        mPantR.setStyle(Paint.Style.STROKE);
        RectF oval2 = new RectF(width/2-r-outCir_value, height/2-r-outCir_value, width/2+r+outCir_value, height/2+r+outCir_value);// 设置个新的长方形，扫描测量
        canvas.drawArc(oval2, stratAngle, endAngle, false, mPantR);
        canvas.restore();
    }

    public void startAnim(){
        if (va!=null) va.start();
    }

    public void stopAnim(){
        if (va!=null && va.isRunning()) va.cancel();
    }

}
