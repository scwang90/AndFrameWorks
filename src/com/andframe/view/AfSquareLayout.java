package com.andframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 方正布局
 * Created by SCWANG on 2016/3/9.
 */
public class AfSquareLayout extends RelativeLayout{

    public AfSquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AfSquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AfSquareLayout(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        //高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(childWidthSize, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
