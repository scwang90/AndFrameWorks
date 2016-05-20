package com.andframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.activity.framework.AfViewable;
import com.andframe.view.pulltorefresh.PullRefreshFooterImpl;
import com.andframe.view.pulltorefresh.PullRefreshHeaderImpl;

/**
 * 可下拉刷新的 上啦更多的 listview 
 * @author 树朾
 *
 */
public class AfListView extends AfRefreshAbsListView<ListView> {
	
	private static ListView mlistView = null;

	public AfListView(ListView listView) {
		super((mlistView=listView).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(listView.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(listView.getContext()));
	}

	public AfListView(AfViewable viewable,int res) {
		super((mlistView=viewable.findViewByID(res)).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(viewable.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(viewable.getContext()));
	}
	
	public AfListView(Context context) {
		super(context);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	public AfListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	public AfListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		mTargetView.setAdapter(adapter);
	}

	@Override
	protected ListView onCreateTargetView(Context context, AttributeSet attrs) {
		if (mlistView != null) {
			if (getParent() == null && mlistView.getParent() instanceof ViewGroup) {
				ViewGroup parent = ViewGroup.class.cast(mlistView.getParent());
				int index = parent.indexOfChild(mlistView);
				parent.removeView(mlistView);
				parent.addView(this, index,mlistView.getLayoutParams());
				mTargetView = mlistView;
				mlistView = null;
			}
			return mTargetView;
		}
		return new ListView(context);
	}

//	@Override
//	protected ListView onCreateRefreshableView(Context context, AttributeSet attrs) {
//		mAbsListView = onCreateTargetView(context, attrs);//new ListView(context)
//		// 解决listview在拖动的时候背景图片消失变成黑色背景
//		mAbsListView.setCacheColorHint(0);
//		mAbsListView.setScrollingCacheEnabled(false);
//		// 解决listview的上边和下边有黑色的阴影
//		mAbsListView.setFadingEdgeLength(0);
//
//		if(Build.VERSION.SDK_INT >= 16){
//			mAbsListView.setDivider(getDividerDrawable());
//		}else if(Build.VERSION.SDK_INT >= 14){
//			mAbsListView.setDividerHeight(getDividerPadding());
//			mAbsListView.setDivider(new BitmapDrawable(getResources(),
//					getDrawingCache()));
//		}else{
//			mAbsListView.setDividerHeight(1);
//			mAbsListView.setDivider(new BitmapDrawable(getResources(),
//					getDrawingCache()));
//		}
//		return mAbsListView;
//	}

	/**
	 * Returns the number of header views in the list. Header views are special
	 * views at the top of the list that should not be recycled during a layout.
	 * @return
	 */
	public final int getHeaderViewsCount() {
		return mTargetView.getHeaderViewsCount() - 1;
	}

	/**
	 * 获取 OnItemClick 中的index 对应ListView 中的index 包含 HeaderView
	 * @param index
	 * @return
	 */
	public final int getIndex(int index) {
		return index - 0;//1;
	}

	/**
	 * 获取 OnItemClick 中的index 对应Adapter 中的index
	 * 主要用于当ListView中有Header的时候 可以排除Header产生的index偏移
	 * @param index
	 * @return index-headercount (小于0时代表点击的是header)
	 */
	public final int getDataIndex(int index) {
//		if (index < mTargetView.getHeaderViewsCount()) {
//			return index;
//		}
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

}
