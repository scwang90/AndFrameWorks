package com.andoffice.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andframe.activity.framework.AfView;
import com.andframe.view.treeview.AfTreeViewItem;
import com.andoffice.R;

public abstract class ItemTree<T> extends AfTreeViewItem<T>{

	protected TextView mTvTitle = null;
	protected TextView mTvCorner = null;
	protected TextView mTvLeft1 = null;
	protected TextView mTvLeft2 = null;
	protected TextView mTvRight1 = null;
	protected TextView mTvRight2 = null;
	protected TextView mTvSignNew = null;
	
	@Override
	public void onHandle(AfView view) {
		// TODO Auto-generated method stub
		mTvTitle = view.findViewByID(R.id.listitem_common_title);
		mTvCorner = view.findViewByID(R.id.listitem_common_date);
		mTvLeft1 = view.findViewByID(R.id.listitem_common_left1);
		mTvLeft2 = view.findViewByID(R.id.listitem_common_left2);
		mTvRight1 = view.findViewByID(R.id.listitem_common_right1);
		mTvRight2 = view.findViewByID(R.id.listitem_common_right2);
		mTvSignNew= view.findViewByID(R.id.listitem_common_signednew);
		mTvSignNew.setVisibility(View.GONE);
	}

	@Override
	public final int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.listitem_common;
	}

}
