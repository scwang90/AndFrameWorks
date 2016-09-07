package com.andframe.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.andframe.api.page.Pager;
import com.andframe.widget.pulltorefresh.AfPullToRefreshBase;
import com.andframe.widget.pulltorefresh.PullRefreshFooterImpl;
import com.andframe.widget.pulltorefresh.PullRefreshHeaderImpl;

@SuppressWarnings("unused")
public class AfRefreshScorllView extends AfPullToRefreshBase<ScrollView> {
	
	private static ScrollView mScrollView = null;

	public AfRefreshScorllView(ScrollView listView) {
		super((mScrollView=listView).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(listView.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(listView.getContext()));
	}
	
	public AfRefreshScorllView(Pager viewable, int res) {
		super((mScrollView=viewable.findViewByID(res)).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(viewable.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(viewable.getContext()));
	}
	
	public AfRefreshScorllView(Context context) {
		super(context);
	}

	public AfRefreshScorllView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected final ScrollView onCreateRefreshableView(Context context,
			AttributeSet attrs) {
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
		return mTargetView.getScrollY() <= 0;
	}

	@Override
	protected final boolean isReadyForPullUp() {
		return false;
	}

	public final void addChildToScrollView(View child) {
		mTargetView.addView(child);
	}
}
