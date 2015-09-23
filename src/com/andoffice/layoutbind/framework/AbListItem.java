package com.andoffice.layoutbind.framework;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andframe.activity.framework.AfView;
import com.andframe.view.multichoice.AfMultiChoiceItem;
import com.andoffice.R;

public abstract class AbListItem<T> extends AfMultiChoiceItem<T>{

	protected ViewGroup mLayout = null;
	protected ViewGroup mLayoutTitle = null;
	protected ImageView mIvHeadimg = null;
	protected TextView mTvTitle = null;
	protected TextView mTvCorner = null;
	protected TextView mTvLeft1 = null;
	protected TextView mTvLeft2 = null;
	protected TextView mTvRight1 = null;
	protected TextView mTvRight2 = null;
	protected TextView mTvSignNew = null;
	protected TextView mIvTitleLeft = null;
	
	@Override
	public void onHandle(AfView view) {
		mLayout = view.findViewByID(R.id.listitem_common_layout);
		mLayoutTitle = view.findViewByID(R.id.listitem_common_titlelayout);
		mTvTitle = view.findViewByID(R.id.listitem_common_title);
		mTvCorner = view.findViewByID(R.id.listitem_common_date);
		mTvLeft1 = view.findViewByID(R.id.listitem_common_left1);
		mTvLeft2 = view.findViewByID(R.id.listitem_common_left2);
		mTvRight1 = view.findViewByID(R.id.listitem_common_right1);
		mTvRight2 = view.findViewByID(R.id.listitem_common_right2);
		mIvTitleLeft= view.findViewByID(R.id.listitem_common_titleleft);
		mTvSignNew= view.findViewByID(R.id.listitem_common_signednew);
		mIvHeadimg= view.findViewByID(R.id.listitem_common_headimg);
	}
	
	@Override
	public final int getLayoutId() {
		return R.layout.listitem_common;
	}

}
