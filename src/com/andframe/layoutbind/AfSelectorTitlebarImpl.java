package com.andframe.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.activity.framework.AfPageable;
import com.andframe.activity.framework.AfViewable;

public class AfSelectorTitlebarImpl extends AfSelectorTitlebar {

	public static final int ID_FINISH = R.id.af_titlebar_select_finish;
	public static final int ID_OPERATE = R.id.af_titlebar_select_operate;

	public AfSelectorTitlebarImpl(AfPageable page) {
		super(page,R.id.af_titlebar_select);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected View findTitleSelectBtFinish(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewById(R.id.af_titlebar_select_finish);
	}
	@Override
	protected View findTitleSelectOperate(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewById(R.id.af_titlebar_select_operate);
	}
	@Override
	protected TextView findTitleSelectTvText(AfViewable view) {
		// TODO Auto-generated method stub
		return view.findViewByID(R.id.af_titlebar_select_text);
	}

}
