package com.andoffice.view;

import android.content.Context;
import android.util.AttributeSet;

import com.andoffice.R;
import com.andframe.view.AfContactsListView;
import com.andframe.view.AfContactsRefreshView;

public class ContactsListView extends AfContactsRefreshView{

	public ContactsListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setPullFooterLayout(new PullRefreshFooter(context));
		setPullHeaderLayout(new PullRefreshHeader(context));
	}

	public ContactsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setPullFooterLayout(new PullRefreshFooter(context));
		setPullHeaderLayout(new PullRefreshHeader(context));
	}

	protected AfContactsListView onCreateRefreshableView(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		AfContactsListView listview = super.onCreateRefreshableView(context, attrs);
		listview.setBackgroundColor(getResources().getColor(R.color.theme_background));
		return listview;
	}
}
