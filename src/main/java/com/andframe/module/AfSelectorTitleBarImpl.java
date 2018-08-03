package com.andframe.module;

import android.view.View;
import android.widget.TextView;

import com.andframe.R;
import com.andframe.api.viewer.Viewer;

public class AfSelectorTitleBarImpl extends AfSelectorTitleBar {

	public static final int ID_FINISH = R.id.af_titlebar_select_finish;
	public static final int ID_OPERATE = R.id.af_titlebar_select_operate;

	@SuppressWarnings("unused")
	protected AfSelectorTitleBarImpl() {
	}

	public AfSelectorTitleBarImpl(Viewer page) {
		super(page,R.id.af_titlebar_select);
	}

	@Override
	protected View findTitleSelectBtFinish(Viewer view) {
		return view.findViewById(R.id.af_titlebar_select_finish);
	}
	@Override
	protected View findTitleSelectOperate(Viewer view) {
		return view.findViewById(R.id.af_titlebar_select_operate);
	}
	@Override
	protected TextView findTitleSelectTvText(Viewer view) {
		return view.findViewById(R.id.af_titlebar_select_text);
	}

}
