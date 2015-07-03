package com.andframe.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
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

	@Override
	protected final boolean isReadyForPullDown() {
		// TODO Auto-generated method stub
		// targetview.getOverScrollMode();
		return mIsOpenRefresh
				&& 5 >= Math.abs(getFirstPositionDistanceGuess(mTargetView)
						- mTargetView.getTop());
	}

	@Override
	protected final boolean isReadyForPullUp() {
		// TODO Auto-generated method stub
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;

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
