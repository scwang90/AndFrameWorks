package com.andframe.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

import com.andframe.api.Viewer;
import com.andframe.widget.multichoice.AfMultiChoiceAbsListView;
import com.andframe.widget.pulltorefresh.PullRefreshFooterImpl;
import com.andframe.widget.pulltorefresh.PullRefreshHeaderImpl;

/**
 * 可下拉刷新的 上啦更多的 listview 
 * @author 树朾
 */
@SuppressWarnings("unused")
public class AfMultiGridView extends AfMultiChoiceAbsListView<GridView> {

	private static GridView mlistView = null;

	public AfMultiGridView(GridView listView) {
		super((mlistView=listView).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(listView.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(listView.getContext()));
	}

	public AfMultiGridView(Viewer viewable, int res) {
		super((mlistView=viewable.findViewByID(res)).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(viewable.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(viewable.getContext()));
	}

	public AfMultiGridView(Context context) {
		super(context);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	public AfMultiGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	public AfMultiGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	@Override
	protected GridView onCreateTargetView(Context context, AttributeSet attrs) {
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
		return new GridView(context);
	}

//	@Override
//	protected GridView onCreateRefreshableView(Context context, AttributeSet attrs) {
//		mAbsListView = onCreateTargetView(context, attrs);//new GridView(context)
//		// 解决listview在拖动的时候背景图片消失变成黑色背景
//		mAbsListView.setCacheColorHint(0);
//		mAbsListView.setScrollingCacheEnabled(false);
//		// 解决listview的上边和下边有黑色的阴影
//		mAbsListView.setFadingEdgeLength(0);
//		return mAbsListView;
//	}
}
