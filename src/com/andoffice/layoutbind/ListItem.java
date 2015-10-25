package com.andoffice.layoutbind;

import android.view.View;

import com.andframe.activity.framework.AfView;
import com.andframe.feature.AfDensity;
import com.andframe.model.framework.AfModel;
import com.andoffice.layoutbind.framework.AbListItem;

public class ListItem<T> extends AbListItem<T>{

	public ListItem() {
		mSelectDisplay = SD_CHECK;
	}

	@Override
	public void onHandle(AfView view) {
		super.onHandle(view);
		mTvLeft1.setMaxLines(2);
		mTvLeft1.setSingleLine(false);
		mTvLeft2.setVisibility(View.GONE);
		mTvCorner.setVisibility(View.GONE);
		mTvRight1.setVisibility(View.GONE);
		mTvRight2.setVisibility(View.GONE);
		mTvSignNew.setVisibility(View.GONE);
	}
	@Override
	protected boolean onBinding(T data,int index,SelectStatus status) {
		if (data instanceof AfModel) {
			AfModel model = AfModel.class.cast(data);
			mTvTitle.setText(model.Name);
			if(model.Remark == null || model.Remark.equals("")){
				int padding = AfDensity.dip2px(mTvCorner.getContext(),5);
				mTvLeft1.setVisibility(View.GONE);
				mTvTitle.setPadding(0, padding, 0, padding);
			}else{
				mTvLeft1.setText(model.Remark);
				mTvLeft1.setVisibility(View.VISIBLE);
				mTvTitle.setPadding(0, 0, 0, 0);
			}
		}else if(data != null){
			mTvTitle.setText(data.toString());
			mTvLeft1.setVisibility(View.GONE);
		}else {
			mTvTitle.setText("[null]");
			mTvLeft1.setVisibility(View.GONE);
		}
		return false;
	}

}
