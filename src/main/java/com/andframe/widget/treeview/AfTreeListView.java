package com.andframe.widget.treeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.andframe.widget.multichoice.AfMultiChoiceAbsListView;
import com.andframe.widget.multichoice.AfMultiChoiceAdapter;

public class AfTreeListView extends AfMultiChoiceAbsListView<ListView> {

	protected AfTreeViewAdapter<? extends Object> mAdapter = null;
	
	public AfTreeListView(Context context) {
		super(context);
	}
	
	public AfTreeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AfTreeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Deprecated. Use {@link #setAdapter(AfTreeViewAdapter adapter)} from
	 * now on.
	 * @deprecated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if(adapter instanceof AfTreeViewAdapter){
			mAdapter = (AfTreeViewAdapter<? extends Object>) adapter;
		}
	}

	/**
	 * Deprecated. Use {@link #setAdapter(AfTreeViewAdapter adapter)} from
	 * now on.
	 * @deprecated
	 */
	@Override
	public void setAdapter(AfMultiChoiceAdapter<?> adapter) {
		super.setAdapter(adapter);
		if(adapter instanceof AfTreeViewAdapter){
			mAdapter = (AfTreeViewAdapter<?>) adapter;
		}
	}

	public void setAdapter(AfTreeViewAdapter<?> adapter) {
		super.setAdapter(adapter);
		mAdapter = adapter;
	}

    protected ListView onCreateTargetView(Context context, AttributeSet attrs) {
        return new ListView(context);
    }

	@Override
	public void onItemClick(AdapterView<?> adview, View view, int index, long id) {
		int position = index - mTargetView.getHeaderViewsCount();
		if (position < 0 || mAdapter == null || mAdapter.isMultiChoiceMode()
				|| mAdapter.isItemClickable(position)) {
			super.onItemClick(adview, view, index, id);
		} else {
			mAdapter.onItemClick(position);
		}
//		if (mAdapter != null && mAdapter.isMultiChoiceMode()) {
//			super.onItemClick(adview, view, index, id);
//		}else if(mAdapter != null){
//			mAdapter.onItemClick(getDataIndex(index));
//		}else if (mItemClickListener != null) {
//			mItemClickListener.onItemClick(adview, view, index, id);
//		}
	}
}
