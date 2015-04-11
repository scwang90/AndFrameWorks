package com.andframe.activity.albumn;

import com.andframe.exception.AfException;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AfAlbumViewPager extends ViewPager{

	private boolean mHorizontalScrollBarEnabled;
	
	public AfAlbumViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public AfAlbumViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (!mHorizontalScrollBarEnabled) {
			return false;
		}
		return super.onTouchEvent(arg0);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (!mHorizontalScrollBarEnabled) {
			return false;
		}
		try {
			return super.onInterceptTouchEvent(arg0);
		} catch (Throwable e) {
			// TODO: handle exception
			AfException.handle(e, "AfAlbumViewPager.onInterceptTouchEvent ²¶×½Òì³££¡");
			return false;
		}
	}

	@Override
	public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
		// TODO Auto-generated method stub
		mHorizontalScrollBarEnabled = horizontalScrollBarEnabled;
		super.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
	}
}
