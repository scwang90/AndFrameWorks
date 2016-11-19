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
        int w = width / num - AfDensity.dp2px(5);
        for (int i = 0;i < num; i++)
        {
            float index = 1f * (i + 1) - 1f * (num + 1) / 2;
            mPath.setAlpha(255 - (int) (2 * (Math.abs(index) / num) * 255));
            canvas.drawCircle(width / 2 + cir_x * index + index * w / 3 * 2 - r/2, height / 2, r, mPath);
        }
    }
}
