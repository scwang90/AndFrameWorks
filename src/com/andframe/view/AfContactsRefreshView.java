package com.andframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;

import com.andframe.adapter.AfContactsAdapter;

public class AfContactsRefreshView extends AfRefreshListView<AfContactsListView>{

	public AfContactsRefreshView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AfContactsRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AfContactsRefreshView(Context context) {
		super(context);
	}
	
	@Override
	protected AfContactsListView onCreateListView(Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new AfContactsListView(context,attrs);
	}
	
	public void setHeaderView(View view) {
		mListView.setHeaderView(view);
	}

	/**
	 * @deprecated User {@link AfContactsListView#setAdapter(AfContactsAdapter)}} from now on.
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
		throw new NullPointerException("请使用 setAdapter(AfContactsAdapter adapter)");
	}

	@SuppressWarnings("rawtypes")
	public void setAdapter(AfContactsAdapter adapter) {
		// TODO Auto-generated method stub
		mListView.setAdapter(adapter);
	}
}
