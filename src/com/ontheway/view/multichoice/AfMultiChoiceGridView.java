package com.ontheway.view.multichoice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.ontheway.layoutbind.AfSelectorBottombar;
import com.ontheway.layoutbind.AfSelectorTitlebar;
import com.ontheway.view.AfRefreshGridView;

public class AfMultiChoiceGridView extends AfRefreshGridView implements
		OnItemLongClickListener, OnItemClickListener {
	protected OnItemClickListener mItemClickListener = null;
	protected OnItemLongClickListener mItemLongClickListener = null;
	protected AfMultiChoiceAdapter<? extends Object> mAdapter = null;
	protected AfSelectorTitlebar mSelectorTitlebar = null;
	protected AfSelectorBottombar mSelectorBottombar = null;

	public AfMultiChoiceGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		super.setOnItemClickListener(this);
		super.setOnItemLongClickListener(this);
	}

	public AfMultiChoiceGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		super.setOnItemClickListener(this);
		super.setOnItemLongClickListener(this);
	}

	public AfMultiChoiceGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		super.setOnItemClickListener(this);
		super.setOnItemLongClickListener(this);
	}
	
	@Override
	protected GridView onCreateGridView(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new GridView(context);
	}

	@Override
	public void setOnItemClickListener(OnItemClickListener listener) {
		// TODO Auto-generated method stub
		mItemClickListener = listener;
	}

	@Override
	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		// TODO Auto-generated method stub
		mItemLongClickListener = listener;
	}

	/**
	 * Deprecated. Use {@link #setAdapter(AfMultiChoiceAdapter adapter)} from
	 * now on.
	 * @deprecated
	 */
	@SuppressWarnings("unchecked")
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		if(adapter instanceof AfMultiChoiceAdapter){
			mAdapter = (AfMultiChoiceAdapter<? extends Object>) adapter;
			if(mSelectorTitlebar != null){
				mSelectorTitlebar.setAdapter(mAdapter);
			}
			if(mSelectorBottombar != null){
				mSelectorBottombar.setAdapter(mAdapter);
			}
			
		}
	}

	public void setAdapter(AfMultiChoiceAdapter<? extends Object> adapter) {
		super.setAdapter(adapter);
		mAdapter = adapter;
		if(mSelectorTitlebar != null){
			mSelectorTitlebar.setAdapter(adapter);
		}
		if(mSelectorBottombar != null){
			mSelectorBottombar.setAdapter(mAdapter);
		}
	}
	
	public void setSelector(AfSelectorTitlebar selector) {
		this.mSelectorTitlebar = selector;
	}

	public void setSelector(AfSelectorBottombar selector) {
		// TODO Auto-generated method stub
		this.mSelectorBottombar = selector;
	}
	
	@Override
	public void onItemClick(AdapterView<?> adview, View view, int index, long id) {
		// TODO Auto-generated method stub
		if (mAdapter != null && mAdapter.isMultiChoiceMode()) {
			mAdapter.onItemClick((index));
		} else if (mItemClickListener != null) {
			mItemClickListener.onItemClick(adview, view, index, id);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adview, View view, int index,
			long id) {
		// TODO Auto-generated method stub
		if (mAdapter != null && !mAdapter.isMultiChoiceMode()) {
			mAdapter.beginMultiChoice((index));
			return true;
		} else if (mItemLongClickListener != null) {
			return mItemLongClickListener.onItemLongClick(adview, view, index,id);
		}
		return false;
	}

	public boolean isMultiChoiceMode() {
		if(mAdapter == null){
			return false;
		}
		return mAdapter.isMultiChoiceMode();
	}

	public boolean beginMultiChoice() {
		if(mAdapter == null){
			return false;
		}
		return mAdapter.beginMultiChoice();
	}

}
