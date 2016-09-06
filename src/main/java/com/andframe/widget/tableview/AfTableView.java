package com.andframe.widget.tableview;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AfTableView extends HorizontalScrollView {

	protected ListView mListView;
	protected LinearLayout mLayout;
	protected AfTableHeader mHeader = null;
	protected AfTableAdapter mAdapter = null;

	public AfTableView(Context context) {
		super(context);
		InitializeComponent(context);
	}

	public AfTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitializeComponent(context);
	}

	protected void InitializeComponent(Context context) {
		mLayout = new LinearLayout(context);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mListView = new ListView(context);
		mListView.setDividerHeight(0);
		// 解决listview在拖动的时候背景图片消失变成黑色背景
		mListView.setCacheColorHint(0);
		mListView.setScrollingCacheEnabled(false);
		// 解决listview的上边和下边有黑色的阴影
		mListView.setFadingEdgeLength(0);

		mLayout.addView(mListView);
		this.addView(mLayout);
	}

	public void setAdapter(AfTableAdapter adapter) {
		mAdapter = adapter;
		if (mHeader != null) {
			mLayout.removeView(mHeader);
			mHeader = null;
		}
		AfTable table = mAdapter.getTable();
		setBackground(this, table.BackgroundId);
		mHeader = new AfTableHeader(getContext(), table);
		mLayout.addView(mHeader, 0);
		mListView.setAdapter(adapter);
	}

	public static void setBackground(View view, int back) {
		try {
			view.setBackgroundResource(back);
		} catch (NotFoundException e) {
			view.setBackgroundColor(back);
		}
	}

	public static class AfTableHeader extends LinearLayout {

		protected static final int TYPEVALUE = TypedValue.COMPLEX_UNIT_SP;
		protected static final int PARENT = LayoutParams.MATCH_PARENT;
		protected static final int CONTENT = LayoutParams.WRAP_CONTENT;

		protected TextView[] mTextViews;

		public AfTableHeader(Context context, AfTable table) {
			super(context);
			setOrientation(HORIZONTAL);
			setBackground(this, table.BackgroundIdHeader);
			setLayoutParams(new LayoutParams(CONTENT, table.HeaderHeight));

			mTextViews = new TextView[table.Column];
			for (int i = 0; i < mTextViews.length; i++) {
				mTextViews[i] = new TextView(context);
				mTextViews[i].setSingleLine(true);
				mTextViews[i].setGravity(Gravity.CENTER);
				mTextViews[i].setText(table.getColumns()[i].Header);
				mTextViews[i].setTextColor(table.TextcolorHeader);
				mTextViews[i].setTextSize(TYPEVALUE, table.TextsizeHeader);
				if ((i & 1) == 1) {
					setBackground(mTextViews[i], table.BackgroundIdHeaderCell);
				}
				LayoutParams lpar = new LayoutParams(table.ltParam[i]);
				lpar.height = table.HeaderHeight;
				this.addView(mTextViews[i], lpar);
			}
		}

		public static void setBackground(View view, int back) {
			try {
				view.setBackgroundResource(back);
			} catch (NotFoundException e) {
				view.setBackgroundColor(back);
			}
		}
	}

}
