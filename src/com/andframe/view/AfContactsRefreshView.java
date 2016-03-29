package com.andframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.adapter.AfContactsAdapter;
import com.andframe.view.pulltorefresh.PullRefreshFooterImpl;
import com.andframe.view.pulltorefresh.PullRefreshHeaderImpl;

public class AfContactsRefreshView extends AfRefreshListView<AfContactsListView> {

	private static AfContactsListView mlistView = null;

	public AfContactsRefreshView(AfContactsListView listView) {
		super((mlistView = listView).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(listView.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(listView.getContext()));
	}

	public AfContactsRefreshView(AfViewable viewable, int res) {
		super((mlistView = viewable.findViewByID(res)).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(viewable.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(viewable.getContext()));
	}

	public AfContactsRefreshView(Context context) {
		super(context);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	public AfContactsRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	public AfContactsRefreshView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	@Override
	protected AfContactsListView onCreateListView(Context context, AttributeSet attrs) {
		if (mlistView != null) {
			if (getParent() == null && mlistView.getParent() instanceof ViewGroup) {
				ViewGroup parent = ViewGroup.class.cast(mlistView.getParent());
				int index = parent.indexOfChild(mlistView);
				parent.removeView(mlistView);
				parent.addView(this, index, mlistView.getLayoutParams());
				mTargetView = mlistView;
				mlistView = null;
			}
			return mTargetView;
		}
		return new AfContactsListView(context);
	}

	@Override
	protected AfContactsListView onCreateRefreshableView(Context context, AttributeSet attrs) {
		mListView = onCreateListView(context, attrs);//new ListView(context)
		// 解决listview在拖动的时候背景图片消失变成黑色背景
		mListView.setCacheColorHint(0);
		mListView.setScrollingCacheEnabled(false);
		// 解决listview的上边和下边有黑色的阴影
		mListView.setFadingEdgeLength(0);
		return mListView;
	}

	public void setHeaderView(View view) {
		mListView.setHeaderView(view);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		throw new NullPointerException("请使用 setAdapter(AfContactsAdapter adapter)");
	}

	@SuppressWarnings("rawtypes")
	public void setAdapter(AfContactsAdapter adapter) {
		mListView.setAdapter(adapter);
	}
}
