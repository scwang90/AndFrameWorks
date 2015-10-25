package com.andoffice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.andoffice.R;
import com.andframe.view.treeview.AfTreeListView;

public class FlexoListView extends AfTreeListView{

	public FlexoListView(Context context) {
		super(context);
		setPullFooterLayout(new PullRefreshFooter(context));
		setPullHeaderLayout(new PullRefreshHeader(context));
	}

	public FlexoListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPullFooterLayout(new PullRefreshFooter(context));
		setPullHeaderLayout(new PullRefreshHeader(context));
	}

	@Override
	protected ListView onCreateRefreshableView(Context context, AttributeSet attrs) {
		ListView listview = super.onCreateRefreshableView(context, attrs);
		listview.setBackgroundColor(getResources().getColor(R.color.theme_background));
		return listview;
	}
}
