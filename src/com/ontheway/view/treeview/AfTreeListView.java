package com.ontheway.view.treeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.ontheway.view.multichoice.AfMultiChoiceAdapter;
import com.ontheway.view.multichoice.AfMultiChoiceListView;

public class AfTreeListView extends AfMultiChoiceListView{

	protected AfTreeViewAdapter<? extends Object> mAdapter = null;
	
	public AfTreeListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public AfTreeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
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
	public void setAdapter(AfMultiChoiceAdapter<? extends Object> adapter) {
		super.setAdapter(adapter);
		if(adapter instanceof AfTreeViewAdapter){
			mAdapter = (AfTreeViewAdapter<? extends Object>) adapter;
		}
	}

	public void setAdapter(AfTreeViewAdapter<? extends Object> adapter) {
		super.setAdapter(adapter);
		mAdapter = adapter;
	}
	
	@Override
	public void onItemClick(AdapterView<?> adview, View view, int index, long id) {
		// TODO Auto-generated method stub
		if(mAdapter == null || mAdapter.isMultiChoiceMode()
				|| mAdapter.isItemClickable(getDataIndex(index))){
			super.onItemClick(adview, view, index, id);
		}else{
			mAdapter.onItemClick(getDataIndex(index));
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
