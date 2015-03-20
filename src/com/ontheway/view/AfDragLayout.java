package com.ontheway.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
/**
 * @Description: 
 * 抽屉布局
 * @Author: scwang
 * @Version: V1.0, 2015-1-27 下午2:22:48
 * @Modified: 初次创建AfDragLayout类
 */
public class AfDragLayout  extends RelativeLayout implements OnTouchListener,OnClickListener,Runnable{

	private Handler mHandler=new Handler();
	private int interval = 15;
	
	private Boolean mIsExpand = false;
	private View mDragView = null;
	private View mContent = null;
	private View mSummary = null;

	private int  mLastValue;
	private int  mLastDevalue;
	private int  mContentHeight;
	private int  mSummaryHeight;
	private int  mLayoutHeight;
	private int  mCurDragValue = 0;

	public AfDragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mDragView = findViewById(android.R.id.hint);
		mContent = findViewById(android.R.id.content);
		mSummary = findViewById(android.R.id.summary);
		if(mDragView != null){
			mDragView.setOnTouchListener(this);
			mDragView.setOnClickListener(this);
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	mIsExpand = !mIsExpand;
    	mHandler.postDelayed(this, interval);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	mLastValue = (int) event.getRawY();// 获取触摸事件触摸位置的原始Y坐标
        	mLastDevalue = mCurDragValue;
        	mContentHeight = mContent.getHeight();
        	mSummaryHeight = mSummary.getHeight();
            break;
        case MotionEvent.ACTION_MOVE:

        	int newValue = (int) event.getRawY();
        	int newDevalue = newValue - mLastValue;

        	mCurDragValue = mLastDevalue + newDevalue;
        	mCurDragValue = (mCurDragValue < 0)?0:mCurDragValue;
        	mCurDragValue = (mCurDragValue > mContentHeight)?mContentHeight:mCurDragValue;
        	mLayoutHeight = mCurDragValue + mSummaryHeight;
        	
        	this.layout(getLeft(), getTop(), getRight(), getTop() + mLayoutHeight);
        	mContent.layout(mContent.getLeft(), mCurDragValue-mContentHeight, mContent.getRight(), mCurDragValue);
			mSummary.layout(mSummary.getLeft(), mCurDragValue, mSummary.getRight(), mCurDragValue+mSummaryHeight);

            break;
        case MotionEvent.ACTION_UP:
        	if(mCurDragValue != 0 && mCurDragValue != mContentHeight){
            	mIsExpand = mCurDragValue - mLastDevalue < 0;
            	mHandler.postDelayed(this, interval);
        	}
            break;
        }
        return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(mIsExpand && mCurDragValue != mContentHeight){
			mCurDragValue += 10;
			if(mCurDragValue > mContentHeight){
				mCurDragValue = mContentHeight;
			}else{
				mHandler.postDelayed(this,interval);
			}
		}else if(!mIsExpand && mCurDragValue != 0){
			mCurDragValue -= 10;       
			if(mCurDragValue < 0){
				mCurDragValue = 0;
			}else{
				mHandler.postDelayed(this,interval);
			}
		}
    	mLayoutHeight = mCurDragValue + mSummaryHeight;
    	this.layout(getLeft(), getTop(), getRight(), getTop() + mLayoutHeight);
    	mContent.layout(mContent.getLeft(), mCurDragValue-mContentHeight, mContent.getRight(), mCurDragValue);
		mSummary.layout(mSummary.getLeft(), mCurDragValue, mSummary.getRight(), mCurDragValue+mSummaryHeight);
	}


}
