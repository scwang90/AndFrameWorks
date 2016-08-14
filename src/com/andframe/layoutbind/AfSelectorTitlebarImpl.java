package com.andframe.layoutbind;

import android.view.View;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.activity.framework.AfViewable;

public class AfSelectorTitlebarImpl extends AfSelectorTitlebar {

	public static final int ID_FINISH = R.id.af_titlebar_select_finish;
	public static final int ID_OPERATE = R.id.af_titlebar_select_operate;

	@SuppressWarnings("unused")
	protected AfSelectorTitlebarImpl() {
	}

	public AfSelectorTitlebarImpl(AfViewable page) {
		super(page,R.id.af_titlebar_select);
	}

	@Override
	protected View findTitleSelectBtFinish(AfViewable view) {
		return view.findViewById(R.id.af_titlebar_select_finish);
	}
	@Override
	protected View findTitleSelectOperate(AfViewable view) {
		return view.findViewById(R.id.af_titlebar_select_operate);
	}
	@Override
	protected TextView findTitleSelectTvText(AfViewable view) {
		return view.findViewByID(R.id.af_titlebar_select_text);
	}

}
