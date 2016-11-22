package com.andpack.impl.bezierlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.andframe.util.android.AfDensity;

/**
 *
 * Created by cjj on 2015/8/27.
 */
public class RoundDotView extends View {

    private Paint mPath;
    private float r= AfDensity.dp2px(7);
    private int  num = 7;
    private float fraction;

    public void setCir_x(float cir_x) {
        this.cir_x = cir_x;
    }

    private float cir_x;

    public RoundDotView(Context context) {
        this(context, null, 0);
    }

    public RoundDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Paint();
        mPath.setAntiAlias(true);
        mPath.setColor(Color.rgb(114,114,114));
    }

    public void setDotColor(int color) {
        mPath.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float wide = (width / num) * fraction-((fraction>1)?((fraction-1)*(width / num)/fraction):0);//y1 = t*(w/n)-(t>1)*((t-1)*(w/n)/t)
        float high = height - ((fraction>1)?((fraction-1)*height/2/fraction):0);//y2 = x - (t>1)*((t-1)*x/t);
        for (int i = 0 ; i < num; i++)
        {
            float index = 1f + i - (1f + num) / 2;//y3 = (x + 1) - (n + 1)/2; 居中 index 变量：0 1 2 3 4 结果： -2 -1 0 1 2
            float alpha = 255 * (1 - (2 * (Math.abs(index) / num)));//y4 = m * ( 1 - 2 * abs(y3) / n); 横向 alpha 差
            float x = AfDensity.px2dp(height);
            mPath.setAlpha((int) (alpha * (1d - 1d / Math.pow((x / 800d + 1d), 15))));//y5 = y4 * (1-1/((x/800+1)^15));竖直 alpha 差
            canvas.drawCircle(width / 2- r/2 + wide * index , high / 2, r, mPath);
        }
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
    }
}
