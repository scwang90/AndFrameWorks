package com.andframe.view;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.view.pulltorefresh.AfPullToRefreshBase;

public abstract class AfRefreshListView<T extends ListView> extends AfPullToRefreshBase<T> {
	protected ListAdapter mAdapter = null;
	protected boolean mIsNeedFooter = false;
	protected boolean mIsOpenRefresh = true;
	protected T mListView;

	public AfRefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AfRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AfRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public final Object getData(int position) {
		if (mAdapter != null) {
			return mAdapter.getItem(getDataIndex(position));
		}
		return mTargetView.getItemAtPosition(getDataIndex(position));
	}
	

	public final <TT> TT getData(int position,Class<TT> clazz) {
		Object item = new Object();
		if (mAdapter != null) {
			item = mAdapter.getItem(getDataIndex(position));
		}else{
			item = mTargetView.getItemAtPosition(getDataIndex(position));
		}
		if(clazz.isInstance(item)){
			return clazz.cast(item);
		}
		return null;
	}

	public void setAdapter(ListAdapter adapter) {
		mTargetView.setAdapter(mAdapter = adapter);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mTargetView.setOnItemClickListener(listener);
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		mTargetView.setOnItemLongClickListener(listener);
	}

	protected abstract T onCreateListView(Context context, AttributeSet attrs);

	@Override
	@SuppressLint("NewApi")
	protected T onCreateRefreshableView(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		mListView = onCreateListView(context,attrs);//new ListView(context)
//		View view = new View(context);
//		mListView.addHeaderView(view, null, true);
//		mListView.addFooterView(view, null, true);
//		mListView.setHeaderDividersEnabled(true);
//		mListView.setFooterDividersEnabled(false);

		if(VERSION.SDK_INT >= 16){
			mListView.setDivider(getDividerDrawable());
		}else if(VERSION.SDK_INT >= 14){
			mListView.setDividerHeight(getDividerPadding());
			mListView.setDivider(new BitmapDrawable(getResources(),
					getDrawingCache()));
		}else{
			mListView.setDividerHeight(1);
			mListView.setDivider(new BitmapDrawable(getResources(),
					getDrawingCache()));
		}
		
		// 解决listview在拖动的时候背景图片消失变成黑色背景
		mListView.setCacheColorHint(0);
		mListView.setScrollingCacheEnabled(false);
		// 解决listview的上边和下边有黑色的阴影
		mListView.setFadingEdgeLength(0);
		// TODO Auto-generated method stub
		// TypedArray array =
		// context.obtainStyledAttributes(attrs,android.R.style.DeviceDefault_Light_ButtonBar);
		// listview.setCacheColorHint(array.getInteger(R.styleable.PullToRefresh_cacheColorHint,0));
		// listview.setDividerHeight((int)array.getDimension(R.styleable.PullToRefresh_dividerHeight,
		// 1));
		// listview.setDivider(array.getDrawable(R.styleable.PullToRefresh_divider));
		// array.recycle();

		// ScrollBarUtil.bindScrollBar(listview, R.drawable.shape_scrollbar);
		return mListView;
	}

	public void setRefreshable(boolean able) {
		mIsOpenRefresh = able;
	}
	
//	@Override
//	protected final boolean isReadyForPullDown() {
//		// TODO Auto-generated method stub
//		return mIsOpenRefresh 
//				&&mTargetView.getFirstVisiblePosition() == 0
//				&& mTargetView.getScrollY() <= 0;
//	}
//	
//	@Override
//	protected final boolean isReadyForPullUp() {
//		// TODO Auto-generated method stub
//		return mIsNeedFooter &&
//				mTargetView.getLastVisiblePosition() 
//				== mTargetView.getCount() - 1;
//	}
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
	
	public final void addMoreView() {
		// TODO Auto-generated method stub
		mIsNeedFooter = true;
	}

	public final void removeMoreView() {
		// TODO Auto-generated method stub
		onRefreshComplete();
		mIsNeedFooter = false;
	}

	/**
	 * Returns the number of header views in the list. Header views are special
	 * views at the top of the list that should not be recycled during a layout.
	 * 
	 * @return
	 */
	public final int getHeaderViewsCount() {
		// TODO Auto-generated method stub
		return mTargetView.getHeaderViewsCount() - 1;
	}

	/**
	 * 获取 OnItemClick 中的index 对应ListView 中的index 包含 HeaderView
	 * @param index
	 * @return
	 */
	public final int getIndex(int index) {
		// TODO Auto-generated method stub
		return index - 0;//1;
	}

	/**
	 * 获取 OnItemClick 中的index 对应Adapter 中的index
	 * @param index
	 * @return
	 */
	public final int getDataIndex(int index) {
		// TODO Auto-generated method stub
		if (index < mTargetView.getHeaderViewsCount()) {
			return index;
		}
		return index - mTargetView.getHeaderViewsCount();
	}

	public void addHeaderView(View v) {
		mTargetView.addHeaderView(v);
	}
	
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		mTargetView.addHeaderView(v, data, isSelectable);
	}

	public void addFooterView(View v) {
		mTargetView.addFooterView(v);
	}

	public void addFooterView(View v, Object data, boolean isSelectable) {
		mTargetView.addFooterView(v, data, isSelectable);
	}

	public boolean removeHeaderView(View v) {
		return mTargetView.removeHeaderView(v);
	}

	public boolean removeFooterView(View v) {
		return mTargetView.removeFooterView(v);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mTargetView.getCount();
	}

	public void smoothScrollToPosition(int position) {
		// TODO Auto-generated method stub
		mTargetView.smoothScrollToPosition(position);
	}

}
