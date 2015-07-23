package com.andframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.andframe.activity.framework.AfPageable;
import com.andframe.view.pulltorefresh.AfPullToRefreshBase;
import com.andframe.view.pulltorefresh.PullRefreshFooterImpl;
import com.andframe.view.pulltorefresh.PullRefreshHeaderImpl;

public class AfRefreshScorllView extends AfPullToRefreshBase<ScrollView> {
	
	private static ScrollView mScrollView = null;

	public AfRefreshScorllView(ScrollView listView) {
		super((mScrollView=listView).getContext());
		// TODO Auto-generated constructor stub
		setPullFooterLayout(new PullRefreshFooterImpl(listView.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(listView.getContext()));
	}
	
	public AfRefreshScorllView(AfPageable viewable,int res) {
		super((mScrollView=viewable.findViewByID(res)).getContext());
		// TODO Auto-generated constructor stub
		setPullFooterLayout(new PullRefreshFooterImpl(viewable.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(viewable.getContext()));
	}
	
	public AfRefreshScorllView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AfRefreshScorllView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected final ScrollView onCreateRefreshableView(Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		if (mScrollView != null) {
			if (getParent() == null && mScrollView.getParent() instanceof ViewGroup) {
				ViewGroup parent = ViewGroup.class.cast(mScrollView.getParent());
				int index = parent.indexOfChild(mScrollView);
				parent.removeView(mScrollView);
				parent.addView(this, index,mScrollView.getLayoutParams());
				mTargetView = mScrollView;
				mScrollView = null;
			}
			return mTargetView;
		}
		ScrollView scrollview = new ScrollView(context, attrs);
		scrollview.setFadingEdgeLength(0);
		return scrollview;
	}

	@Override
	protected final boolean isReadyForPullDown() {
		// TODO Auto-generated method stub
		// targetview.getOverScrollMode();
		return mTargetView.getScrollY() <= 0;
	}

	@Override
	protected final boolean isReadyForPullUp() {
		// TODO Auto-generated method stub
		return false;
	}

	public final void addChildToScrollView(View child) {
		mTargetView.addView(child);
	}
}
