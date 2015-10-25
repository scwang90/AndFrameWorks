package com.andoffice.layoutbind;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfSelectorBottombar;
import com.andoffice.R;

public class ModuleBottombarSelector extends AfSelectorBottombar{

	public static final int ID_EDIT = R.drawable.bottom_edit;
	public static final int ID_DELETE = R.drawable.bottom_delete;
	public static final int ID_OK = R.drawable.icon_ok;

	public static final String DETAIL_EDIT = "编辑";
	public static final String DETAIL_DELETE = "删除";
	public static final String DETAIL_OK = "完成";
	
	public ModuleBottombarSelector(AfPageable page) {
		super(page,R.id.bottombar_select_layout);
	}
	
	@Override
	protected ImageView getFunctionViewMore(AfViewable view) {
		return view.findViewByID(R.id.bottombar_select_more);
	}

	@Override
	protected LinearLayout getFunctionLayout(AfViewable view) {
		return view.findViewByID(R.id.bottombar_select_contain);
	}
	
	@Override
	protected int getSelectorDrawableResId() {
		return R.drawable.selector_background;
	}

}
