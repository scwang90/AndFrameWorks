package com.andframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.andframe.view.pulltorefresh.AfPullToRefreshBase;

public abstract class AfRefreshGridView extends AfPullToRefreshBase<GridView> {
	protected boolean mIsOpenRefresh = true;
	protected boolean mIsNeedFooter = false;

	public AfRefreshGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AfRefreshGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AfRefreshGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected GridView onCreateRefreshableView(Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		mTargetView = onCreateGridView(context,attrs);
		return mTargetView;
	}

	protected GridView onCreateGridView(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new GridView(context, attrs);
	}

//	@Override
//	protected final boolean isReadyForPullDown() {
//		// TODO Auto-generated method stub
//		// targetview.getOverScrollMode();
//		return mIsOpenRefresh
//				&& 5 >= Math.abs(getFirstPositionDistanceGuess(mTargetView)
//						- mTargetView.getTop());
//	}
//
//	@Override
//	protected final boolean isReadyForPullUp() {
//		// TODO Auto-generated method stub
//		return mIsNeedFooter
//				&& 5 >= Math.abs(getLastPositionDistanceGuess(mTargetView)
//						- mTargetView.getBottom());
//	}
//
//	int getFirstPositionDistanceGuess(AbsListView view) {
//		Field field;
//		// 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
//		try {
//			field = AbsListView.class
//					.getDeclaredField("mFirstPositionDistanceGuess");
//			field.setAccessible(true);
//			return (Integer) field.get(view);
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return -1;
//	}
//
//	int getLastPositionDistanceGuess(AbsListView view) {
//		Field field;
//		// 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
//		try {
//			field = AbsListView.class
//					.getDeclaredField("mLastPositionDistanceGuess");
//			field.setAccessible(true);
//			return (Integer) field.get(view);
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return -1;
//
//	}

	@Override
	protected boolean isReadyForPullDown() {
		return mIsOpenRefresh&&isFirstItemVisible();
	}

	@Override
	protected boolean isReadyForPullUp() {
		return mIsNeedFooter&&isLastItemVisible();
	}
	
	private boolean isFirstItemVisible() {
		final Adapter adapter = mTargetView.getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			return true;
		} else {
			/**
			 * This check should really just be:
			 * mRefreshableView.getFirstVisiblePosition() == 0, but PtRListView
			 * internally use a HeaderView which messes the positions up. For
			 * now we'll just add one to account for it and rely on the inner
			 * condition which checks getTop().
			 */
			if (mTargetView.getFirstVisiblePosition() <= 1) {
				final View firstVisibleChild = mTargetView.getChildAt(0);
				if (firstVisibleChild != null) {
					return firstVisibleChild.getTop() >= mTargetView.getTop();
				}
			}
		}
		return false;
	}
	
	private boolean isLastItemVisible() {
		final Adapter adapter = mTargetView.getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			return true;
		} else {
			final int lastItemPosition = mTargetView.getCount() - 1;
			final int lastVisiblePosition = mTargetView.getLastVisiblePosition();
			/**
			 * This check should really just be: lastVisiblePosition ==
			 * lastItemPosition, but PtRListView internally uses a FooterView
			 * which messes the positions up. For me we'll just subtract one to
			 * account for it and rely on the inner condition which checks
			 * getBottom().
			 */
			if (lastVisiblePosition >= lastItemPosition - 1) {
				final int childIndex = lastVisiblePosition - mTargetView.getFirstVisiblePosition();
				final View lastVisibleChild = mTargetView.getChildAt(childIndex);
				if (lastVisibleChild != null) {
					return lastVisibleChild.getBottom() <= mTargetView.getBottom();
				}
			}
		}
		return false;
	}
	


	public void setAdapter(ListAdapter mAdapter) {
		// TODO Auto-generated method stub
		mTargetView.setAdapter(mAdapter);
	}

	public void setRefreshable(boolean able) {
		mIsOpenRefresh = able;
	}

	public final void addMoreView() {
		// TODO Auto-generated method stub
		mIsNeedFooter = true;
	}

	public final void removeMoreView() {
		// TODO Auto-generated method stub
		onRefreshComplete();
		mIsNeedFooter = false;
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		// TODO Auto-generated method stub
		mTargetView.setOnItemClickListener(listener);
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		mTargetView.setOnItemLongClickListener(listener);
	}

}
