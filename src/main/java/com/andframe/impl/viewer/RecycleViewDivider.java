package com.andframe.impl.viewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class RecycleViewDivider extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Paint mPaint;
    private Drawable mDivider;
    private boolean mDrawFooter = true;
    private int mDividerHeight = 2;//分割线高度，默认为1px
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    /**
     * 默认分割线：高度为2px，颜色为灰色
     */
    public RecycleViewDivider(Context context) {
        mContext = context;
        mOrientation = HORIZONTAL;
    }

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param orientation 列表方向
     */
    public RecycleViewDivider(Context context, int orientation) {
        if (orientation != VERTICAL && orientation != HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mContext = context;
        mOrientation = orientation;

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public RecycleViewDivider orientation(int orientation) {
        mOrientation = orientation== HORIZONTAL? HORIZONTAL: VERTICAL;
        return this;
    }

    public RecycleViewDivider size(int dividerHeight) {
        mDividerHeight = dividerHeight;
        return this;
    }

    public RecycleViewDivider color(int dividerColor) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
        return this;
    }

    public RecycleViewDivider sizeId(int dividerSizeId) {
        return size(mContext.getResources().getDimensionPixelSize(dividerSizeId));
    }

    public RecycleViewDivider colorId(int dividerColorId) {
        return color(ContextCompat.getColor(mContext, dividerColorId));
    }

    public RecycleViewDivider drawable(int drawableId) {
        return drawable(ContextCompat.getDrawable(mContext, drawableId));
    }

    public RecycleViewDivider drawFooter(boolean value) {
        this.mDrawFooter = value;
        return this;
    }

    private RecycleViewDivider drawable(Drawable drawable) {
        mDivider = drawable;
        mDividerHeight = mDivider.getIntrinsicHeight();
        return this;
    }


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        mContext = null;
        super.getItemOffsets(outRect, view, parent, state);
        int position = mDrawFooter ? 0 : parent.getChildAdapterPosition(view);
        int itemCount = mDrawFooter? 2 : parent.getAdapter().getItemCount();
        if (position < itemCount - 1) {
            outRect.set(0, 0, 0, mDividerHeight);
        }
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        int itemCount = mDrawFooter? 2 : parent.getAdapter().getItemCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            final int position = mDrawFooter ? 0 : parent.getChildAdapterPosition(child);
            if (position < itemCount - 1) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + layoutParams.bottomMargin;
                final int bottom = top + mDividerHeight;
                if (mDivider != null) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
                if (mPaint != null) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
    }

    //绘制纵向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        int itemCount = mDrawFooter? 2 : parent.getAdapter().getItemCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            final int position = mDrawFooter ? 0 : parent.getChildAdapterPosition(child);
            if (position < itemCount - 1) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + layoutParams.rightMargin;
                final int right = left + mDividerHeight;
                if (mDivider != null) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
                if (mPaint != null) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
    }
}