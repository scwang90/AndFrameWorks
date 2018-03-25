package com.andframe.module;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andframe.R;
import com.andframe.api.viewer.Viewer;

public class AfSelectorBottomBarImpl extends AfSelectorBottomBar {

	public static final int ID_EDIT = R.drawable.af_bottom_edit;
	public static final int ID_DELETE = R.drawable.af_bottom_delete;
	public static final int ID_OK = R.drawable.af_icon_ok;

	public static final String DETAIL_EDIT = "编辑";
	public static final String DETAIL_DELETE = "删除";
	public static final String DETAIL_OK = "完成";

	@SuppressWarnings("unused")
	protected AfSelectorBottomBarImpl() {
	}

	public AfSelectorBottomBarImpl(Viewer page) {
		super(page,R.id.af_bottombar_select_layout);
	}
	
	@Override
	protected ImageView getFunctionViewMore(Viewer view) {
		return view.findViewById(R.id.af_bottombar_select_more);
	}

	@Override
	protected LinearLayout getFunctionLayout(Viewer view) {
		return view.findViewById(R.id.af_bottombar_select_contain);
	}
	
	@Override
	protected int getSelectorDrawableResId() {
		return R.drawable.af_selector_background;
	}
}
