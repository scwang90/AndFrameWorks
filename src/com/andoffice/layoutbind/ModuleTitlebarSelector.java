package com.andoffice.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andoffice.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;
import com.andframe.layoutbind.AfSelectorTitlebar;

public class ModuleTitlebarSelector extends AfSelectorTitlebar {
	
	public static final int ID_FINISH = R.id.titlebar_select_finish;
	public static final int ID_OPERATE = R.id.titlebar_select_operate;

	public ModuleTitlebarSelector(AfPageable page) {
		super(page,R.id.titlebar_select);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected View findTitleSelectBtFinish(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewById(R.id.titlebar_select_finish);
	}
	@Override
	protected View findTitleSelectOperate(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewById(R.id.titlebar_select_operate);
	}
	@Override
	protected TextView findTitleSelectTvText(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewByID(R.id.titlebar_select_text);
	}

}
