package com.andframe.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.andframe.view.pulltorefresh.AfPullToRefreshBase;

import java.lang.reflect.Field;

public abstract class AfRefreshAbsListView<T extends AbsListView> extends AfPullToRefreshBase<T> {

	protected ListAdapter mAdapter = null;
	protected boolean mIsNeedFooter = false;
	protected boolean mIsOpenRefresh = true;
	protected T mAbsListView;

	public AfRefreshAbsListView(Context context) {
		super(context);
	}

	public AfRefreshAbsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AfRefreshAbsListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setAdapter(ListAdapter adapter) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mTargetView.setAdapter(mAdapter = adapter);
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mTargetView.setOnItemClickListener(listener);
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		mTargetView.setOnItemLongClickListener(listener);
	}

	protected abstract T onCreateTargetView(Context context, AttributeSet attrs);

	@Override
	@SuppressLint("NewApi")
	protected T onCreateRefreshableView(Context context, AttributeSet attrs) {
		mAbsListView = onCreateTargetView(context, attrs);//new ListView(context)
		// 解决listview在拖动的时候背景图片消失变成黑色背景
		mAbsListView.setCacheColorHint(0);
		mAbsListView.setScrollingCacheEnabled(false);
		// 解决listview的上边和下边有黑色的阴影
		mAbsListView.setFadingEdgeLength(0);
		return mAbsListView;
	}

	public void setRefreshable(boolean able) {
		mIsOpenRefresh = able;
	}
	
//	@Override
//	protected final boolean isReadyForPullDown() {
//		return mIsOpenRefresh 
//				&&mTargetView.getFirstVisiblePosition() == 0
//				&& mTargetView.getScrollY() <= 0;
//	}
//	
//	@Override
//	protected final boolean isReadyForPullUp() {
//		return mIsNeedFooter &&
//				mTargetView.getLastVisiblePosition() 
//				== mTargetView.getCount() - 1;
//	}
	
	@Override
	protected final boolean isReadyForPullDown() {
		// targetview.getOverScrollMode();
		return mIsOpenRefresh
				&& 5 >= Math.abs(getFirstPositionDistanceGuess(mTargetView)
						- mTargetView.getTop());
	}

	@Override
	protected final boolean isReadyForPullUp() {
		return mIsNeedFooter
				&& 5 >= Math.abs(getLastPositionDistanceGuess(mTargetView)
						- mTargetView.getBottom());
	}

	int getFirstPositionDistanceGuess(AbsListView view) {
		Field field;
		// 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
		try {
			field = AbsListView.class
					.getDeclaredField("mFirstPositionDistanceGuess");
			field.setAccessible(true);
			return (Integer) field.get(view);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return -1;
	}

	int getLastPositionDistanceGuess(AbsListView view) {
		Field field;
		// 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
		try {
			field = AbsListView.class
					.getDeclaredField("mLastPositionDistanceGuess");
			field.setAccessible(true);
			return (Integer) field.get(view);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return -1;

	}

//	@Override
//	protected boolean isReadyForPullDown() {
//		return mIsOpenRefresh&&isFirstItemVisible();
//	}
//
//	@Override
//	protected boolean isReadyForPullUp() {
//		return mIsNeedFooter&&isLastItemVisible();
//	}
//
//	private boolean isFirstItemVisible() {
//		final Adapter adapter = mTargetView.getAdapter();
//
//		if (null == adapter || adapter.isEmpty()) {
//			return true;
//		} else {
//			/**
//			 * This check should really just be:
//			 * mRefreshableView.getFirstVisiblePosition() == 0, but PtRListView
//			 * internally use a HeaderView which messes the positions up. For
//			 * now we'll just add one to account for it and rely on the inner
//			 * condition which checks getTop().
//			 */
//			if (mTargetView.getFirstVisiblePosition() <= 1) {
//				final View firstVisibleChild = mTargetView.getChildAt(0);
//				if (firstVisibleChild != null) {
//					return firstVisibleChild.getTop() >= mTargetView.getTop();
//				}
//			}
//		}
//		return false;
//	}
//
//	private boolean isLastItemVisible() {
//		final Adapter adapter = mTargetView.getAdapter();
//		if (null == adapter || adapter.isEmpty()) {
//			return true;
//		} else {
//			final int lastItemPosition = mTargetView.getCount() - 1;
//			final int lastVisiblePosition = mTargetView.getLastVisiblePosition();
//			/**
//			 * This check should really just be: lastVisiblePosition ==
//			 * lastItemPosition, but PtRListView internally uses a FooterView
//			 * which messes the positions up. For me we'll just subtract one to
//			 * account for it and rely on the inner condition which checks
//			 * getBottom().
//			 */
//			if (lastVisiblePosition >= lastItemPosition - 1) {
//				final int childIndex = lastVisiblePosition - mTargetView.getFirstVisiblePosition();
//				final View lastVisibleChild = mTargetView.getChildAt(childIndex);
//				if (lastVisibleChild != null) {
//					return lastVisibleChild.getBottom() <= mTargetView.getBottom();
//				}
//			}
//		}
//		return false;
//	}
	
	public final void addMoreView() {
		mIsNeedFooter = true;
	}

	public final void removeMoreView() {
		onRefreshComplete();
		mIsNeedFooter = false;
	}

	public int getCount() {
		return mTargetView.getCount();
	}

	public void smoothScrollToPosition(int position) {
		mTargetView.smoothScrollToPosition(position);
	}

}
