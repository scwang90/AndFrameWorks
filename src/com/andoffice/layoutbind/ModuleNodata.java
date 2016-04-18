package com.andoffice.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfModuleNodata;
import com.andoffice.R;

public class ModuleNodata extends AfModuleNodata {
	
	public static final int ID_BUTTON = R.id.module_nodata_button;

	public static final int TEXT_NODATA = R.string.module_nodata_description;
	public static final int TEXT_NOFIXED = R.string.module_nodata_nofixed;
	public static final int TEXT_NOFAVORITE = R.string.module_nodata_nofavorite;
	public static final int TEXT_NOBIRTHDAY = R.string.module_nodata_norecord;

	public static final int TEXT_TOREFRESH = R.string.module_nodata_to_refresh;
	public static final int TEXT_TOADD = R.string.module_nodata_to_add;

	private TextView mTvButton = null;
	private TextView mTvDescription = null;
	
	public ModuleNodata(AfPageable page) {
		super(page,R.id.module_nodata_layout);
	}

	@Override
	protected View findRefreshButton(AfViewable view) {
		return view.findViewByID(R.id.module_nodata_button);
	}

	@Override
	protected TextView findDescription(AfViewable view) {
		return view.findViewByID(R.id.module_nodata_description);
	}

//	public void setDescription(String description) {
//		mTvDescription.setText(description);
//	}
//
//	public void setDescription(int id) {
//		mTvDescription.setText(id);
//	}
//
//	public void setButtonText(String text) {
//		mTvButton.setText(text);
//	}
//
//	public void setButtonText(int id) {
//		mTvButton.setId(id);
//		mTvButton.setText(id);
//	}
//
//	public void setOnRefreshListener(OnClickListener listener) {
//		mTvButton.setOnClickListener(listener);
//		if(listener != null){
//			mTvButton.setVisibility(View.VISIBLE);
//		}else{
//			mTvButton.setVisibility(View.GONE);
//		}
//	}
//
//	public TextView getButton() {
//		return mTvButton;
//	}
}
